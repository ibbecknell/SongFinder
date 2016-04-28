package project3p1_querybuilding;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.json.simple.JSONObject;

public class QueryWriter {

	public void writeQueries(Path outputPath, JSONObject jsonResults) {
		try (BufferedWriter bw = Files.newBufferedWriter(outputPath, Charset.forName("UTF-8"))) {
			bw.write(jsonResults.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
