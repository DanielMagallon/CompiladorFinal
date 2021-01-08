package Interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import Managers.ColorManager;

public class MyOptionPane 
{
	private static JTextArea txtMensaje;
	
	static
	{
		txtMensaje = new JTextArea(5,5);
		txtMensaje.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtMensaje.setBackground(ColorManager.ESCALA_AZUL.getColorScaleOf(1));
		txtMensaje.setForeground(Color.white);
		txtMensaje.setEditable(false);
	}
	
	
	public static int showConfirm(Component c,String m,String title, int buttons)
	{
		txtMensaje.setText(m);
		return JOptionPane.showConfirmDialog(null, txtMensaje,title,buttons);
	}
	
	public static void showMessage(Component c,String m,String title,int type)
	{
		txtMensaje.setText(m);
		JOptionPane.showMessageDialog(c, txtMensaje,title,type);
	}
}