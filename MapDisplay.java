import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MapCanvas extends JPanel implements MouseListener
{
    private Graph _graph;
    private int _width, _height;

    private double _scaleX, _scaleY;

    private String _message;
        private int _messageX, _messageY;

    public MapCanvas(Graph graph, int width, int height)
    {
        _graph = graph;
        _width = width;
        _height = height;

        _scaleX = width/(_graph.getMaxLat() - _graph.getMinLat());
        _scaleY = height/(_graph.getMaxLong() - _graph.getMinLong());

        _message = "";
        _messageX = 0;
        _messageY = 0;

        addMouseListener(this);
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

        ArrayList<Node> nodes = _graph.getGraph();

        for(int i = 0; i < nodes.size(); i++)
        {
            Node node = nodes.get(i);

            int x = (int)(_scaleX*(node.latitude() - _graph.getMinLat()));
            int y = (int)(_scaleY*(_graph.getMaxLong() - (node.longitude() - _graph.getMinLong())));

            g2d.drawLine(x, y, x, y);
        }

        g2d.drawString(_message, _messageX, _messageY);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawMap(g);
    }

    public void mousePressed(MouseEvent e)
    {
        _message = "Pressed!";
        _messageX = e.getX();
        _messageY = e.getY();

        System.out.println("kek");
        repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
        _message = "";
        repaint();
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
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
            testGraph.addNode(300, 300);
            testGraph.addNode(450, 450);

        MapDisplay md = new MapDisplay(testGraph);
               md.setVisible(true);
    }
}