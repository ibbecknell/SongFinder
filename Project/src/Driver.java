import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
	
	public static final String INPUT_FLAG = "-input";
	public static final String OUTPUT_FLAG = "-output";
	public static final String ORDER_FLAG = "-order";
	
	
	public static void main(String[] args) {
		MusicLibrary library = new MusicLibrary();
		ArgumentParser argumentParser = new ArgumentParser(args);		
		
		if (argumentParser.hasFlag(INPUT_FLAG) && argumentParser.hasFlag(OUTPUT_FLAG) && argumentParser.hasFlag(ORDER_FLAG)) {
			
			if (argumentParser.hasValue(INPUT_FLAG) && argumentParser.hasValue(OUTPUT_FLAG) && argumentParser.hasValue(ORDER_FLAG)) {
				String input = argumentParser.getValue(INPUT_FLAG);
				String output = argumentParser.getValue(OUTPUT_FLAG);
				String order = argumentParser.getValue(ORDER_FLAG);
				
				Path inputPath = Paths.get(input);
				MusicLibraryBuilder.traverseDirectory(inputPath, library, true);
				
				Path outputPath = Paths.get(output);
				library.writeToOutput(outputPath, order);
				
			} else {
				return;
			}
		} else {
			return;
		}
	}

}
