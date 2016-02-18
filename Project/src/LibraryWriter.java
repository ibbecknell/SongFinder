import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

public class LibraryWriter {
	
	public static void writeByArtist(BufferedWriter w, TreeMap<String, TreeSet<Song>> artistMap) throws IOException{
		for(String i : artistMap.keySet()){
			for(Song s : artistMap.get(i)){
				w.write(i + " - " + s.getTitle());
				w.newLine();
			}
		}
	}

	public static void writeByTitle(BufferedWriter w, TreeMap<String, TreeSet<Song>> titleMap) throws IOException{
		for(String i : titleMap.keySet()){
			for(Song s : titleMap.get(i)){
				w.write(s.getArtist() + " - " + i);
				w.newLine();
			}
		}
	}
	
	public static void writeByTag(BufferedWriter w, TreeMap<String, TreeSet<String>> tagMap) throws IOException{
		for(String i : tagMap.keySet()){
			w.write(i + ": " );
			writeTrackIds(w, tagMap.get(i));
//			w.write(i + ": " + tagMap.get(i));
			w.newLine();
		}
	}
	
	public static void writeTrackIds(BufferedWriter w, TreeSet<String> trackids) throws IOException{
		for(String i : trackids){
			w.write(i.replace("[","").replace("]", "").trim()+" ");
		}
	}
}
