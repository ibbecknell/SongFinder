import java.io.IOException;
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
	 * @throws EmptyFileException 
	 */
	public static void main(String[] args) throws EmptyFileException {
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
					if(argumentParser.hasFlag(SEARCH_INPUT)&& argumentParser.hasFlag(SEARCH_OUTPUT)){
						String inputQuery = argumentParser.getValue(SEARCH_INPUT);
//						System.out.println(inputQuery);
						Path queryInputPath = Paths.get(inputQuery);
						System.out.println(queryInputPath);
						String outputQuery =  argumentParser.getValue(SEARCH_OUTPUT);
						Path queryOutputPath = Paths.get(outputQuery);
						System.out.println(queryOutputPath);
//						TODO when you combine final methods to read queries, search and print output, move the empty file exception thrown in main 
						qBuilder.readQueries(queryInputPath, library);
//						library.artistMapToString();
//						System.out.println(library.artistMap.containsKey("Busta Rhymes"));
						
//						library.searchByTitle("Que Vuelvas");
//						library.getSimilarSongs();
//						library.titleMapToString();
						
//						library.searchByTag("beyonce");
						
//						library.searchByArtist("Selena");
//						library.getSimilarSongs();
//						library.writeToJSON(queryOutputPath);
//						System.out.println("artistMapCount: " + library.MapCount);
						
					}

				}
				
			}
			
		}
		
		return;
	}
}
