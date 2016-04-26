import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	private ArrayList<String> tagList;
	private ArrayList<String> simList;

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
	public Song(String artist, String trackId, String title, ArrayList<ArrayList<Object>> similars,
			ArrayList<ArrayList<Object>> tags) {
		this.artist = artist;
		this.trackId = trackId;
		this.title = title;	
		this.tagList = buildTagList(tags);
		this.simList = buildSimList(similars);
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
		this.tagList = buildTagList((ArrayList<ArrayList<Object>>) object.get("tags"));
		this.simList = buildSimList((ArrayList<ArrayList<Object>>) object.get("similars"));
	}
	
	public Song(){
		
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
	 * returns list of just tag names
	 * 
	 * @return tagList ArrayList
	 */
	public ArrayList<String> getTagList() {
		return this.tagList;
	}
	
	public JSONObject toJSON(){
		JSONParser parser = new JSONParser();
		
		JSONObject newSong = new JSONObject();
//		try {
//			newSong = (JSONObject)parser.parse(song.toString());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			System.err.println("there was an issue parsing " + song.getTitle());
//		}
			newSong.put("artist", this.artist);
			newSong.put("trackId", this.trackId);
			newSong.put("title", this.title);
		return newSong;
	}

	public Song clone(Song song){
		Song s = new Song();

		s.artist = this.artist;
		s.title = this.title;
		s.trackId = this.trackId;
		s.simList = new ArrayList<>();
		s.tagList = new ArrayList<>();
		
		for(String sim : this.simList){
			s.simList.add(sim);
		}
		for(String tag : this.tagList){
			s.tagList.add(tag);
		}
			
		return s;
	}
	
	/**
	 * Builds an ArrayList of just similar track ids from the song data
	 * @return ArrayList of similars
	 */
	public ArrayList<String> buildSimList(ArrayList<ArrayList<Object>> similars) {
		simList = new ArrayList<String>();
		for (ArrayList<Object> a : similars) {
			this.simList.add((String) a.get(0));
		}
		return simList;
	}
	
	public ArrayList<String> getSimList(){
		return simList;
	}
	
	/**
	 * Builds an ArrayList of just tag names from the song data
	 * @return ArrayList of tags
	 */
	public ArrayList<String> buildTagList(ArrayList<ArrayList<Object>> tags) {
		tagList = new ArrayList<String>();
		for (ArrayList<Object> a : tags) {
			tagList.add((String) a.get(0));
		}
		return tagList;
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
		return "Song [TITLE: " + "'" + this.title + "'," + " ARTIST: " + "'" + this.artist + "'," + " TRACKID: " + "'" + this.trackId
				+ " TAG: " + this.tagList + "SIMILARS: " + this.simList  + "]";
	}
	
	public static void main(String[] args){
//		Song s = new Song("Fuck the Police","alhgi3245", "nwa", );
//		Song s = new Song("Rihanna", "TRADDXS12903CEDB38", "Don't Stop The Music", [["Love it", "dancepop", "r and b", "best", "handclaps", "pop dance", "i am a party girl here is my soundtrack", "leapsandloved", "moodboost"]], [["TRGXGJF128F933B4EB", "TRMYNVX128F92D3097, TRHFGRA128F92D309A"]]
//);
	}

}
