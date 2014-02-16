import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MapCanvas extends JPanel
{
    private Graph _graph;
    private int _width, _height;

    private double _scaleX, _scaleY;

    public MapCanvas(Graph graph, int width, int height)
    {
        _graph = graph;
        _width = width;
        _height = height;

        _scaleX = width/(_graph.getMaxLat() - _graph.getMinLat());
        _scaleY = height/(_graph.getMaxLong() - _graph.getMinLong());
    }

    private void drawMap(Graphics g)
    {
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_ROUND);

        Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(bs);
            g2d.setColor(Color.blue);

        ArrayList<Node> nodes = _graph.getGraph();

        for(int i = 0; i < nodes.size(); i++)
        {
            Node node = nodes.get(i);

            int x = (int)(_scaleX*(node.latitude() - _graph.getMinLat()));
            int y = (int)(_scaleY*(_graph.getMaxLong() - (node.longitude() - _graph.getMinLong())));

            g2d.drawLine(x, y, x, y);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawMap(g);
    }
}

public class MapDisplay extends JFrame
{
    public MapDisplay(Graph graph)
    {
        setTitle("MapDisplay");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new MapCanvas(graph, 800, 800));

        setSize(800, 800);

        setLocationRelativeTo(null);
    }

    public static void main(String[] args)
    {
        Graph testGraph = new Graph();
            testGraph.addNode(0, 0);
            testGraph.addNode(800,800);
            testGraph.addNode(250, 250);

        MapDisplay md = new MapDisplay(testGraph);
               md.setVisible(true);
    }
}