import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	protected final TreeMap<String, Song> idMap;
	
//	protected ArrayList<String> resultList;
	
	protected ArrayList<Song> similarSongs;

	/**
	 * Constructor to initialize Data Structures
	 */
	public MusicLibrary() {
		this.tagMap = new TreeMap<String, TreeSet<String>>();
		this.titleMap = new TreeMap<String, TreeSet<Song>>();
		this.artistMap = new TreeMap<String, TreeSet<Song>>();
		this.idMap = new TreeMap<String,Song>();

	}
	
	public Song getSongByTrackId(String trackId){
		if (idMap.get(trackId) == null) {
			return null;
		}
		return idMap.get(trackId);
	}
	
//	public void addToSimilarSongs(TreeSet<TreeSet<Song>> similarSongs, Song song){
//		if(similarSongs.get(song.getTrackId()) == null){
//			idMap.put(song.getTrackId(), new TreeSet<Song>(new ByIdComparator()));
//		}
//		this.idMap.get(song.getTrackId()).add(song);
//	}
	
	public ArrayList<Song> getSimilarSongs(ArrayList<String> resultList){
		similarSongs = new ArrayList<Song>();
		for(String s : resultList){
				similarSongs.add(idMap.get(s));
//				System.out.println(idMap.get(s));
		}
		System.out.println(similarSongs.toString());
//		System.out.println("size of similarList: " + similarSongs.size());
		return similarSongs;
	}
	
	
	public ArrayList<Song> searchByArtist(String artist){
		ArrayList<String>resultList = new ArrayList<String>();
		TreeSet<Song> songs = getSongsByArtist(artist);
		for(Song s : songs){
			ArrayList<String> similarList = s.getSimList();
			for(String similarSong : similarList){
				if(this.idMap.containsKey(similarSong)){
					resultList.add(similarSong);
				}
			}
		}
		System.out.print("songs similar to " + artist + ": ");
		ArrayList<Song> similarSongs = getSimilarSongs(resultList);
		return similarSongs;
	}
	
	/**
	 * Return a sorted set of all songs by a given artist.
	 * 
	 * @param artist
	 *            whose songs are being returned
	 * @return TreeSet of songs from given input
	 */
	public TreeSet<Song> getSongsByArtist(String artist) {
		return this.artistMap.get(artist);
	}
	
	public ArrayList<Song> searchByTitle(String title){
		ArrayList<String> resultList = new ArrayList<String>();
		TreeSet<Song> songs = getSongsByTitle(title);
//		System.out.println(songs);
		for(Song s : songs){
////			System.out.print(s.getTitle()+", ");
////			System.out.println("songs similar to " + s +": [");
			ArrayList<String> similarList = s.getSimList();
//			System.out.println(similarList);
			for(String similarSong : similarList){
//				System.out.print(similarSong);
				if(this.idMap.containsKey(similarSong)){
//					System.out.println(similarSong);
					resultList.add(similarSong);
				}
			}	
		}
//		System.out.println(resultList);
		System.out.print("songs similar to "+ title + ": ");
		ArrayList<Song> similarSongs = getSimilarSongs(resultList);
		return similarSongs;
	}
	
	public TreeSet<Song> getSongsByTitle(String title){
		return titleMap.get(title);
		
	}
	
	public ArrayList<Song> searchByTag(String tag){
		similarSongs = new ArrayList<Song>();
		for(String s : getSongsByTag(tag)){
				similarSongs.add(idMap.get(s));
//				System.out.println(idMap.get(s));
		}
		System.out.println("songs similar to " + tag +": ");
		System.out.println(similarSongs.toString());
//		System.out.println("size of similarList: " + similarSongs.size());
		return similarSongs;
		
	}
	
	public TreeSet<String> getSongsByTag(String tag){
		return tagMap.get(tag);
	}
	
	
	public void writeToJSON(Path outputPath){
		try {
			QueryWriter.writeResults(outputPath, similarSongs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
//		this.MapCount++;
		
		if(this.idMap.get(song.getTrackId()) == null){
			idMap.put(song.getTrackId(), song);
//			this.MapCount++;
		}
		addSong(this.tagMap, song);
	}

	/**
	 * Helper method that traverses a songs list of tags and adds each tag to
	 * the map. Maps each tag to the track id of the songs with that tag.
	 * 
	 * @param tagMap
	 * @param song
	 */
	private void addSong(TreeMap<String, TreeSet<String>> tagMap, Song song) {
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

	/******************* METHODS FOR DEBUGGING ***********************/

	/**
	 * Prints the library sorted by tag to console
	 */
	public void tagMapToString() {
		for (String i : this.tagMap.keySet()) {
			System.out.println(i + " : " + this.tagMap.get(i).toString());
		}
	}

	public void idMapToString(){
		for(String i : this.idMap.keySet()){
			System.out.println(i + " : " + this.idMap.get(i).toString());
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
		return "MusicLibrary [tagMap : " + this.tagMap + "]\n" + " titleMap = " + this.titleMap + "]\n"
				+ " artistMap = " + this.artistMap + "]";
	}

	/**
	 * main driver
	 * 
	 * @param args
	 *            to be debugged
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
