import java.io.File;
import java.util.Scanner;
import java.awt.image.BufferedImage;

import java.io.FileNotFoundException;

public class Parser {

	private Graph myGraph;

	public static void parseFile (String filename, Graph myGraph) throws FileNotFoundException {
		File file = new File(filename);
		double lat, longi;
		BufferedImage pic;
		Scanner inputStream = new Scanner(file);
		inputStream.useDelimiter(",");
		try {
		while (inputStream.hasNext()) {
			lat = Double.parseDouble(inputStream.next());
			longi = Double.parseDouble(inputStream.next());
			pic = ImageDecode.decodeImage(inputStream.next());
			Node myNode = new Node(lat, longi, pic);
			myGraph.addNode(myNode);
		} } catch (Exception e)
		{

		}
	}


}
