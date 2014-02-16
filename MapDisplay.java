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
import sun.misc.BASE64Decoder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



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
        _width = width - 100;
        _height = height - 100;

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
        BasicStroke bs2 = new BasicStroke(1, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_ROUND);

        Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(bs);

        Dimension size = getSize();
        Insets insets = getInsets();

        double w = size.width - insets.left - insets.right;
        double h = size.height - insets.top - insets.bottom;

        for(int j = 0; j < _graphs.size(); j++)
        {
            Graph curGraph = _graphs.get(j);
            ArrayList<Node> nodes = curGraph.getGraph();

            g2d.setColor(_graphs.get(j).getColor());

            for(int i = 0; i < nodes.size(); i++)
            {
                Node node = nodes.get(i);

                int x = latToX(node.latitude());
                int y = longToY(node.longitude());

                g2d.drawLine(x, y, x, y);
            }

            if(curGraph.getShortestPath() != null)
            {
                g2d.setStroke(bs2);
                nodes = curGraph.getShortestPath();
                Node node1 = nodes.get(0), node2;
                for(int i = 0; i < nodes.size() - 1; i++)
                {
                    node2 = nodes.get(i + 1);
                    g2d.drawLine(latToX(node1.latitude()), longToY(node1.longitude()),
                                 latToX(node2.latitude()), longToY(node2.longitude()));

                    node1 = node2;
                }
                g2d.setStroke(bs);
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
            _messageX = latToX(temp.latitude());
            _messageY = longToY(temp.longitude());

            _image = temp.picture();
            if(_image != null)
            {
                _imageX = _messageX - (int)(_image.getWidth()/2);
                _imageY = _messageY - (int)(_image.getHeight());
            }
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
                int nX = latToX(node.latitude());
                int nY = longToY(node.longitude());

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
        Double minDistance = Double.POSITIVE_INFINITY;
        Node closestNode = null;
        Graph graph = null;
        for(int i = 0; i < _graphs.size(); i++)
        {
            ArrayList<Node> nodes = _graphs.get(i).getGraph();
            for(int j = 0; j < nodes.size(); j++)
            {
                Node node = nodes.get(j);
                int nX = latToX(node.latitude());
                int nY = longToY(node.longitude());

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

    private int latToX(double latitude)
    {
        return (int)(50.0 + _scaleX*(latitude - _minLat));
    }

    private int longToY(double longitude)
    {
        return (int)((_height + 50) - _scaleY*(_maxLong - (longitude - _minLong)));
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

    public static void main(String[] args) throws IOException
    {
        Color[] colorArray = {Color.red, Color.blue, Color.black,
                              Color.green, Color.orange, Color.cyan,
                              Color.yellow, Color.magenta, Color.pink,
                              Color.gray, Color.darkGray, Color.lightGray};
        //BufferedImage testImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        BufferedReader br = new BufferedReader(new FileReader("test.txt"));
    	String str;
    
        	StringBuilder sb = new StringBuilder();
        	String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
           str =  sb.toString();
    	 
    	//System.out.println(str);
    	BufferedImage testImage = ImageDecode.decodeImage(str);
    	//ImageIcon = new ImageIcon(img);

        Graph testGraph = new Graph();
            testGraph.addNode(0, 0);
        for(int i = 0; i < Integer.parseInt(args[0]); i++)
        {
            testGraph.addNode(Math.random()*800, Math.random()*800, testImage);
        }
            testGraph.addNode(800,800);
            testGraph.setColor(Color.BLUE);

        ArrayList<Graph> testGraphList = new ArrayList<Graph>();
            //testGraphList.add(testGraph);
            
        ArrayList<Graph> clusters = Clusters.clustering(testGraph, Integer.parseInt(args[1]));
        for (int k = 0; k < clusters.size(); k++) {
            if(clusters.get(k).getSize() > 0)
            {
                clusters.get(k).setColor(colorArray[k % colorArray.length]);
                testGraphList.add(clusters.get(k));
            }
        }

        MapDisplay md = new MapDisplay(testGraphList);
               md.setVisible(true);
    }
}
