package project3p2_webInterface;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import project1_librarybuilding.MusicLibraryBuilder;
import project2_multithreading.ThreadSafeMusicLibrary;
import project4_database.LoginServlet;
import project4_database.*;

public class SongFinderServer {
	public static final int DEFAULT_PORT = 14001;

	public static void main(String[] args) throws Exception { // <- not
																// recommended
																// to throw
																// Exception in
																// general, but
																// hard to avoid
																// in this case

		Server server = new Server(DEFAULT_PORT);

		// create a ServletHander to attach servlets
		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(servhandler);

		servhandler.addEventListener(new ServletContextListener() {

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				// Do nothing when server shut down.
			}

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				MusicLibraryBuilder builder = new MusicLibraryBuilder();
				ThreadSafeMusicLibrary library = new ThreadSafeMusicLibrary();
				Path path = Paths.get("input/lastfm_subset");
				library = builder.buildLibrary(path, library);
				if (library == null) {
					library = new ThreadSafeMusicLibrary();
				}
				sce.getServletContext().setAttribute("musiclibrary", library);
			}

		});

		// add a servlet for searching for Songs
		servhandler.addServlet(SearchServlet.class, "/search");

		// TODO: add a servlet for displaying songs
		servhandler.addServlet(SongsServlet.class, "/songs");

		//add a servlet to login
		servhandler.addServlet(LoginServlet.class, "/");
		
		servhandler.addServlet(VerifyUserServlet.class, "/verifyuser");
		
		servhandler.addServlet(RegisterServlet.class, "/register");
		
		servhandler.addServlet(LogoutServlet.class, "/logout");
		
		servhandler.addServlet(UserFavServlet.class, "/user_favorites");
		
		// set the list of handlers for the server
		server.setHandler(servhandler);

		server.start();
		server.join();
	}
}
