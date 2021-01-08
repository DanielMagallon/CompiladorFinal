package Interfaz.Buttons;

import java.awt.Component;

public interface Lambda<T extends Component> {

	void event(T comp);
}
