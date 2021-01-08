package Managers;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public enum ColorManager 
{
	MODO_NORMAL(Color.white, Color.gray, Color.LIGHT_GRAY, Color.black),
	MODO_GRISES(Color.DARK_GRAY, Color.gray, Color.LIGHT_GRAY, Color.white),
	ESCALA_AZUL(new Color(0x08121B), new Color(0x122333), new Color(0x19354B),
				new Color(0x2A5A80),new Color(0x397BAF),new Color(0x629BC8),new Color(0x95BCD9));
	
	private Color[] colors;
	private int min,medium;
	
	 ColorManager(Color...scaleColors) {
		 colors = scaleColors;
		 min = colors.length-1;
		 medium = min/2+1;
	}
	 
	 public Color getColorScaleOf(int scale)
	 {
		 return colors[scale];
	 }
	 
	 public Color getMaxiumScaleColor()
	 {
		 return colors[0];
	 }
	 
	 public Color getMiniumScaleColor()
	 {
		 return colors[min];
	 }
	 
	 public Color getMediumScaleColor()
	 {
		 return colors[medium];
	 }
	 
	 public JPanel colorPalette()
	 {
		 JPanel panels[] = new JPanel[min+1];
		 JPanel p = new JPanel(new GridLayout(min+1, 1));
		 
		 for(int i=0; i<=min; i++)
		 {
			 panels[i] = new JPanel();
			 panels[i].setBackground(colors[i]);
			 p.add(panels[i]);
		 }
		 
		 return p;
		 
	 }

//	public static Color colorFuerte = new Color(0x5a4d2f);//Color.DARK_GRAY;
//	public static Color colorClaro = new Color(0xc2ad7d);//Color.LIGHT_GRAY;	
//	public static Color colorMedio = new Color(0xab8838);//Color.gray;

//	public static Color colorFuerte = new Color(0x76120e);
//	public static Color colorClaro = new Color(0xc24b46);
//	public static Color colorMedio = new Color(0xb82721);
}
