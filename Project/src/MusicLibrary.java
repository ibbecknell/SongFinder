import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Maintains a music library of several songs.
 * 
 * @author srollins
 *
 */
public class MusicLibrary {

	/**
	 * Data structure to map tag -> track id of all songs with same tag
	 */
	private final TreeMap<String, TreeSet<String>> tagMap;
	
	/**
	 * data structure to map song title -> artist name
	 * -sorted by title
	 */
	private final TreeMap<String, String> titleMap;
	
	/**
	 * data structure to map song -> artist name title
	 * 
	 */
	private final TreeMap<String, TreeSet<Song>> artistMap;

	/**
	 * Constructor initialize Data Structures
	 */
	public MusicLibrary() {
		this.tagMap = new TreeMap<String, TreeSet<String>>();
		this.titleMap = new TreeMap<String, String>();
		this.artistMap = new TreeMap<String, TreeSet<Song>>();

	}

	/**
	 * Add a song to the library. Make sure to add a reference to the song
	 * object to all appropriate data structures.
	 * 
	 * @param song
	 *            to be added to library
	 * 
	 */
	public void addSong(Song song) {

		titleMap.put(song.getTitle(), song.getArtist());
		
		if (!this.artistMap.containsKey(song.getArtist())) {
			this.artistMap.put(song.getArtist(), new TreeSet<Song>(new ByArtistComparator()));
		}
		artistMap.get(song.getArtist()).add(song);
		
//		if(this.tagMap!=null){
			addSong(tagMap, song);
//		}
		
//		if(!this.tagMap.containsKey()){
//			this.tagMap.put(key, new ArrayList<String>());
//		}
//		this.tagMap.get(key).add(song.getTrackId());

		

//		this.artistMap.get(song.getArtist()).add(song.);
	}
	
	public void addSong(TreeMap<String, TreeSet<String>> tagMap, Song song){
		for(ArrayList<String> a : song.getTags()){
			if(tagMap.get(a.get(0)) == null){
				tagMap.put(a.get(0), new TreeSet<String>());
			}
//			else if(tagMap.containsKey(a.get(0))){
				tagMap.get(a.get(0)).add(song.getTrackId());
//					return;
//			} 
		}
		
	}
	
	
	public void writeToOutput(Path output, String order){
		try (BufferedWriter writer = Files.newBufferedWriter(output)) {
//			System.out.println("writing to : " + output.getFileName() + " ordered by " + order);
//			System.out.println(order.compareTo( "artist") == 0);
			if(order.compareTo( "artist") == 0){
//				System.out.println("ordered by artist");
				LibraryWriter.writeByArtist(writer, this.artistMap);
			}
			else if (order.compareTo( "title") == 0){
//				System.out.println("ordered by title");
				LibraryWriter.writeByTitle(writer, this.titleMap);
			}
			else if (order.compareTo( "tag") == 0){
//				System.out.println("ordered by tag");
				LibraryWriter.writeByTag(writer, this.tagMap);
			}
//			LibraryWriter.writeMap(order, writer);
//			System.out.println("writing to : " + output.getFileName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void tagMapToString(){
		for(String i : tagMap.keySet()){
			System.out.println(i + " : " + tagMap.get(i).toString());
		}
	}
	
	public void titleMapToString(){
		for(String i : titleMap.keySet()){
			System.out.println(i + " - " + titleMap.get(i));
		}
	}
	
	public void artistMapToString(){
		for(String i : artistMap.keySet()){
			for(Song s : artistMap.get(i)){
				System.out.println(i + " - " + s.getTitle());
			}
		}
	}
	
	@Override
	public String toString() {
		return "MusicLibrary [tagMap : " + tagMap + "]\n"+" titleMap = " + titleMap + "]\n"+" artistMap = " + artistMap + "]";
	}
	
	/**
	 * Return the song associated with a unique tag.
	 * 
	 * @param tag
	 *            to be searched for in the songMap
	 * @return Song with specified trackId
	 */
//	public ArrayList<> getSongsByTag(String tag) {
//		if (tagMap.get(tag) == null) {
//			return null;
//		}
//		return tagMap.get(tag);
//	}
	
	

	/**
	 * Return the song associated with a unique title.
	 * 
	 * @param title
	 *            to be searched for in the songMap
	 * @return Song with specified title
//	 */
//	public Song getSongByTitle(String title) {
//		if (titleMap.get(title) == null) {
//			return null;
//		}
//		return titleMap.get(title);
//	}
//
//	
//	/**
//	 * Return a sorted set of all songs by a given artist.
//	 * 
//	 * @param artist
//	 *            whose songs are being returned
//	 * @return TreeSet of songs from given input
//	 */
//	public TreeSet<Song> getSongsByArtist(String artist) {
//		return this.artistMap.get(artist);
//	}
	
	public static void main (String[] args){
//		Song s1 = new Song("artist1", "id1", "title1", [["id", 0.324], ["id2"]] , [["id", 0.324], ["id2"]]);
//		Song s2 = new Song("artist2", "id2", String title, ArrayList<ArrayList<String>>  similars, ArrayList<ArrayList<String>> tags) {);
//		Song s3 = new Song("artist3", "id3", String title, ArrayList<ArrayList<String>>  similars, ArrayList<ArrayList<String>> tags) {);
//		Song s4 = new Song("artist4", "id4", String title, ArrayList<ArrayList<String>>  similars, ArrayList<ArrayList<String>> tags) {);
	}

}
