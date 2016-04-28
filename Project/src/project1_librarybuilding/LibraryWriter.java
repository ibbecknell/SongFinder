package project1_librarybuilding;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Utility class that writes a data structure to file in a specified format
 * 
 * @author missionbit
 *
 */
public class LibraryWriter {

	/**
	 * writes the music library to file sorted by artist
	 * 
	 * @param w
	 *            bufferedwriter to write to file
	 * @param artistMap
	 *            data structure to be written to file
	 * @throws IOException
	 */
	public static void writeByArtist(BufferedWriter w, TreeMap<String, TreeSet<Song>> artistMap) throws IOException {
		for (String i : artistMap.keySet()) {
			for (Song s : artistMap.get(i)) {
				w.write(i + " - " + s.getTitle());
				w.newLine();
			}
		}
	}

	/**
	 * writes the music library to file sorted by tag
	 * 
	 * @param w
	 *            bufferedwriter to write to file
	 * @param titleMap
	 *            data structure to be written to file
	 * @throws IOException
	 */
	public static void writeByTitle(BufferedWriter w, TreeMap<String, TreeSet<Song>> titleMap) throws IOException {
		for (String i : titleMap.keySet()) {
			for (Song s : titleMap.get(i)) {
				w.write(s.getArtist() + " - " + i);
				w.newLine();
			}
		}
	}

	/**
	 * writes the music library to file sorted by tag
	 * 
	 * @param w
	 *            bufferedwriter to write to file
	 * @param tagMap
	 *            data structure to be written to file
	 * @throws IOException
	 */
	public static void writeByTag(BufferedWriter w, TreeMap<String, TreeSet<String>> tagMap) throws IOException {
		for (String i : tagMap.keySet()) {
			w.write(i + ": ");
			writeTrackIds(w, tagMap.get(i));
			w.newLine();
		}
	}

	/**
	 * Writes the set of track ids in the correct format to file
	 * 
	 * @param w
	 *            bufferedwriter to write track ids to file
	 * @param trackids
	 *            to be reformatted without square brackets and trimmed of
	 *            whitespace
	 * @throws IOException
	 */
	public static void writeTrackIds(BufferedWriter w, TreeSet<String> trackids) throws IOException {
		for (String i : trackids) {
			w.write(i.replace("[", "").replace("]", "").trim() + " ");
		}
	}
}
