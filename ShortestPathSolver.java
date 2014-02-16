import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class ShortestPathSolver
{
	private static double curDistance = 0.0;
	private static ArrayList<Node> solution;

	public static ArrayList<Node> solve(Graph graph)
	{
		Solutions temp1 = DumbPathSolver2.solve(graph);
		Solutions temp2 = (graph.getSize() < 40) ?AntColonySolver.solve(graph) : new Solutions(null, Double.POSITIVE_INFINITY);

		return temp1.distance < temp2.distance ? temp1.path : temp2.path;
		//return temp1.path;
	}
}

class Solutions
{
	public ArrayList<Node> path;
	public double distance;

	public Solutions(ArrayList<Node> path, double dist)
	{
		this.path = path;
		distance = dist;
	}
}

class AntColonySolver
{
	private static ArrayList<Node> _nodes;
	private static HashMap _pheromones = new HashMap();
		//format is id(node1)*10000 + id(node2)
	private static int ANTS = 100000;
	private static Random rand = new Random();

	public static Solutions solve(Graph graph)
	{
		_pheromones.clear();
		ArrayList<Node> solution = new ArrayList<Node>();
		double distance = Double.POSITIVE_INFINITY;

		for(int h = 0; h < ANTS; h++)
		{
			ArrayList<Integer> routes = new ArrayList<Integer>();
			ArrayList<Node> tempSol = new ArrayList<Node>();
			_nodes = new ArrayList<Node>(graph.getGraph());
			int k = rand.nextInt(_nodes.size());
			double tempDistance = 0;
			Node node1 = _nodes.get(k),
				 node2 = null;
				_nodes.remove(k);
				tempSol.add(node1);
			while(_nodes.size() > 0)
			{
				weightedProbabilityCalculator calc = new weightedProbabilityCalculator();
				for(int j = 0; j < _nodes.size(); j++)
				{
					node2 = _nodes.get(j);
					int hashId = calcHashId(node1.id(), node2.id());
					calc.addChoice(node2,
						(_pheromones.containsKey(hashId) ? (int)_pheromones.get(hashId) : 0)*10
						/node1.distanceToNode(node2) +
						1/node1.distanceToNode(node2));
				}
				node2 = (Node)calc.choose();
				_nodes.remove(node2);
				routes.add(calcHashId(node1.id(), node2.id()));
				tempDistance += node1.distanceToNode(node2);
				tempSol.add(node2);
				node1 = node2;
			}
			node1 = (Node)graph.getGraph().get(k);
			routes.add(calcHashId(node2.id(), node1.id()));
			tempDistance += node2.distanceToNode(node1);
			tempSol.add(node1);

			if(tempDistance < distance)
			{
				for(int i = 0; i < routes.size(); i++)
				{
					if(_pheromones.containsKey(routes.get(i)))
					{
						_pheromones.put(routes.get(i), (int)_pheromones.get(routes.get(i)) + 1);
					}
					else
					{
						_pheromones.put(routes.get(i), 1);
					}
				}
				solution = tempSol;
				distance = tempDistance;
			}
		}
		System.out.printf("Ant : %.2f\n", distance);
		return new Solutions(solution, distance);
	}

	private static int calcHashId(int id1, int id2)
	{
		return id1*10000 + id2;
	}
}

class DumbPathSolver
{
	public static Solutions solve(Graph graph)
	{
		ArrayList<Node> solution = new ArrayList<Node>();
		ArrayList<Node> nodes = new ArrayList<Node>(graph.getGraph());
		Node node1 = nodes.get(0), node2 = null;
			nodes.remove(0);
			solution.add(node1);
		Double dist;
		Double totalDistance = 0.0;

		while(nodes.size() > 0)
		{
			node2 = null;
			dist = Double.POSITIVE_INFINITY;
			for(int i = 0; i < nodes.size(); i++)
			{
				if(node1.distanceToNode(nodes.get(i)) < dist)
				{
					node2 = nodes.get(i);
					dist = node1.distanceToNode(nodes.get(i));
				}
			}
			totalDistance += dist;
			node1 = node2;
			nodes.remove(node2);
			solution.add(node2);
		}

		node1 = (Node)graph.getGraph().get(0);
		solution.add(node1);
		totalDistance += node2.distanceToNode(node1);

		System.out.printf("Dumb : %.2f\n", totalDistance);
		return new Solutions(solution, totalDistance);
	}
}

class DumbPathSolver2
{
	public static Solutions solve(Graph graph)
	{
		ArrayList<Node> solution = null;
		Double minDistance = Double.POSITIVE_INFINITY;
		for(int j = 0; j < graph.getSize(); j++)
		{
			ArrayList<Node> nodes = new ArrayList<Node>(graph.getGraph());
			ArrayList<Node> tempSolution = new ArrayList<Node>();
			Node node1 = nodes.get(j), node2 = null;
				nodes.remove(j);
				tempSolution.add(node1);
			Double dist;
			Double totalDistance = 0.0;

			while(nodes.size() > 0)
			{
				node2 = null;
				dist = Double.POSITIVE_INFINITY;
				for(int i = 0; i < nodes.size(); i++)
				{
					if(node1.distanceToNode(nodes.get(i)) < dist)
					{
						node2 = nodes.get(i);
						dist = node1.distanceToNode(nodes.get(i));
					}
				}
				totalDistance += dist;
				node1 = node2;
				nodes.remove(node2);
				tempSolution.add(node2);
			}

			node1 = (Node)graph.getGraph().get(j);
			tempSolution.add(node1);
			totalDistance += node2.distanceToNode(node1);

			if(totalDistance < minDistance)
			{
				solution = tempSolution;
				minDistance = totalDistance;
			}
		}

		System.out.printf("Dumb2 : %.2f\n", minDistance);
		return new Solutions(solution, minDistance);
	}
}

class weightedProbabilityCalculator
{
	private ArrayList<Object> _choices;
	private ArrayList<Double> _weight;
	private Double _totalWeight;

	public weightedProbabilityCalculator()
	{
		_choices = new ArrayList<Object>();
		_weight = new ArrayList<Double>();
		_totalWeight = 0.0;
	}

	public void addChoice(Object choice, Double weight)
	{
		_totalWeight += weight;
		_choices.add(choice);
		_weight.add(weight);
	}

	public Object choose()
	{
		//System.out.printf("Max weight: %.2f.\n", _totalWeight);

		Double selection = Math.random();

		for(int i = 0; i < _weight.size(); i++)
		{
			selection -= _weight.get(i)/_totalWeight;
			if(selection < 0)
			{
				//System.out.printf("\tChose %.2f weight, chance %.2f.\n", _weight.get(i), _weight.get(i)/_totalWeight);
				return _choices.get(i);
			}
		}

		return _choices.get(0);
	}
}