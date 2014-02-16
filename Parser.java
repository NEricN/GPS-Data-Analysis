import java.io.File;
import java.util.Scanner;

public class Parser {

	public static void parseFile (String filename, Graph myGraph) {
		File file = new File(filename);
		double lat, longi;
		String pic;
		Scanner inputStream = new Scanner(file);
		inputStream.useDelimiter(",");
		while (inputStream.hasNext()) {
			lat = inputStream.next();
			longi = inputStream.next();
			pic = inputStream.next();
			Node myNode = new Node(lat, longi, pic);
			myGraph.addNode(myNode);
		}
	}
}
