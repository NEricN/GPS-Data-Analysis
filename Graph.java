import java.util.ArrayList

//Graph API to create new graph, not best implementation....
//but good ehough for now
public class Graph {
	//Store nodes of graph in an arraylist
	private ArrayList<Nodes> nodes;
	
	//Number of nodes in graph
	private int size = 0;
	
	//Lagest/smallest longitude/longitudes
	private double maxLat = Double.MIN_VALUE;
	private double minLat = Double.MAX_VALUE;
	private double maxLong = Double.MIN_VALUE;
	private double minLong = Double.MAX_VALUE;
	
	//constructs new graph;
	public Graph() {
		nodes = new ArrayList<Nodes>();
	}
	
	//Add new node to the graph, updates size and max/min values
	public void addNode(double latitude, double longitude) {
		maxLat = (latitude > maxLang)? latitude: maxLat;
		minLat = (latitude < minLang)? latitude: minLat;
		maxLong = (longitude > maxLong)? longitude: maxLong;
		minLong = (longitude < minLang)? longitude: minLong;
		Node temp = new Node(lang, longitude);
		nodes.add(temp);
		size ++ ;
	}

	//Return graph
	public ArrayList getGraph() {
		return nodes;
	}
	
	//Return highest latitude
	public double getMaxLat() {
		return maxLat;
	}
	
	//Return smallest latitude
	public double getMinLat() {
		return minLat;
	}

	//Return highest longitude
	public double getMaxLong() {
		return maxLong;
	}
	
	//Return smallest longitude
	public double getMinLong() {
		return minLong;
	}
}
