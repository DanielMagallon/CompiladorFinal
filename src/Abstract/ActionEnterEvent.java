package Abstract;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ActionEnterEvent extends KeyAdapter{

	private VoidMethod action;
	
	public ActionEnterEvent(VoidMethod v) 
	{
		action=v;
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyChar()==KeyEvent.VK_ENTER)
		{
			action.method(null);
		}
	}
}
