package project2_multithreading;
import java.nio.file.Path;

import org.json.simple.JSONObject;

import project1_librarybuilding.MusicLibrary;
import project1_librarybuilding.Song;

/**
 * Maintains a thread-safe music library of several songs using reentrant locks.
 *
 */
public class ThreadSafeMusicLibrary extends MusicLibrary {

	private ReentrantLock lock;

	public ThreadSafeMusicLibrary() {
		super();
		lock = new ReentrantLock();
	}

	@Override
	/**
	 * Add a song to the library. Adds a reference to the song object to all
	 * appropriate data structures.
	 * 
	 * @param song
	 *            to be added to each data structure
	 * 
	 */
	public void addSong(Song song) {
		lock.lockWrite();
		super.addSong(song);
		lock.unlockWrite();
	}

	@Override
	/**
	 * Calls a method to write the library to a text file sorted by the given
	 * order.
	 * 
	 * @param output
	 *            file to write music library to
	 * @param order
	 *            that the library should be sorted by
	 */
	public void writeToOutput(Path output, String order) {
		lock.lockRead();
		super.writeToOutput(output, order);
		lock.unlockRead();
	}

	@Override
	public JSONObject getJSONSearchByTag(String tag) {
		lock.lockRead();
		JSONObject obj = super.getJSONSearchByTag(tag);
		lock.unlockRead();
		return obj;
	}

	@Override
	public JSONObject getJSONSearchByArtist(String artist) {
		lock.lockRead();
		JSONObject obj = super.getJSONSearchByArtist(artist);
		lock.unlockRead();
		return obj;
	}

	@Override
	public JSONObject getJSONSearchByTitle(String title) {
		lock.lockRead();
		JSONObject obj = super.getJSONSearchByTitle(title);
		lock.unlockRead();
		return obj;
	}

	/******************* METHODS FOR DEBUGGING ***********************/
	@Override
	/**
	 * Prints the library sorted by tag to console
	 */
	public void tagMapToString() {
		lock.lockRead();
		try {
			super.tagMapToString();
		} finally {
			lock.unlockRead();
		}
	}

	@Override
	/**
	 * Prints the library sorted by title to console
	 */
	public void titleMapToString() {
		lock.lockRead();
		try {
			super.titleMapToString();
		} finally {
			lock.unlockRead();
		}
	}

	@Override
	/**
	 * Prints the library sorted by Artist name to console
	 **/
	public void artistMapToString() {
		lock.lockRead();
		try {
			super.artistMapToString();
		} finally {
			lock.unlockRead();
		}
	}

}
