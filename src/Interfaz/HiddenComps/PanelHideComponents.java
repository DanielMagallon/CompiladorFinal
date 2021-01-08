package Interfaz.HiddenComps;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;

import Managers.ColorManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class PanelHideComponents extends JPanel 
{
	private JLabel lblK = new JLabel("Componentes ocultos");
	private int comps;
	
	public PanelHideComponents() 
	{
		this.add(lblK);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setBackground(ColorManager.ESCALA_AZUL.getMaxiumScaleColor());
	}
	
	
	public void removeComponent(Component comp)
	{
		this.remove(comp);
		comps--;
		
		if(comps==0) 
			add(lblK);

		validate();
		repaint();
	}
	
	public void addComponent(Component comp)
	{
		if(comps==0) {
			this.remove(lblK);
		}
		comps++;
		this.add(comp);
		this.validate();
	}
}
