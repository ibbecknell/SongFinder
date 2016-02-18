import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
	
	public static final String INPUT_FLAG = "-input";
	public static final String OUTPUT_FLAG = "-output";
	public static final String ORDER_FLAG = "-order";
	
	public static final String ARTIST = "artist";
	public static final String TITLE = "title";
	public static final String TAG = "tag";
	
	public static void main(String[] args) {
		MusicLibrary library = new MusicLibrary();
		ArgumentParser argumentParser = new ArgumentParser(args);		
		
		if (argumentParser.hasFlag(INPUT_FLAG) && argumentParser.hasFlag(OUTPUT_FLAG) && argumentParser.hasFlag(ORDER_FLAG)) {
			if (argumentParser.hasValue(INPUT_FLAG) && argumentParser.hasValue(OUTPUT_FLAG) && argumentParser.hasValue(ORDER_FLAG)) {
				String input = argumentParser.getValue(INPUT_FLAG);
				String output = argumentParser.getValue(OUTPUT_FLAG);
				String order = argumentParser.getValue(ORDER_FLAG);
//				System.out.println("input: " + input);
//				System.out.println("output: " + output);
//				System.out.println("order: " + order);
				
				Path inputPath = Paths.get(input);
//				System.out.println("input:" + inputPath);
				MusicLibraryBuilder.traverseDirectory(inputPath, library, true);
				
//				System.out.println("artistMap");
//				library.artistMapToString();
				Path outputPath = Paths.get(output);
//				System.out.println("output: " + outputPath);
				library.writeToOutput(outputPath, order);
//				System.out.println(library.toString());

				

				
			} else {
				return;
			}
		} else {
			return;
		}
	}

}
