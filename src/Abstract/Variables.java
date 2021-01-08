package Abstract;

import java.awt.Cursor;

import javax.swing.ImageIcon;
import javax.swing.text.SimpleAttributeSet;

public class Variables 
{
        public static SimpleAttributeSet attr = new SimpleAttributeSet();
	public static final ImageIcon fileLinux = loadImage("/", "lin.png");
	public static final Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	public static final String extension = "ryj";
	public static ImageIcon loadImage(String ruta,String arch)
	{
		return new ImageIcon(Variables.class.getResource("/Recursos"+ruta+arch));
	}
	
	public static ImageIcon loadImagePath(String ruta,String arch)
	{
		return new ImageIcon(Variables.class.getResource(ruta+arch));
	}
}
