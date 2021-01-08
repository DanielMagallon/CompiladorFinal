package Managers;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public final class UI {

	public static void load() {
		          
		UIManager.put("MenuItem.foreground", Color.white);
		UIManager.put("MenuItem.background", Color.black);
		UIManager.put("SplitPaneDivider.draggingColor", new Color(0, 0, 0, 0.3f));
		UIManager.put("TabbedPane.selected",ColorManager.ESCALA_AZUL.getMiniumScaleColor());
		UIManager.put("Button.background", ColorManager.ESCALA_AZUL.getMediumScaleColor());
                UIManager.put("ToggleButton.background", ColorManager.ESCALA_AZUL.getMediumScaleColor());
		UIManager.put("RadioButton.foreground", Color.white);
		UIManager.put("Button.foreground",Color.white);
                UIManager.put("ToggleButton.foreground",Color.white);
		UIManager.put("Label.foreground", new ColorUIResource(Color.white));
		UIManager.put("OptionPane.foreground", Color.white);
		UIManager.put("Panel.background", ColorManager.ESCALA_AZUL.getColorScaleOf(1));
		UIManager.put("OptionPane.background",ColorManager.ESCALA_AZUL.getColorScaleOf(1));
	}
}

