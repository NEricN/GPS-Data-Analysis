import java.util.ArrayList;
import java.util.Random;

//Methods for clustering and coordinating rescue
public class Clusters {

    //main clustering algorithm, returns a list of smaller graphs to find routes
    public static ArrayList<Graph> clustering(Graph g, int N) {
        ArrayList<Node> nodes = g.getGraph();
        //ArrayList<Center> centers = generateCenters(g, N);
        ArrayList<Center> centers = kMeanPlus(g, N);
        ArrayList<Graph> graphs = new ArrayList<Graph>();
        int iterations = 5000;
        while (iterations > 0) {
            iterations--;
            findClosestCenter(nodes, centers);
            if (iterations > 0) {
                updateCenters(centers);
            }
        }
        graphs = makeGraphs(centers);
        return graphs;
    }

    //Generate random centers to start
    private static ArrayList<Center> generateCenters(Graph g, int N) {
        ArrayList<Center> result = new ArrayList<Center>();
        ArrayList<Node> nodes = g.getGraph();
        Random r = new Random();
        while (N > 0) {
            int random = r.nextInt(g.getSize());
            System.out.println(random);
            result.add(new Center(nodes.get(random).longitude(),
                                  nodes.get(random).latitude()));
            N--;
        }
        return result;
    }

    //Make garphs for each cluster.
    private static ArrayList<Graph> makeGraphs(ArrayList<Center> c) {
        ArrayList<Graph> graphs = new ArrayList<Graph>();
        for (int i = 0; i < c.size(); i++) {
            Graph g = new Graph();
            Center cent = c.get(i);
            ArrayList<Node> nodes = cent.getNodes();
            for (int k = 0; k < nodes.size(); k++) {
                g.addNode(nodes.get(k));
            }
            graphs.add(g);
        }
        return graphs;
    }

    //Calculate the closest centers to each node
    private static void findClosestCenter(ArrayList<Node> nodes,
                                   ArrayList<Center> centers) {
        for (int n = 0; n < nodes.size(); n++ ) {
            Center centerToAdd = null;
            double minDistance = Double.MAX_VALUE;
            for (int c = 0; c < centers.size(); c++ ) {
                double dist = calcDistance(centers.get(c), nodes.get(n));
                if (dist < minDistance) {
                    centerToAdd = centers.get(c);
                    minDistance = dist;
                }		
            }
            centerToAdd.addNode(nodes.get(n)); 
        }
    }

    //Calc distance
    private static double calcDistance(Center c, Node n) {
        double x = c.getLong() - n.longitude();
        double y = c.getLat() - n.latitude();
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
    }

    //Update the centers by calculating center of associated nodes
    private static void updateCenters(ArrayList<Center> c) {
        for (int n = 0; n < c.size(); n++) {
            Center cent = c.get(n);
            ArrayList<Node> nodes = cent.getNodes();
            Double lat = 0.0;
            Double longi = 0.0;
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                lat += node.latitude();
                longi += node.longitude();
            }
            cent.updateLongi(longi / (double) nodes.size());
            cent.updateLat(lat / (double) nodes.size());
            cent.emptyList();
        }
    }
    
    //K-mean++ initilization algorithm
    private static ArrayList<Center> kMeanPlus(Graph g, int N) {
        ArrayList<Center> result = new ArrayList<Center>();
        Random r = new Random();
        ArrayList<Node> nodes = g.getGraph();
        int random = r.nextInt(g.getSize());
        Double lat, longi;
        while (result.size() != N) {
            if (result.size() == 0) {
                Node node = nodes.get(random);
                Center cent1 = new Center(node.longitude(), node.latitude());
                result.add(cent1);
            }
            HashMap<Node, Double> probDist = new HashMap<Node, Double>();
            Double totalDist = 0.0;
            for (int i = 0; i < nodes.size(); i++) {
                Double dist = calcDistance(result.get(result.size() - 1),
                                           nodes.get(i));
                probDist.put(nodes.get(i), dist);
                totalDist += dist;
            }
            longi = 0.0;
            lat = 0.0;
            for (Node n : probDist.keySet()) {
                Double val = probDist.get(n);
                if ((Math.pow(val, 2.0) / Math.pow(totalDist, 2.0))
                    > r.nextDouble()) {
                    longi = n.longitude();
                    lat = n.latitude();
                    break;
                }
            }
            if (longi == 0.0) {
                random = r.nextInt(g.getSize());
                Node node2 = nodes.get(random);
                longi = node2.longitude();
                lat = node2.latitude();
            }
            Center cent = new Center(longi, lat);
            result.add(cent);
        }
        return result;
    }
}
