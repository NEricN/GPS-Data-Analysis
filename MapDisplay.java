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
import java.awt.image.BufferedImage;
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

    private BufferedImage _image;
        private int _imageX, _imageY;

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

        if(_image != null)
        {
            g2d.drawImage(_image, _imageX, _imageY, null);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawMap(g);
    }

    public void mouseDragged(MouseEvent e)
    {
        removeClosestToMouseRange(e.getX(), e.getY(), 50.0);
        _image = null;
        _message = "";
        repaint();
    }

    public void mouseMoved(MouseEvent e)
    {
        Node temp = closestToMouseRange(e.getX(), e.getY(), 50.0);
        if(temp != null)
        {
            //System.out.printf("%.2f, %.2f\n", temp.latitude(), temp.longitude());
            _message = "This node!";
            _messageX = (int)(_scaleX*(temp.latitude() - _minLat));
            _messageY = (int)(_scaleY*(_maxLong - (temp.longitude() - _minLong)));

            _image = temp.picture();
            _imageX = _messageX - (int)(_image.getWidth()/2);
            _imageY = _messageY - (int)(_image.getHeight());
        }
        else
        {
            _message = "";
            _image = null;
        }
        repaint();
    }

    private Node closestToMouseRange(int x, int y, double range)
    {
        Double minDistance = 9999999.0;
        Node closestNode = null;
        for(int i = 0; i < _graphs.size(); i++)
        {
            ArrayList<Node> nodes = _graphs.get(i).getGraph();
            for(int j = 0; j < nodes.size(); j++)
            {
                Node node = nodes.get(j);
                int nX = (int)(_scaleX*(node.latitude() - _minLat));
                int nY = (int)(_scaleY*(_maxLong - (node.longitude() - _minLong)));

                Double tempDistance = Math.sqrt((x - nX)*(x - nX) + (y - nY)*(y - nY));
                if(tempDistance < minDistance)
                {
                    minDistance = tempDistance;
                    closestNode = node;
                }
            }
        }
        return minDistance <= range ? closestNode : null;
    }

    private void removeClosestToMouseRange(int x, int y, double range)
    {
        Double minDistance = 9999999.0;
        Node closestNode = null;
        Graph graph = null;
        for(int i = 0; i < _graphs.size(); i++)
        {
            ArrayList<Node> nodes = _graphs.get(i).getGraph();
            for(int j = 0; j < nodes.size(); j++)
            {
                Node node = nodes.get(j);
                int nX = (int)(_scaleX*(node.latitude() - _minLat));
                int nY = (int)(_scaleY*(_maxLong - (node.longitude() - _minLong)));

                Double tempDistance = Math.sqrt((x - nX)*(x - nX) + (y - nY)*(y - nY));
                if(tempDistance < minDistance)
                {
                    minDistance = tempDistance;
                    closestNode = node;
                    graph = _graphs.get(i);
                }
            }
        }
        if(minDistance <= range)
        {
            graph.removeNode(closestNode);
        }
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
        BufferedImage testImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        Graph testGraph = new Graph();
            testGraph.addNode(0, 0);
            testGraph.addNode(800,800);
            testGraph.addNode(250, 250, testImage);
            testGraph.addNode(300, 300, testImage);
            testGraph.addNode(450, 450, testImage);
            testGraph.setColor(Color.GREEN);

        ArrayList<Graph> testGraphList = new ArrayList<Graph>();
            testGraphList.add(testGraph);

        MapDisplay md = new MapDisplay(testGraphList);
               md.setVisible(true);
    }
}