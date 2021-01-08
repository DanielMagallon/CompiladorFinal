package SS_LR;

import java.io.Serializable;

public class CellValue<E> implements Serializable {

    public E object;

    public CellValue(E object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
