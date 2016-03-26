import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;

public class ThreadSafeMusicLibrary extends MusicLibrary {
//	/**
//	 * Data structure to map tag -> treeset of track ids with the tag
//	 */
//	private final TreeMap<String, TreeSet<String>> tagMap;
//
//	/**
//	 * data structure to map song title -> TreeSet of Songs sorted by the title
//	 * comparator -sorted by title
//	 */
//	private final TreeMap<String, TreeSet<Song>> titleMap;
//
//	/**
//	 * data structure to map artist name -> TreeSet of Songs sorted by the
//	 * artist comparator
//	 * 
//	 */
//	private final TreeMap<String, TreeSet<Song>> artistMap;
//
//	/**
//	 * Constructor to initialize Data Structures
//	 */
//	public ThreadSafeMusicLibrary() {
//		this.tagMap = new TreeMap<String, TreeSet<String>>();
//		this.titleMap = new TreeMap<String, TreeSet<Song>>();
//		this.artistMap = new TreeMap<String, TreeSet<Song>>();
//
//	}
//
//	/**
//	 * Add a song to the library. Adds a reference to the song object to all
//	 * appropriate data structures.
//	 * 
//	 * @param song
//	 *            to be added to each data structure
//	 * 
//	 */
//	public synchronized void addSong(Song song) {
//
//		if (this.titleMap.get(song.getTitle()) == null) {
//			titleMap.put(song.getTitle(), new TreeSet<Song>(new ByTitleComparator()));
//		}
//		this.titleMap.get(song.getTitle()).add(song);
//
//		if (this.artistMap.get(song.getArtist()) == null) {
//			this.artistMap.put(song.getArtist(), new TreeSet<Song>(new ByArtistComparator()));
//		}
//		this.artistMap.get(song.getArtist()).add(song);
//
//		addSong(this.tagMap, song);
//	}
//
//	/**
//	 * Helper method that traverses a songs list of tags and adds each tag to
//	 * the map. Maps each tag to the track id of the songs with that tag.
//	 * 
//	 * @param tagMap
//	 * @param song
//	 */
//	public synchronized void addSong(TreeMap<String, TreeSet<String>> tagMap, Song song) {
//		for (String a : song.getTagList()) {
//			if (tagMap.get(a) == null) {
//				tagMap.put(a, new TreeSet<String>());
//			}
//			tagMap.get(a).add(song.getTrackId());
//		}
//	}
//
//
//	/**
//	 * Calls a method to write the library to a text file sorted by the given
//	 * order.
//	 * 
//	 * @param output
//	 *            file to write music library to
//	 * @param order
//	 *            that the library should be sorted by
//	 */
//	public void writeToOutput(Path output, String order) {
//		try (BufferedWriter writer = Files.newBufferedWriter(output, Charset.forName("UTF-8"))) {
//			if (order.compareTo("artist") == 0) {
//				LibraryWriter.writeByArtist(writer, this.artistMap);
//			} else if (order.compareTo("title") == 0) {
//				LibraryWriter.writeByTitle(writer, this.titleMap);
//			} else if (order.compareTo("tag") == 0) {
//				LibraryWriter.writeByTag(writer, this.tagMap);
//			}
//
//		} catch (IOException e) {
//			System.err.println("there was an issue writing to " + output.getFileName());
//		}
//
//	}
//	

	
	private ReentrantLock lock;
	
	public ThreadSafeMusicLibrary(){
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
//		System.out.println("----------------addSong-------------------");
		lock.lockWrite();
//		System.out.println("writelock acquired");
//		try{
//			System.out.println("adding song: " + song.toString());
			super.addSong(song);
//		} finally {
			lock.unlockWrite();
//			System.out.println("writelock released");
//			System.out.println("----------------End-addSong-------------------");
//		}
	}
	
//	@Override
	/**
	 * Helper method that traverses a songs list of tags and adds each tag to
	 * the map. Maps each tag to the track id of the songs with that tag.
	 * 
	 * @param tagMap
	 * @param song
	 */
//	public  void addSong(TreeMap<String, TreeSet<String>> tagMap, Song song) {
//		lock.lockWrite();
//		try{
//			super.addSong(tagMap, song);
//		} finally {
//			lock.unlockWrite();
//		}
//	}

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
	public synchronized void writeToOutput(Path output, String order) {
		
//		try{
//			lock.lockRead();
			super.writeToOutput(output, order);
//		} finally {
//			lock.unlockRead();
//		}
	}
	
	
	/******************* METHODS FOR DEBUGGING ***********************/

	/**
	 * Prints the library sorted by tag to console
	 */
	public void tagMapToString() {
		lock.lockRead();
		try{
			super.tagMapToString();
		} finally {
			lock.unlockRead();
		}
	}

	/**
	 * Prints the library sorted by title to console
	 */
	public void titleMapToString() {
		lock.lockRead();
		try{
			super.titleMapToString();
		} finally {
			lock.unlockRead();
		}
	}

	/**
	 * Prints the library sorted by Artist name to console
	 **/
	public void artistMapToString() {
		lock.lockRead();
		try{
			super.artistMapToString();
		} finally {
			lock.unlockRead();
		}
	}



}
