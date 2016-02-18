import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
	private final TreeMap<String, TreeSet<String>> tagMap;

	/**
	 * data structure to map song title -> TreeSet of Songs sorted by the title
	 * comparator -sorted by title
	 */
	private final TreeMap<String, TreeSet<Song>> titleMap;

	/**
	 * data structure to map artist name -> TreeSet of Songs sorted by the
	 * artist comparator
	 * 
	 */
	private final TreeMap<String, TreeSet<Song>> artistMap;

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

		if (!this.titleMap.containsKey(song.getTitle())) {
			titleMap.put(song.getTitle(), new TreeSet<Song>(new ByTitleComparator()));
		}
		titleMap.get(song.getTitle()).add(song);

		if (!this.artistMap.containsKey(song.getArtist())) {
			this.artistMap.put(song.getArtist(), new TreeSet<Song>(new ByArtistComparator()));
		}
		artistMap.get(song.getArtist()).add(song);

		addSong(tagMap, song);
	}

	/**
	 * Helper method that traverses a songs list of tags and adds each tag to
	 * the map. Maps each tag to the track id of the songs with that tag.
	 * 
	 * @param tagMap
	 * @param song
	 */
	public void addSong(TreeMap<String, TreeSet<String>> tagMap, Song song) {
		for (ArrayList<String> a : song.getTags()) {
			if (tagMap.get(a.get(0)) == null) {
				tagMap.put(a.get(0), new TreeSet<String>());
			}
			tagMap.get(a.get(0)).add(song.getTrackId());
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
	/******************* METHODS FOR DEBUGGING ***********************/

	/**
	 * Prints the library sorted by tag to console
	 */
	public void tagMapToString() {
		for (String i : tagMap.keySet()) {
			System.out.println(i + " : " + tagMap.get(i).toString());
		}
	}

	/**
	 * Prints the library sorted by title to console
	 */
	public void titleMapToString() {
		for (String i : titleMap.keySet()) {
			for (Song s : titleMap.get(i)) {
				System.out.println(s.getArtist() + " - " + i);
			}
		}
	}

	/**
	 * Prints the library sorted by Artist name to console
	 **/
	public void artistMapToString() {
		for (String i : artistMap.keySet()) {
			for (Song s : artistMap.get(i)) {
				System.out.println(i + " - " + s.getTitle());
			}
		}
	}

	/**
	 * ToString method
	 */
	@Override
	public String toString() {
		return "MusicLibrary [tagMap : " + tagMap + "]\n" + " titleMap = " + titleMap + "]\n" + " artistMap = "
				+ artistMap + "]";
	}

	/**
	 * main driver
	 * 
	 * @param args
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
