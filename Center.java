import java.util.ArrayList;

//Object representing center for clustering use
public class Center {
    private double latitude;
    private double longitude;
    private ArrayList<Node> nodes;

    public Center(double longi, double lati) {
        latitude = lati;
        longitude = longi;
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void updateLongi(Double longi) {
        longitude = longi;
    }

    public void updateLat(Double lat) {
        latitude = lat;
    }
    public double getLong() {
        return longitude;
    }

    public double getLat() {
        return latitude;
    }

    public void emptyList() {
        nodes.clear();
    }
}
