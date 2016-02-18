import java.util.ArrayList;

import org.json.simple.JSONObject;

/**
 * A class to maintain data about a single song. Java object representation of a
 * JSON object with schema below.
 * 
 * @author srollins
 *
 */

/*
 * { "artist":"The Primitives", "timestamp":"2011-09-07 12:34:34.851502",
 * "similars":[ [ "TROBUDC128F92F7F0B", 1 ], [ "TRWSCCK128F92F7EDB",
 * 0.98714400000000002 ] ], "tags":[ [ "1980s", "100" ], [ "80s", "33" ], [
 * "pop", "33" ], [ "alternative", "33" ] ], "track_id":"TRBDCAB128F92F7EE4",
 * "title":"Never Tell" }
 * 
 */

public class Song {

	/**
	 * Declare appropriate instance variables.
	 */
	private String artist, trackId, title;
	private  ArrayList<ArrayList<String>>  similars, tags;

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
	public Song(String artist, String trackId, String title, ArrayList<ArrayList<String>>  similars, ArrayList<ArrayList<String>> tags) {
		this.artist = artist;
		this.trackId = trackId;
		this.title = title;
		this.similars = similars;
		this.tags = tags;
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
		this.similars = (ArrayList<ArrayList<String>>) object.get("similars");
		this.tags = ( ArrayList<ArrayList<String>> ) object.get("tags");
	}

	/**
	 * Return artist.
	 * 
	 * @return
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
	public  ArrayList<ArrayList<String>>  getSimilars() {
		return this.similars;
	}

	/**
	 * Return a list of all tags for this track.
	 * 
	 * @return Arraylist of tags for a track
	 */
	public  ArrayList<ArrayList<String>>  getTags() {
		return this.tags;
	}

	public boolean containsTag(String tag, ArrayList<ArrayList<String>> tags){
		for(ArrayList<String> a : tags){
			for(String t : a){
				if(t.equals(tag)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * toString method for debugging
	 */
	public String toString() {
		return "Song [TITLE: " + "'" + this.title + "'," + " ARTIST: " + "'" + this.artist + "'," + " TAGS: " + "'" + this.tags + "'," + " SIMILARS: " + "'" + this.similars + "',"  + " TRACKID: " + "'" + this.trackId + "'" + "]";
	}
}
