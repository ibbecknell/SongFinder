import java.nio.file.Path;
import java.nio.file.Paths;


import project1_librarybuilding.ArgumentParser;
import project1_librarybuilding.MusicLibrary;
import project1_librarybuilding.MusicLibraryBuilder;
import project2_multithreading.MultithreadedMusicLibraryBuilder;
import project2_multithreading.ThreadSafeMusicLibrary;
import project3p1_querybuilding.QueryBuilder;

/**
 * Main Driver class
 * 
 * @author ibbecknell
 *
 */
public class Driver {

	/**
	 * initialization of given flags
	 */
	public static final String INPUT_FLAG = "-input";
	public static final String OUTPUT_FLAG = "-output";
	public static final String ORDER_FLAG = "-order";
	public static final String THREADS_FLAG = "-threads";

	public static final String ORDER_BY_ARTIST = "artist";
	public static final String ORDER_BY_TITLE = "title";
	public static final String ORDER_BY_TAG = "tag";

	public static final String SEARCH_INPUT = "-searchInput";
	public static final String SEARCH_OUTPUT = "-searchOutput";


	/**
	 * Main method to begin building the music library and create the text file
	 * 
	 * @param args
	 *            to be parsed to determine the order of the output, input file
	 *            to be read, and output file to be written to
	 */
	public static void main(String[] args) {

		int numThreads = 10;
		ThreadSafeMusicLibrary safeLibrary = new ThreadSafeMusicLibrary();
		MusicLibrary library = new MusicLibrary();
		ArgumentParser argumentParser = new ArgumentParser(args);
		QueryBuilder qBuilder = new QueryBuilder();

		if (argumentParser.hasValidFlags(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {

			if (argumentParser.hasValidValues(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {
				String input = argumentParser.getValue(INPUT_FLAG);
				String output = argumentParser.getValue(OUTPUT_FLAG);
				String order = argumentParser.getValue(ORDER_FLAG);
				Path inputPath = Paths.get(input);
				Path outputPath = Paths.get(output);

				if (argumentParser.hasValidOrder(order, ORDER_BY_ARTIST, ORDER_BY_TITLE, ORDER_BY_TAG)) {
					if (argumentParser.hasThreadFlag(THREADS_FLAG)) {
						if (argumentParser.hasValidThreadCount(THREADS_FLAG)) {
							numThreads = Integer.parseInt(argumentParser.getValue(THREADS_FLAG));
						}
						MultithreadedMusicLibraryBuilder musicLibraryBuilder = new MultithreadedMusicLibraryBuilder(
								numThreads);
						musicLibraryBuilder.traverse(inputPath, safeLibrary);
						safeLibrary.writeToOutput(outputPath, order);

					} else {
						MusicLibraryBuilder.traverseDirectory(inputPath, library);
						library.writeToOutput(outputPath, order);
					}
					if (argumentParser.hasFlag(SEARCH_INPUT) && argumentParser.hasFlag(SEARCH_OUTPUT)) {
						String inputQuery = argumentParser.getValue(SEARCH_INPUT);
						Path queryInputPath = Paths.get(inputQuery);
						String outputQuery = argumentParser.getValue(SEARCH_OUTPUT);
						Path queryOutputPath = Paths.get(outputQuery);
						if (argumentParser.hasThreadFlag(THREADS_FLAG)) {
							qBuilder.readQueries(queryInputPath, queryOutputPath, safeLibrary);
						} else {
							qBuilder.readQueries(queryInputPath, queryOutputPath, library);
						}
					}

				}

			}

		}

		return;
	}
}
