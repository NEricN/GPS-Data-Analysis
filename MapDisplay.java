import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MapCanvas extends JPanel implements MouseMotionListener
{
    private ArrayList<Graph> _graphs;
    private int _width, _height;

    private double _scaleX, _scaleY;

    private String _message;
        private int _messageX, _messageY;

    private double _minLat,
                   _maxLat,
                   _minLong,
                   _maxLong;

    public MapCanvas(int width, int height)
    {
        _width = width;
        _height = height;

        _message = "";
        _messageX = 0;
        _messageY = 0;

        addMouseMotionListener(this);

        _graphs = new ArrayList<Graph>();
    }

    public void addGraph(Graph graph)
    {
        _graphs.add(graph);

        _minLat = (_minLat < graph.getMinLat()) ? _minLat : graph.getMinLat();
        _minLong = (_minLong < graph.getMinLong()) ? _minLong : graph.getMinLong();
        _maxLat = (_maxLat > graph.getMaxLat()) ? _maxLat : graph.getMaxLat();
        _maxLong = (_maxLong > graph.getMaxLong()) ? _maxLong : graph.getMaxLong();

        _scaleX = _width/(_maxLat - _minLat);
        _scaleY = _height/(_maxLong - _minLong);
    }

    private void drawMap(Graphics g)
    {
        BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_ROUND);

        Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(bs);
            g2d.setColor(Color.blue);

        Dimension size = getSize();
        Insets insets = getInsets();

        double w = size.width - insets.left - insets.right;
        double h = size.height - insets.top - insets.bottom;

        for(int j = 0; j < _graphs.size(); j++)
        {
            ArrayList<Node> nodes = _graphs.get(j).getGraph();

            g2d.setColor(_graphs.get(j).getColor());

            for(int i = 0; i < nodes.size(); i++)
            {
                Node node = nodes.get(i);

                int x = (int)(_scaleX*(node.latitude() - _minLat));
                int y = (int)(_scaleY*(_maxLong - (node.longitude() - _minLong)));

                g2d.drawLine(x, y, x, y);
            }
        }

        g2d.drawString(_message, _messageX, _messageY);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawMap(g);
    }

    public void mouseDragged(MouseEvent e)
    {
        _message = "Here!";
        _messageX = e.getX();
        _messageY = e.getY();

        System.out.println("kek");
        repaint();
        System.out.println("Toppest kek");
    }

    public void mouseMoved(MouseEvent e)
    {
        _message = "";
        repaint();
    }
}

public class MapDisplay extends JFrame
{
    public MapDisplay(ArrayList<Graph> graphs)
    {
        setTitle("MapDisplay");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapCanvas drawer = new MapCanvas(800, 800);

        for(int i = 0; i < graphs.size(); i++)
        {
            drawer.addGraph(graphs.get(i));
        }

        add(drawer);

        setSize(800, 800);

        setLocationRelativeTo(null);
    }

    public static void main(String[] args)
    {
        Graph testGraph = new Graph();
            testGraph.addNode(0, 0);
            testGraph.addNode(800,800);
            testGraph.addNode(250, 250);
            testGraph.addNode(300, 300);
            testGraph.addNode(450, 450);
            testGraph.setColor(Color.GREEN);

        ArrayList<Graph> testGraphList = new ArrayList<Graph>();
            testGraphList.add(testGraph);

        MapDisplay md = new MapDisplay(testGraphList);
               md.setVisible(true);
    }
}