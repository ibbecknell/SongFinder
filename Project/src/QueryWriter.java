import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

public class QueryWriter {
	/**
	 * Helper method to indent several times by 2 spaces each time. For example,
	 * indent(0) will return an empty string, indent(1) will return 2 spaces,
	 * and indent(2) will return 4 spaces.
	 * 
	 * @param times-
	 *            number of times for the text to be indented
	 * @return indented text
	 * @throws IOException
	 */
	public static String indent(int times) {
		return times > 0 ? String.format("%" + (times * 2) + "s", " ") : "";
	}
	
	/**
	 * Helper method to quote text for output. This requires escaping the
	 * quotation mark " as \" for use in Strings. For example:
	 * 
	 * @param text
	 *            input to surround with quotation marks
	 * @return quoted text
	 */
	public static String quote(String text) {
		return "\"" + text + "\"";
	}
	
	/**
	 * writes entire partial search
	 * @param outputPath
	 * 			path to output partial search to
	 * @throws IOException
	 */
	public static void writeResults(Path outputPath, ArrayList<Song> similarSongs) throws IOException {

		try (BufferedWriter bw = Files.newBufferedWriter(outputPath,
				Charset.forName("UTF-8"))) {

			

			if (!similarSongs.isEmpty()) {
				
				for(Song s : similarSongs){
					bw.write("{");
//					bw.newLine();
					bw.write( indent(1) + quote("artist") + ":" + quote(s.getArtist()));
//					bw.newLine();
					bw.write( indent(1) + quote("trackId") + ":" + quote(s.getTrackId()));
//					bw.newLine();
					bw.write( indent(1) + quote("title") + ":" + quote(s.getTitle()));
//					bw.newLine();
					bw.write("}");
//					bw.newLine();
				}
			}
			

		} catch (IOException e) {
			System.err.println("Problem outputting partial search to " + outputPath);
		}
	}
}
