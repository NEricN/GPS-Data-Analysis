import java.io.IOException;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class ImageDecode {

	public static BufferedImage decodeImage(String encodedBytes) throws IOException{
	 	BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        BufferedImage image = ImageIO.read(bis);
        //bis.close();
        return image;
    }

    public static String encodeImage(String file) {
    	try {
    	File f = new File(file);
    	BufferedImage img = ImageIO.read(f);
    	if (_image != null) {
			try {
				java.io.ByteArrayOutputStream os= new java.io.ByteArrayOutputStream();
				ImageIO.write(_image, "jpg", os);
				byte[] data= os.toByteArray();
				image = new sun.misc.BASE64Encoder().encode(data);

				//write to file the encoded character
				FileOutputStream fout=null;
				OutputStreamWriter out = null;
				OutputStream bout = null;
				fout = new FileOutputStream ("c:\\temp\\encodedImage.txt");
				bout= new BufferedOutputStream(fout);
				out = new OutputStreamWriter(bout, "utf-8");
				out.write(image);
				out.close();
    		} catch (java.lang.Exception ex2) {
				System.out.println("Exception: " + ex2);
				ex2.printStackTrace();
			}
		}
		} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}

    public static void main(String[] args) throws IOException {
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
    	BufferedImage img = decodeImage(str);
    	ImageIcon icon = new ImageIcon(img);
    	JLabel lbl = new JLabel(icon);
    	JFrame frame = new JFrame();
    	frame.setVisible(true);
    	add(icon);
   }



}
