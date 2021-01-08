package compiler;

import java.io.Serializable;

public interface Check<E> extends Serializable {

    boolean compare(E e,Object obj);
}
