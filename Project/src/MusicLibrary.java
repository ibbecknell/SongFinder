import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Maintains a music library of several songs.
 *
 */
public class MusicLibrary {

	/**
	 * Data structure to map tag -> treeset of track ids with the tag
	 */
	protected final TreeMap<String, TreeSet<String>> tagMap;

	/**
	 * data structure to map song title -> TreeSet of Songs sorted by the title
	 * comparator -sorted by title
	 */
	protected final TreeMap<String, TreeSet<Song>> titleMap;

	/**
	 * data structure to map artist name -> TreeSet of Songs sorted by the
	 * artist comparator
	 * 
	 */
	protected final TreeMap<String, TreeSet<Song>> artistMap;

	/**
	 * Constructor to initialize Data Structures
	 */
	public MusicLibrary() {
		this.tagMap = new TreeMap<String, TreeSet<String>>();
		this.titleMap = new TreeMap<String, TreeSet<Song>>();
		this.artistMap = new TreeMap<String, TreeSet<Song>>();

	}

	/**
	 * Add a song to the library. Adds a reference to the song object to all
	 * appropriate data structures.
	 * 
	 * @param song
	 *            to be added to each data structure
	 * 
	 */
	public void addSong(Song song) {

		if (this.titleMap.get(song.getTitle()) == null) {
			titleMap.put(song.getTitle(), new TreeSet<Song>(new ByTitleComparator()));
		}
		this.titleMap.get(song.getTitle()).add(song);

		if (this.artistMap.get(song.getArtist()) == null) {
			this.artistMap.put(song.getArtist(), new TreeSet<Song>(new ByArtistComparator()));
		}
		this.artistMap.get(song.getArtist()).add(song);

		addSong(this.tagMap, song);
	}

	/**
	 * Helper method that traverses a songs list of tags and adds each tag to
	 * the map. Maps each tag to the track id of the songs with that tag.
	 * 
	 * @param tagMap
	 * @param song
	 */
	public void addSong(TreeMap<String, TreeSet<String>> tagMap, Song song) {
		for (String a : song.getTagList()) {
			if (tagMap.get(a) == null) {
				tagMap.put(a, new TreeSet<String>());
			}
			tagMap.get(a).add(song.getTrackId());
		}
	}


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
		try (BufferedWriter writer = Files.newBufferedWriter(output, Charset.forName("UTF-8"))) {
			if (order.compareTo("artist") == 0) {
				LibraryWriter.writeByArtist(writer, this.artistMap);
			} else if (order.compareTo("title") == 0) {
				LibraryWriter.writeByTitle(writer, this.titleMap);
			} else if (order.compareTo("tag") == 0) {
				LibraryWriter.writeByTag(writer, this.tagMap);
			}

		} catch (IOException e) {
			System.err.println("there was an issue writing to " + output.getFileName());
		}

	}
//	public void addAll(MusicLibrary other) {
//		for (String word : other.artistMap.keySet()) {
//			if (!this.artistMap.containsKey(word)) {
//				this.artistMap.put(word, other.artistMap.get(word));
//			}
//		}
//	}
	/**
	 * adds a local library to a global library
	 * @param library to be added to the global one
	 */
//	TODO complete this to add one given local library to a global one
//	public void addLibrary(MusicLibrary library){
//		for (String key : library.artistMap.keySet()){
//			if (this.artistMap.get(key) == null) {
//				this.artistMap.put(key, library.artistMap.get(key));
//			}
////			this.artistMap.get(song.getArtist()).add(song);
//		}
//		for(String key : library.tagMap.keySet()){
//			if(this.tagMap.get(key)==null){
//				this.tagMap.put(key, library.tagMap.get(key));
//			}
//		}
//		for(String key : library.titleMap.keySet()){
//			if (this.titleMap.get(key) == null) {
//				titleMap.put(key, library.titleMap.get(key));
//			}
////			this.titleMap.get(key).add(library.titleMap.get(key));
//		}
//	}
	
	/******************* METHODS FOR DEBUGGING ***********************/

	/**
	 * Prints the library sorted by tag to console
	 */
	public void tagMapToString() {
		for (String i : this.tagMap.keySet()) {
			System.out.println(i + " : " + this.tagMap.get(i).toString());
		}
	}

	/**
	 * Prints the library sorted by title to console
	 */
	public void titleMapToString() {
		for (String i : this.titleMap.keySet()) {
			for (Song s : this.titleMap.get(i)) {
				System.out.println(s.getArtist() + " - " + i);
			}
		}
	}

	/**
	 * Prints the library sorted by Artist name to console
	 **/
	public void artistMapToString() {
		for (String i : this.artistMap.keySet()) {
			for (Song s : this.artistMap.get(i)) {
				System.out.println(i + " - " + s.getTitle());
			}
		}
	}

	/**
	 * ToString method
	 */
	@Override
	public String toString() {
		return "MusicLibrary [tagMap : " + this.tagMap + "]\n" + " titleMap = " + this.titleMap + "]\n" + " artistMap = "
				+ this.artistMap + "]";
	}

	/**
	 * main driver
	 * 
	 * @param args to be debugged
	 */
	public static void main(String[] args) {
		// Song s1 = new Song("artist1", "id1", "title1", [["id", 0.324],
		// ["id2"]] , [["id", 0.324], ["id2"]]);
		// Song s2 = new Song("artist2", "id2", String title,
		// ArrayList<ArrayList<String>> similars, ArrayList<ArrayList<String>>
		// tags) {);
		// Song s3 = new Song("artist3", "id3", String title,
		// ArrayList<ArrayList<String>> similars, ArrayList<ArrayList<String>>
		// tags) {);
		// Song s4 = new Song("artist4", "id4", String title,
		// ArrayList<ArrayList<String>> similars, ArrayList<ArrayList<String>>
		// tags) {);
	}

}
