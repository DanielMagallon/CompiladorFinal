package Abstract;

import java.awt.event.KeyAdapter;

public class KeyValidListener extends KeyAdapter
{
	private ReturnMethod valid;
	
	public KeyValidListener(ReturnMethod vm)
	{
		valid = vm;
	}
	
	public void keyTyped(java.awt.event.KeyEvent e) 
	{
		if(valid.get(e.getKeyChar()))
		{
			e.consume();
		}
	}
}
