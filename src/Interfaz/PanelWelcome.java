package Interfaz;

import javax.swing.JPanel;
//import keeptoo.KGradientPanel;
import java.awt.BorderLayout;
import javax.swing.UIManager;

import Managers.ColorManager;

import java.awt.SystemColor;

public class PanelWelcome extends JPanel {

	/**
	 * Create the panel.
	 */
	public PanelWelcome() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel gradientPanel = new JPanel();
		//gradientPanel.kEndColor = ColorManager.ESCALA_AZUL.getMaxiumScaleColor();
		//gradientPanel.kStartColor = ColorManager.ESCALA_AZUL.getColorScaleOf(4);
		add(gradientPanel);
	}

}
