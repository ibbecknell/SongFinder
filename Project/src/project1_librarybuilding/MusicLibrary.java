package project1_librarybuilding;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import comparators.ByArtistComparator;
import comparators.ByIdComparator;
import comparators.ByTitleComparator;
import project3p1_querybuilding.QueryWriter;

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

	private JSONArray jsonArtistResults;

	private JSONArray jsonTagResults;

	private JSONArray jsonTitleResults;

	private JSONObject jsonResults;

	/**
	 * Constructor to initialize Data Structures
	 */
	public MusicLibrary() {
		this.tagMap = new TreeMap<String, TreeSet<String>>();
		this.titleMap = new TreeMap<String, TreeSet<Song>>();
		this.artistMap = new TreeMap<String, TreeSet<Song>>();
		this.idMap = new TreeMap<String, Song>();
		this.jsonArtistResults = new JSONArray();
		this.jsonTagResults = new JSONArray();
		this.jsonTitleResults = new JSONArray();

	}

	private TreeMap<String, Song> cloneIdMap() {
		TreeMap<String, Song> toReturn = new TreeMap<String, Song>();
		for (String s : this.idMap.keySet()) {
			toReturn.put(s, this.idMap.get(s).clone(this.idMap.get(s)));
		}
		return toReturn;
	}

	private JSONArray getSimilarTitleSongs(TreeSet<String> resultList) {
		TreeMap<String, Song> idMapClone = cloneIdMap();
		JSONArray similarTitleSongs = new JSONArray();
		for (String s : resultList) {
			if (idMapClone.get(s).getTrackId() != null) {
				JSONObject obj = (JSONObject) idMapClone.get(s).toJSON();
				similarTitleSongs.add(obj);
			}
		}
		return similarTitleSongs;
	}

	private JSONArray getSimilarArtistSongs(TreeSet<String> resultList) {
		TreeMap<String, Song> idMapClone = cloneIdMap();
		JSONArray similarArtistSongs = new JSONArray();
		for (String s : resultList) {
			if (idMapClone.get(s).getTrackId() != null) {
				JSONObject obj = (JSONObject) idMapClone.get(s).toJSON();
				similarArtistSongs.add(obj);
			}

		}
		return similarArtistSongs;
	}

	private JSONArray searchByArtist(String artist) {
		TreeMap<String, Song> idMapClone = cloneIdMap();
		TreeSet<String> resultList = new TreeSet<String>();
		if (getSongsByArtist(artist) != null) {
			TreeSet<Song> songs = getSongsByArtist(artist);
			for (Song s : songs) {
				ArrayList<String> similarList = s.getSimList();
				for (String similarSong : similarList) {
					if (idMapClone.containsKey(similarSong)) {
						resultList.add(similarSong);
					}
				}
			}
		}
		JSONArray similarSongs = getSimilarArtistSongs(resultList);
		return similarSongs;
	}
	
	private JSONArray searchByTitle(String title) {
		TreeMap<String, Song> idMapClone = cloneIdMap();
		TreeSet<String> resultList = new TreeSet<String>();
		if (getSongsByTitle(title) != null) {
			TreeSet<Song> songs = getSongsByTitle(title);
			for (Song s : songs) {
				ArrayList<String> similarList = s.getSimList();
				for (String similarSong : similarList) {
					if (idMapClone.containsKey(similarSong)) {
						resultList.add(similarSong);
					}
				}
			}
		}

		JSONArray similarSongs = getSimilarTitleSongs(resultList);
		return similarSongs;
	}
	
	private JSONArray searchByTag(String tag) {
		TreeMap<String, Song> idMapClone = cloneIdMap();
		JSONArray similarTagSongs = new JSONArray();
		for (String s : getSongsByTag(tag)) {
			JSONObject obj = idMapClone.get(s).toJSON();
			similarTagSongs.add(obj);
		}
		return similarTagSongs;
	}

	private TreeSet<String> getSongsByTag(String tag) {
		TreeSet<String> toReturn = new TreeSet<String>();
		TreeSet<String> current = tagMap.get(tag);
		for (String s : current) {
			toReturn.add(s);
		}
		return toReturn;
	}

	/**
	 * Return a sorted set of all songs by a given artist.
	 * 
	 * @param artist
	 *            whose songs are being returned
	 * @return TreeSet of songs from given input
	 */
	private TreeSet<Song> getSongsByArtist(String artist) {
		TreeSet<Song> toReturn = new TreeSet<Song>(new ByIdComparator());
		TreeSet<Song> current = artistMap.get(artist);
		if (current != null) {
			for (Song s : current) {
				toReturn.add(s.clone(s));
			}
		}

		return toReturn;
	}

	

	private TreeSet<Song> getSongsByTitle(String title) {
		TreeSet<Song> toReturn = new TreeSet<Song>(new ByTitleComparator());
		TreeSet<Song> current = titleMap.get(title);
		for (Song s : current) {
			toReturn.add(s.clone(s));
		}

		return toReturn;

	}

	public JSONObject getJSONSearchByTag(String tag) {
		JSONObject obj = new JSONObject();
		obj.put("similars", searchByTag(tag));
		obj.put("tag", tag);
		jsonTagResults.add(obj);
		return obj;
	}

	public JSONObject getJSONSearchByArtist(String artist) {
		JSONObject obj = new JSONObject();
		obj.put("artist", artist);
		obj.put("similars", searchByArtist(artist));
		jsonArtistResults.add(obj);
		return obj;
	}

	public JSONObject getJSONSearchByTitle(String title) {
		JSONObject obj = new JSONObject();
		obj.put("similars", searchByTitle(title));
		obj.put("title", title);
		jsonTitleResults.add(obj);
		return obj;
	}

	public JSONObject getJSONResults(boolean artist, boolean title, boolean tag) {
		jsonResults = new JSONObject();
		if (artist) {
			jsonResults.put("searchByArtist", jsonArtistResults);
		}
		if (title) {
			jsonResults.put("searchByTitle", jsonTitleResults);
		}
		if (tag) {
			jsonResults.put("searchByTag", jsonTagResults);
		}
		return jsonResults;
	}


	public void writeToJSON(Path outputPath) {
		QueryWriter writer = new QueryWriter();
		writer.writeQueries(outputPath, jsonResults);
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

		if (this.idMap.get(song.getTrackId()) == null) {
			idMap.put(song.getTrackId(), song);
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

	public void idMapToString() {
		for (String i : this.idMap.keySet()) {
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
