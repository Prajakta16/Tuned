package com.example.Tuned;

import com.example.Tuned.model.Artist;
import com.example.Tuned.model.Song;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.jayway.jsonpath.JsonPath;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Spotify {

    public String getNewAccessCode() {

        String accessCodeUrl = "https://accounts.spotify.com/api/token";
        String access_token = null;

        URL url = null;
        try {
            url = new URL(accessCodeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            String url_parameter = "grant_type=client_credentials";
            byte[] postData = url_parameter.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            String client_id = "a889b998641f47f7a255a3a9142839f3";
            String client_secret = "ce9064596f79422bb631c13bbaf3ebed";
            String code = client_id + ":" + client_secret;
            System.out.println("code " + code);
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(code.getBytes()));
            System.out.println(basicAuth);

            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
                connection.connect();
                int responseCode = connection.getResponseCode();
                System.out.println(responseCode);

                if (responseCode != 200)
                    throw new RuntimeException("HttpResponseCode for getting access token " + responseCode);
                else {
                    StringBuilder response = new StringBuilder();
                    String strCurrentLine;

                    BufferedReader objReader = new BufferedReader(new InputStreamReader((connection.getInputStream()), StandardCharsets.UTF_8));

                    while ((strCurrentLine = objReader.readLine()) != null) {
                        System.out.println(strCurrentLine);
                        response.append(strCurrentLine);
                    }
                    objReader.close();

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
                    access_token = (String) jsonObject.get("access_token");
                    return access_token;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray searchSong(String access_token, String title) throws URISyntaxException {

        URI baseUrl = new URI("https://api.spotify.com/v1/search");
        URI final_uri = applyParameters(baseUrl, new String[]{"q", title, "type", "track", "limit", "3"});
        System.out.println(final_uri.toString());
        JSONArray jsonArray = new JSONArray();
        try {
            String bearerAuth = "Bearer " + access_token;
            HttpURLConnection connection = (HttpURLConnection) final_uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", bearerAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);

            if (responseCode != 200)
                throw new RuntimeException("HttpResponseCode for getting access token " + responseCode);
            else {
                StringBuilder song_response = new StringBuilder();
                String curr_line;
                BufferedReader objReader = new BufferedReader(new InputStreamReader((connection.getInputStream()), StandardCharsets.UTF_8));

                while ((curr_line = objReader.readLine()) != null) {
                    System.out.println(curr_line);
                    song_response.append(curr_line);
                }
                objReader.close();
                String song_json = song_response.toString();

                Integer count_items = JsonPath.read(song_json, "$.tracks.items.length()"); //counts the number of songs in json response

                for (int i = 0; i < count_items; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title",JsonPath.read(song_json, "$.tracks.items[" + i + "].name"));
                    jsonObject.put("spotify_url",JsonPath.read(song_json, "$.tracks.items[" + i + "].external_urls.spotify"));
                    jsonObject.put("spotify_id",JsonPath.read(song_json, "$.tracks.items[" + i + "].id"));
                    jsonObject.put("popularity",JsonPath.read(song_json, "$.tracks.items[" + i + "].popularity"));
                    jsonObject.put("duration",JsonPath.read(song_json, "$.tracks.items[" + i + "].duration_ms"));
                    jsonObject.put("preview_url",JsonPath.read(song_json,"$.tracks.items[" + i + "].preview_url"));

                    Map m = new LinkedHashMap(6);
                    m.put("title", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.name"));
                    m.put("spotify_url", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.external_urls.spotify"));
                    m.put("spotify_id", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.id"));
                    m.put("image_url", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.images[2].url"));
                    m.put("release_year", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.release_date"));

                    Integer count_artists_in_album = JsonPath.read(song_json, "$.tracks.items[" + i + "].album.artists.length()");
                    JSONArray ja = new JSONArray();
                    System.out.println(count_artists_in_album);
                    for (int j = 0; j < count_artists_in_album; j++) {
                        Map mArtist = new LinkedHashMap(2);
                        mArtist.put("name", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.artists[" + j + "].name"));
                        mArtist.put("spotify_id", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.artists[" + j + "].id"));
                        mArtist.put("spotify_url", JsonPath.read(song_json, "$.tracks.items[" + i + "].album.artists[" + j + "].external_urls.spotify"));
                        ja.add(mArtist); // adding map to array
                    }
                    //jsonObject.put("artists",ja);
                    m.put("artists",ja);
                    jsonObject.put("album", m); // putting album to JSONObject of song

                    jsonArray.add(jsonObject);
                }
            }
            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public URI applyParameters(URI baseUri, String[] urlParameters) {
        StringBuilder query = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < urlParameters.length; i += 2) {
            if (first) {
                first = false;
            } else {
                query.append("&");
            }
            query.append(urlParameters[i]).append("=")
                    .append(urlParameters[i + 1]);
        }
        try {
            return new URI(baseUri.getScheme(), baseUri.getAuthority(),
                    baseUri.getPath(), query.toString(), null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
