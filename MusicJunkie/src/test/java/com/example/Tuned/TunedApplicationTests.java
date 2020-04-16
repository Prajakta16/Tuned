package com.example.Tuned;

import com.example.Tuned.dao.ListenerDao;
import com.example.Tuned.dao.PlaylistDao;
import com.example.Tuned.dao.SongDao;
import com.example.Tuned.model.Listener;
import com.example.Tuned.model.Playlist;
import com.example.Tuned.model.Song;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class TunedApplicationTests {

	@Autowired
	ListenerDao listenerDao;

	@Autowired
	PlaylistDao playlistDao;

	@Autowired
	SongDao songDao;

	@BeforeAll
	void setup(){
		Listener listener1 = new Listener("pdharme","lpass","Prajakta","Dharme",617407,"address","pdharme7@gmail.com","Pop","Rock");
		Listener listener2 = new Listener("ak47","lpass","Akshat","Shukla",617393,"address","aks@gmail.com","Pop","Rock");
		listenerDao.createListener(listener1);
		listenerDao.createListener(listener2);


		Playlist playlist1 = new Playlist("play1","desc1");
		Playlist playlist2 = new Playlist("play2","desc2");
		Playlist playlist3 = new Playlist("play3","desc3");

		Song song1 = new Song("song1","pop","desc1",240,1996,1,1);
		Song song2 = new Song("song2","pop","desc2",290,2020,1,1);
		Song song3 = new Song("song3","pop","desc3",280,2018,1,1);
		songDao.createSong(song1);
		songDao.createSong(song2);
		songDao.createSong(song3);

		playlistDao.createPlaylistForListener(playlist1, listener1);
		playlistDao.createPlaylistForListener(playlist2,listener2);
		playlistDao.createPlaylistForListener(playlist3,listener2);

		playlistDao.addSongToPlaylist(song1.getSong_id(),playlist1.getPlaylist_id());
		playlistDao.addSongToPlaylist(song2.getSong_id(),playlist1.getPlaylist_id());
		playlistDao.addSongToPlaylist(song3.getSong_id(),playlist3.getPlaylist_id());

	}

	@Test
	void countListeners() {
		assertEquals(2, listenerDao.findAllListeners().size());
	}

	@Test
	void countPlaylists() {
		assertEquals(3, playlistDao.findAllPlaylists().size());
	}

	@Test
	void countSongs(){
		assertEquals(3, songDao.findAllSongs().size());
	}

//	@Test
//	void deleteSong(){
//		Song s = songDao.findSongById(2);
//		songDao.deleteSong(s);
////		assertEquals(2, songDao.findAllSongs().size());
////		assertEquals(3, playlistDao.findAllPlaylists().size());
//	}

//	@Test
//	void deletePlaylist(){
//		Playlist p = playlistDao.findPlaylistById(1);
//		playlistDao.deletePlaylist(p);
//		assertEquals(2, playlistDao.findAllPlaylists().size());
//		assertEquals(2, songDao.findAllSongs().size());
//	}
//
//	@Test
//	void countPlaylistsForListener() {
//		assertEquals(2, playlistDao.findAllPlaylistsForListener(1));
//	}

}
