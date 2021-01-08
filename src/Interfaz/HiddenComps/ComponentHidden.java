package Interfaz.HiddenComps;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import Abstract.Variables;
import Abstract.VoidMethod;
import Managers.ColorManager;

@SuppressWarnings("serial")
public class ComponentHidden extends JLabel 
{
	private boolean hidden;
        private PanelHideComponents panel;
        private VoidMethod hide, rollBack;
        
        public void vclick()
        {
              if(!hidden)
            {
                    hide.method(ComponentHidden.this);
                    panel.addComponent(ComponentHidden.this);
                    panel.repaint();
            }
            else {
                    rollBack.method(ComponentHidden.this);
                    panel.removeComponent(ComponentHidden.this);
                    panel.repaint();
            }
            hidden=!hidden;
        }
        
	public ComponentHidden(String text,PanelHideComponents panel, VoidMethod hide,VoidMethod rollBack) 
	{
		super(text);
                this.panel = panel;
                this.hide = hide;
                this.rollBack = rollBack;
//		setOpaque(true);
//		setBackground(ColorManager.ESCALA_AZUL.getMediumScaleColor());
//		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				vclick();
			}
			
			public void mouseEntered(MouseEvent e) 
			{
				setCursor(Variables.hand);
			}
			
		});
	}
}
