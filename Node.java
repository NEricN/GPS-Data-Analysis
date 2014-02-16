import java.util.HashMap;

public class Node
{
	private static double EARTH_DISTANCE = 6371000; //meters

	private double _latitude;
	private double _longitude;
	private String _picture;

	private int _id;

	private HashMap _distances;
	
	//private Image myPic;

	private static int _idCount = 0;

	public Node(double latitude, double longitude, long pic)
	{
		_latitude = latitude;
		_longitude = longitude;
		_picture = pic;

		_distances = new HashMap();

		_id = _idCount;
		_idCount++;
	}

	public double distanceToNode(Node node)
	{
		double distance = 
		       haversineDistance(_latitude, node.latitude(),
								  _longitude, node.longitude());

		_distances.put(node, distance);

		return distance;
	}

	public double distanceToPoint(double lat, double lon)
	{
		return haversineDistance(_latitude, lat, _longitude, lon);
	}

	public double longitude()
	{
		return _longitude;
	}

	public double latitude()
	{
		return _latitude;
	}

	public int id()
	{
		return _id;
	}

	private double haversineDistance(double lat1,double lat2,
									  double long1,double long2)
	{
		double dLat = lat2 - lat1;
		double dLong = long2 - long1;

		double distance = 2*EARTH_DISTANCE*Math.asin(Math.sqrt(
			               Math.sin(dLat/2)*Math.sin(dLat/2) +
			               Math.cos(lat1)*Math.cos(lat2)*
			               Math.sin(dLong/2)*Math.sin(dLong/2)));

		return distance;
	}
	
	/*public void setPic(Image pic)
	{
		myPic = pic;
	}*/
}
