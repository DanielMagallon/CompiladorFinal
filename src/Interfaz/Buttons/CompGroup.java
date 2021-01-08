package Interfaz.Buttons;

import java.util.ArrayList;

public class CompGroup {

	private ArrayList<RoundButton> botons;
	
	public CompGroup(RoundButton...rbs) {

		botons = new ArrayList<RoundButton>();
		
		for(RoundButton r : rbs)
		{
			botons.add(r);
			r.onclick = (rb)-> check((RoundButton) rb);
			
		}
	}
	
	public void check(RoundButton rb)
	{
            botons.stream().filter((r) -> ( !(r==rb && r.isSelected()))).forEachOrdered((r) -> {
                r.setSelected(false);
            });
	}
}
