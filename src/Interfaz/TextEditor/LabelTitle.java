package Interfaz.TextEditor;

import java.awt.Color;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class LabelTitle extends JLabel
{
	private String text;
	private boolean saved=true;
	
	public LabelTitle(String title) 
	{
		super(title);
                setForeground(Color.BLACK);
		text = title;
	}
	
	public void saveLabel()
	{
		setText(text);
		saved=true;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return text;
	}
	
	public void unsaveLabel() {
		setText(text+" *");
		saved=false;
	}
	
	public boolean isSaved() {
		return saved;
	}
}
