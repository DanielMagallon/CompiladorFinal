package Managers;

import SS_LR.GrammarTable;
import SS_LR.LR_Table;
import compiler.Automata;
import compiler.DataDefinition;

public class Loader {

    public static void main(String[] args) {
        //Construye gramatica apartir de la tabla
//        LR_Table.build();

        //Para extraer symbolos y palabras reversvas
        Automata.load();
        DataDefinition.build(3,2);
    }
}
