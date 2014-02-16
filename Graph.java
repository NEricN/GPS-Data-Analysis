import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Color;

//Graph API to create new graph, not best implementation....
//but good ehough for now
public class Graph {
	//Store nodes of graph in an arraylist
	private ArrayList<Node> nodes;

	private ArrayList<Node> _shortestPath;
	
	//Number of nodes in graph
	private int size = 0;
	
	//Lagest/smallest longitude/longitudes
	private double maxLat = Double.MIN_VALUE;
	private double minLat = Double.MAX_VALUE;
	private double maxLong = Double.MIN_VALUE;
	private double minLong = Double.MAX_VALUE;
	private Color _color;
	
	//constructs new graph;
	public Graph() {
		nodes = new ArrayList<Node>();
	}
	
	//Add new node to the graph, updates size and max/min values
	public void addNode(double latitude, double longitude) {
		addNode(latitude, longitude, null);
	}

	public void addNode(double latitude, double longitude, BufferedImage image) {
		maxLat = (latitude > maxLat)? latitude: maxLat;
		minLat = (latitude < minLat)? latitude: minLat;
		maxLong = (longitude > maxLong)? longitude: maxLong;
		minLong = (longitude < minLong)? longitude: minLong;
		Node temp = new Node(latitude, longitude, image);
		nodes.add(temp);
		size ++;

		_shortestPath = null;
	}

	//Add new node
	public void addNode(Node n) {
        maxLat = (n.latitude() > maxLat)? n.latitude(): maxLat;
        minLat = (n.latitude() < minLat)? n.latitude(): minLat;
        maxLong = (n.longitude() > maxLong)? n.longitude(): maxLong;
        minLong = (n.longitude() < minLong)? n.longitude(): minLong;
        nodes.add(n);
        size++;
    }
        
        //Remove Node
    public void removeNode(Node n) {
      	nodes.remove(n);
      	size--;
      	_shortestPath = null;
    }

    public int getSize() {
    	return size;
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

	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}

	public ArrayList<Node> getShortestPath() {
		if(_shortestPath == null)
		{
			System.out.println("Starting");
			_shortestPath = ShortestPathSolver.solve(this);
			System.out.println("Ended");
		}
		return _shortestPath;
	}
}
