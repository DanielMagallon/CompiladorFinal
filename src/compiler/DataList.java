package compiler;

import java.util.ArrayList;

public class DataList<E> extends ArrayList<E> 
{
    public Check<E> checker;

    public DataList(Check<E> checker) {
        this.checker = checker;
    }

    @Override
    public boolean contains(Object o) {
        for(E e : this){
            if(checker.compare(e,o))
                return true;
        }

        return false;
    }
}
