import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

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
	
	public static final int DEFAULT_PORT = 14001;
	
	
	/**
	 * Main method to begin building the music library and create the text file
	 * 
	 * @param args
	 *            to be parsed to determine the order of the output, input file
	 *            to be read, and output file to be written to
	 * @throws EmptyFileException 
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
					if(argumentParser.hasFlag(SEARCH_INPUT)&& argumentParser.hasFlag(SEARCH_OUTPUT)){
							String inputQuery = argumentParser.getValue(SEARCH_INPUT);
							Path queryInputPath = Paths.get(inputQuery);
							String outputQuery =  argumentParser.getValue(SEARCH_OUTPUT);
							Path queryOutputPath = Paths.get(outputQuery);
							if(argumentParser.hasThreadFlag(THREADS_FLAG)){
								qBuilder.readQueries(queryInputPath, queryOutputPath, safeLibrary);
							}
							else{
								qBuilder.readQueries(queryInputPath, queryOutputPath, library);
							}						
					}

				}
				
			}
			
		}
		Server server = new Server(DEFAULT_PORT);

		//create a ServletHander to attach servlets
		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
		server.setHandler(servhandler);

		servhandler.addEventListener(new ServletContextListener() {

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				//Do nothing when server shut down.
			}

			@Override
			public void contextInitialized(ServletContextEvent sce) {

				Path path = Paths.get("grades.json");
				GradeBook book = GradeBookBuilder.buildBook(path);
				//if grades file is not valid then create an empty GradeBook.
				if(book == null) {
					book = new GradeBook();
				}
				sce.getServletContext().setAttribute("gradebook", book);
			}

		});

		//add a servlet for searching for grades
		servhandler.addServlet(GradeSearchServlet.class, "/search");

		//TODO: add a servlet for displaying grades of all students in the GradeBook
		servhandler.addServlet(GradeDisplayServlet.class, "/all");
		
		//set the list of handlers for the server
		server.setHandler(servhandler);

		server.start();
		server.join();
	}
		
		return;
	}
}
