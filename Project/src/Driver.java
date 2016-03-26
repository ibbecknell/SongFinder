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

	/**
	 * Main method to begin building the music library and create the text file
	 * 
	 * @param args
	 *            to be parsed to determine the order of the output, input file
	 *            to be read, and output file to be written to
	 */
//	public static void main(String[] args) {
//		int numThreads = 10;
//		ThreadSafeMusicLibrary safeLibrary = new ThreadSafeMusicLibrary();
//		MusicLibrary library = new MusicLibrary();
//		ArgumentParser argumentParser = new ArgumentParser(args);
//
//		if (argumentParser.hasValidFlags(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {
//		
//			if (argumentParser.hasValidValues(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {
//				System.out.println("valid flags");
//				String input = argumentParser.getValue(INPUT_FLAG);
//				String output = argumentParser.getValue(OUTPUT_FLAG);
//				String order = argumentParser.getValue(ORDER_FLAG);
//				Path inputPath = Paths.get(input);
//	
//	//			if (argumentParser.hasValidThread(THREADS_FLAG)) {
//	//				numThreads = Integer.parseInt(argumentParser.getValue(THREADS_FLAG));
//	//				MultithreadedMusicLibraryBuilder musicLibraryBuilder = new MultithreadedMusicLibraryBuilder(numThreads);
//	//				musicLibraryBuilder.traverseDirectory(inputPath, safeLibrary);
//	//				
//	//				
//	////				musicLibraryBuilder.shutdown();
//	//			}
//	//			if (argumentParser.hasValidOrder(order, ORDER_BY_ARTIST, ORDER_BY_TITLE, ORDER_BY_TAG)) {
//	//				Path outputPath = Paths.get(output);
//	//				System.out.println(output);
//	//				safeLibrary.writeToOutput(outputPath, order);
//	//			}
//	//				else{
//					MusicLibraryBuilder.traverseDirectory(inputPath, library);
//	
//					if (argumentParser.hasValidOrder(order, ORDER_BY_ARTIST, ORDER_BY_TITLE, ORDER_BY_TAG)) {
//						Path outputPath = Paths.get(output);
//						library.writeToOutput(outputPath, order);
//					}
//	//			}
//
//			}
//		}
//		else{
//			System.out.println("invalid flags");
//		}
//		return;
//	}
//	public static void main(String[] args) {
//		int numThreads = 10;
//		ThreadSafeMusicLibrary safeLibrary = new ThreadSafeMusicLibrary();
//
//		String input = null;
//		String output = null;
//		String order = null;
//		Path inputPath = null;
//		Path outputPath = null;
//		MusicLibrary library = new MusicLibrary();
//		ArgumentParser argumentParser = new ArgumentParser(args);
//		if (argumentParser.hasValidFlags(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {
//			if (argumentParser.hasValidValues(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {
//				input = argumentParser.getValue(INPUT_FLAG);
//				output = argumentParser.getValue(OUTPUT_FLAG);
//				order = argumentParser.getValue(ORDER_FLAG);
//				inputPath = Paths.get(input);
//			}
//		}
//		
//		if (argumentParser.hasValidOrder(order,ORDER_BY_ARTIST, ORDER_BY_TITLE, ORDER_BY_TAG )) {
//			outputPath = Paths.get(output);
//		}
//		
//		if (argumentParser.hasThreadFlag(THREADS_FLAG)) {
//			if(argumentParser.hasValidThreadCount(THREADS_FLAG)){
////				
//			}
////			MultithreadedMusicLibraryBuilder musicLibraryBuilder = new MultithreadedMusicLibraryBuilder(numThreads);
////			musicLibraryBuilder.traverseDirectory(inputPath, safeLibrary);
////			safeLibrary.writeToOutput(outputPath, order);
//
//		}
//		else if(!argumentParser.hasThreadFlag(THREADS_FLAG)){
//			MusicLibraryBuilder.traverseDirectory(inputPath, library);
//			library.writeToOutput(outputPath, order);
//		}
//		return;
//	}
	public static void main(String[] args) {
		int numThreads = 10;
		ThreadSafeMusicLibrary safeLibrary = new ThreadSafeMusicLibrary();
		MusicLibrary library = new MusicLibrary();
		ArgumentParser argumentParser = new ArgumentParser(args);

		if (argumentParser.hasValidFlags(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {

			if (argumentParser.hasValidValues(INPUT_FLAG, OUTPUT_FLAG, ORDER_FLAG)) {
				String input = argumentParser.getValue(INPUT_FLAG);
				String output = argumentParser.getValue(OUTPUT_FLAG);
				String order = argumentParser.getValue(ORDER_FLAG);
				Path inputPath = Paths.get(input);
				Path outputPath = Paths.get(output);
				
				
				if (argumentParser.hasValidOrder(order,ORDER_BY_ARTIST, ORDER_BY_TITLE, ORDER_BY_TAG )) {
					if(argumentParser.hasThreadFlag(THREADS_FLAG)){
						if(argumentParser.hasValidThreadCount(THREADS_FLAG)){
							numThreads = Integer.parseInt(argumentParser.getValue(THREADS_FLAG));
						}
						MultithreadedMusicLibraryBuilder musicLibraryBuilder = new MultithreadedMusicLibraryBuilder(numThreads);
						musicLibraryBuilder.traverseDirectory(inputPath, safeLibrary);
						musicLibraryBuilder.shutdown();
						musicLibraryBuilder.awaitTermination();
						System.out.println("writing to output");
						safeLibrary.writeToOutput(outputPath, order);
					}
					else{
						MusicLibraryBuilder.traverseDirectory(inputPath, library);
						library.writeToOutput(outputPath, order);
					}
					
				}

			}
		}
		return;
	}
}
