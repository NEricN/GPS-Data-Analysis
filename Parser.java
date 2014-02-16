import java.io.File;
import java.util.Scanner;

public class Parser {

	public Parser(String fileName) {
		File file = new File(filename);
	}

	public parseFile (File file) {
		double lat, longi;
		String pic;
		Scanner inputStream = new Scanner(file);
		inputStream.useDelimiter(",");
		while (inputStream.hasNext()) {
			lat = inputStream.next();
			longi = inputStream.next();
			pic = inputStream.next();
			Node myNode = new Node(lat, longi, pic);
		}
	}
}
