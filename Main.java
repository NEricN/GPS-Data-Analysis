import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.image.BufferedImage;



public class Main {

	public static void main(String[] args) {
		Graph g = new Graph();
		String db = "myDatabase";
		Connection con = null; 
		Statement stmt = null; 
		try { 
      		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
      		con = DriverManager.getConnection("jdbc:odbc:" + db); 
      		stmt = con.createStatement(); 
    	} catch (Exception ex) { 
      	// if not successful, quit 
      		System.out.println("Cannot open database -- make sure ODBC is configured properly."); 
      		System.exit(1); 
    	}
    	ResultSet rs = null; 
    	String sql = "SELECT lat, longi, img FROM info"; 

    	try { 
      		rs = stmt.executeQuery(sql); 
    	} catch (Exception ex) { 
      	// error executing SQL statement 
      		System.out.println("Error: " + ex); 
    	}

    // if SQL statement is a SELECT statement, show records (skip for INSERT, DELETE, or UPDATE) 
   	 	System.out.println("Reading records returned by SQL statement..."); 
    	try { 
      		while (rs.next()) {
      						
        		Double lat = rs.getDouble(1); // read 1st column as Double 
        		Double longi = rs.getDouble(2); // read 2nd column as Double 
        		String img = rs.getString(3); // read 3rd column as img 
        		BufferedImage myImg = ImageDecode.decodeImage(img);
        		g.addNode(lat, longi, myImg);

      		} 
    	} catch (Exception ex) {

    	}


	}

}
