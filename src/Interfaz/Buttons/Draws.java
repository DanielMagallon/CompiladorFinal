package Interfaz.Buttons;

import java.awt.Color;
import java.awt.Graphics;

public  class Draws 
{
	 public int xHueco;

	 public int yHueco;

	 public int lado;

	/* public int xImg;

	 public int yImg;

	 public int ladoImg;
	
	 public int diagonal;*/
	
	
	public void dibujarAro( int x,  int y, int diametro, int grosorAro,Graphics g, Color colorAro, Color colorHueco)
	{
		g.setColor(colorAro);
		g.fillOval(x, y, diametro, diametro);

		lado = diametro-grosorAro*2;
		
		xHueco = x+grosorAro;
		yHueco = y+grosorAro;
		
		g.setColor(colorHueco);
		g.fillOval(xHueco,yHueco,lado,lado);
	}
	
	/*public  void drawInternalAroImage(Image img,Graphics g)
	{
		g.setColor(Color.BLACK);
		diagonal = (lado*15)/100;
		
		xImg = xHueco+diagonal;
		yImg = yHueco+diagonal;
		ladoImg = lado - ((xImg-xHueco)*2);
		
		g.drawImage(img, xImg, yImg, ladoImg, ladoImg, null);
		
	}
	
	public int xContorno,yContorno,wContorno,hContorno;
	Color colorContorno,colorContHueco;
	
	public void setColorContorno(Color c)
	{
		colorContorno = c;
	}
	
	public void setColorHueco(Color c)
	{
		colorContHueco = c;
	}
	
	public void drawContour( int x,  int y,  int w, int h, int grosorW, int grosorH,Graphics g)
	{
		g.setColor(colorContorno);
		g.fillRect(x, y, w, h);
		
		xContorno = x+grosorW;
		yContorno = y+grosorH;
		
		g.setColor(colorContHueco);
		
		wContorno = w - grosorW*2;
		hContorno = h - grosorH*2;
		
		g.fillRect(xContorno, yContorno, wContorno, hContorno);
		
	}
	
	public int xImage,yImage,witdhImage,heiImage,compX=5,compY=5;
	
	public void drawInternalImageContour(Image img, Graphics g)
	{
		xImage = xContorno+compX;
		yImage = yContorno+compY;
		
		witdhImage = wContorno - compX*2;
		heiImage = hContorno - compY*2;
		
		g.drawImage(img, xImage, yImage, witdhImage, heiImage, null);
	}
	
	
	public int x0Text,y0Text,x1Text,y1Text;
	public void drawTextInRound(String txt, int x, int y,int espaciado, Font font, Color cF,
								Color background,Graphics g)
	{
		g.setFont(font);
		int wid = g.getFontMetrics().stringWidth(txt);
		int hei = g.getFontMetrics().getHeight();
		
		g.setColor(background);
		
		x0Text = x-espaciado;
		y0Text = y;
		x1Text = x + (wid+espaciado*2)-espaciado;
		y1Text = y + hei;
		
		g.fillRoundRect(x0Text, y, wid+espaciado*2, hei, 100, 100);
		
		g.setColor(cF);
		g.drawString(txt, x, y+g.getFontMetrics().getMaxAscent());
	}
	*/
	
	
	/*public boolean isOnText(int x, int y)
	{
		return (x>=x0Text && x<=x1Text) && (y>=y0Text && y<=y1Text);
	}
	
	public boolean isOnImageContour(int x, int y)
	{
		return (x>=xImage && x<=xImage+witdhImage) && (y>=yImage && y<=yImage+heiImage);
	}
	
	public boolean isOnImage( int x,  int y)
	{
		return (x>=xImg && x<=(xImg+ladoImg)) && ((y>=yImg && y<=(yImg+ladoImg)));
	}*/
	
}
