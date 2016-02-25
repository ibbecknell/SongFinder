import java.util.ArrayList;

import org.json.simple.JSONObject;

/**
 * A class to maintain data about a single song.
 * 
 * @author ibbecknell
 *
 */

public class Song {

	/**
	 * Declare appropriate instance variables.
	 */
	private String artist, trackId, title;
	private ArrayList<ArrayList<String>> similars;
	private ArrayList<ArrayList<Object>> tags;
	private ArrayList<String> tagList;

	/**
	 * Constructor.
	 * 
	 * @param artist-
	 *            String song artist name
	 * @param trackId-
	 *            String of the ID of the track
	 * @param title-
	 *            String song title
	 * @param similars
	 *            - ArrayList of similar songs
	 * @param tags-
	 *            ArrayList of tags of the song
	 */
	public Song(String artist, String trackId, String title, ArrayList<ArrayList<String>> similars,
			ArrayList<ArrayList<Object>> tags) {
		this.artist = artist;
		this.trackId = trackId;
		this.title = title;
		
//TODO: since you assign values in buildList you need not do this assignment here. Instead, pass the list into the buildList method.		
		this.similars = similars;
		this.tags = tags;
//TODO: you should parse the similars list in the same way you parse the tags list.		
		buildList();
	}

	/**
	 * Constructor that takes as input a JSONObject as illustrated in the
	 * example above and constructs a Song object by extract the relevant data.
	 * 
	 * @param object-
	 *            JSONObject to be extracted to a Song object
	 */
	public Song(JSONObject object) {
		this.artist = (String) object.get("artist");
		this.trackId = (String) object.get("track_id");
		this.title = (String) object.get("title");
//TODO: see comments above. The code here should be something like this.similars = buildList(object.get("similars"));		
		this.similars = (ArrayList<ArrayList<String>>) object.get("similars");
		this.tags = (ArrayList<ArrayList<Object>>) object.get("tags");
		buildList();
	}

	/**
	 * Return artist.
	 * 
	 * @return artist
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * Return track ID.
	 * 
	 * @return trackID
	 */
	public String getTrackId() {
		return this.trackId;
	}

	/**
	 * Return title.
	 * 
	 * @return song title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Return a list of the track IDs of all similar tracks.
	 * 
	 * @return ArrayList of similar songs
	 */
	public ArrayList<ArrayList<String>> getSimilars() {
		return this.similars;
	}

	/**
	 * Return a list of all tags for this track.
	 * 
	 * @return Arraylist of tags for a track
	 */
	public ArrayList<ArrayList<Object>> getTags() {
		return this.tags;
	}

	/**
	 * returns list of just tag names
	 * 
	 * @return tagList ArrayList
	 */
	public ArrayList<String> getTagList() {
		return this.tagList;
	}

	/**
	 * Builds an ArrayList of just tag names from the song data
	 */
	public void buildList() {
		tagList = new ArrayList<String>();
		for (ArrayList<Object> a : this.tags) {
			this.tagList.add((String) a.get(0));
		}

	}

	/**
	 * helper method that prints the list of tags
	 */
	public void printList() {
		for (String a : this.tagList) {
			System.out.println(a);
		}
	}

	/**
	 * toString method for debugging
	 */
	public String toString() {
		return "Song [TITLE: " + "'" + this.title + "'," + " ARTIST: " + "'" + this.artist + "'," + " TAGS: " + "'"
				+ this.tags + "'," + " SIMILARS: " + "'" + this.similars + "'," + " TRACKID: " + "'" + this.trackId
				+ "'" + ", TAGLIST: " + this.tagList + "]";
	}

}
