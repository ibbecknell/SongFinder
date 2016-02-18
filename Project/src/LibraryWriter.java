import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

public class LibraryWriter {
	
//	public static void writeMap(String order, BufferedWriter writer) throws IOException{
//		if(order == "artist"){
//			System.out.println("ordered by artist");
//			LibraryWriter.writeByArtist(writer, this.artistMap);
//		}
//		else if (order == "title"){
//			System.out.println("ordered by title");
//			LibraryWriter.writeByTitle(writer, this.titleMap);
//		}
//		else if (order == "tag"){
//			System.out.println("ordered by tag");
//			LibraryWriter.writeByTag(writer, this.tagMap);
//		}
//	}
	
	public static void writeByArtist(BufferedWriter w, TreeMap<String, TreeSet<Song>> artistMap) throws IOException{
		for(String i : artistMap.keySet()){
			for(Song s : artistMap.get(i)){
				w.write(i + " - " + s.getTitle());
				w.newLine();
			}
		}
	}

	public static void writeByTitle(BufferedWriter w, TreeMap<String, String> titleMap) throws IOException{
		for(String i : titleMap.keySet()){
			w.write(i + " - " + titleMap.get(i));
			w.newLine();
		}
	}
	public static void writeByTag(BufferedWriter w, TreeMap<String, TreeSet<String>> tagMap) throws IOException{
		for(String i : tagMap.keySet()){
			w.write(i + " : " + tagMap.get(i));
			w.newLine();
		}
	}
}
