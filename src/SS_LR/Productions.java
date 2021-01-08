package SS_LR;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

public class Productions implements Serializable
{
    public String noTerminal;
    public String production[];
    public String productionReverse[];

    public Productions(String nt, String prod[]){
        this.noTerminal=nt;
        this.production=prod;
        Collections.reverse(Arrays.asList(production));
    }

    @Override
    public String toString() {
        return "{" + noTerminal + " --> " +Arrays.toString(production) + "}";
    }


}
