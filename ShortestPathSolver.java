public class ShortestPathSolver
{
	public static ArrayList<Node> solve(Graph graph)
	{
		return AntColonySolver.solve(graph);
	}
}

class AntColonySolver
{
	private static ArrayList<Node> _nodes;
	private static ArrayList<int> _pheromones = new ArrayList<int>();
	private static ANTS = 300;

	public static ArrayList<Node> solve(Graph graph)
	{
		_pheromones.clear();
		_nodes = graph.getGraph();

		double distance = Double.POSITIVE_INFINITY;

		for(int i = 0; i < 300; i++)
		{

		}
	}
}