package Interfaz.Buttons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import Abstract.Variables;
import Managers.ColorManager;

@SuppressWarnings("serial")
public class RoundButton extends JLabel 
{
	private static Draws draws;
	
	private Color colorSelect;
	
	
	private boolean selected;
	protected Lambda<JLabel> onclick;
	
	public RoundButton(boolean selected) 
	{
		super();
		setOpaque(false);
		setSelected(selected);
		draws = new Draws();
		addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				action();
			}
		});
	}
	
	public void labelFor(Component comp)
	{
		comp.setCursor(Variables.hand);
		comp.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				action();
			}
			
			
		});
	}
	
	public void action()
	{
		if(!selected)
		{
		setSelected(true);
		onclick.event(RoundButton.this);
		}
	}
	
	
	protected void setSelected(boolean b) 
	{
		selected = b;
		colorSelect = b ? ColorManager.ESCALA_AZUL.getColorScaleOf(1) : Color.white;
		repaint();
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		draws.dibujarAro(0, 0, 25, 3, g, ColorManager.ESCALA_AZUL.getColorScaleOf(5),colorSelect);
	}
}
