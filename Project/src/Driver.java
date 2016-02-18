import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main Driver class
 * 
 * @author ibbecknell
 *
 */
public class Driver {

	/**
	 * instantiation/initialization of given flags
	 */
	public static final String INPUT_FLAG = "-input";
	public static final String OUTPUT_FLAG = "-output";
	public static final String ORDER_FLAG = "-order";

	/**
	 * Main method to begin building the music library and create the text file
	 * 
	 * @param args
	 *            to be parsed to determine the order of the output, input file
	 *            to be read, and output file to be written to
	 */
	public static void main(String[] args) {
		MusicLibrary library = new MusicLibrary();
		ArgumentParser argumentParser = new ArgumentParser(args);

		if (argumentParser.hasFlag(INPUT_FLAG) && argumentParser.hasFlag(OUTPUT_FLAG)
				&& argumentParser.hasFlag(ORDER_FLAG)) {

			if (argumentParser.hasValue(INPUT_FLAG) && argumentParser.hasValue(OUTPUT_FLAG)
					&& argumentParser.hasValue(ORDER_FLAG)) {
				String input = argumentParser.getValue(INPUT_FLAG);
				String output = argumentParser.getValue(OUTPUT_FLAG);
				String order = argumentParser.getValue(ORDER_FLAG);

				Path inputPath = Paths.get(input);
				MusicLibraryBuilder.traverseDirectory(inputPath, library);

				Path outputPath = Paths.get(output);
				library.writeToOutput(outputPath, order);

			}
		}
		return;
	}

}
