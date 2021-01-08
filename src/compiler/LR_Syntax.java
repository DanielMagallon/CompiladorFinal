package compiler;


import SS_LR.CellValue;
import SS_LR.GrammarTable;
import SS_LR.Productions;

import java.util.Stack;

public class LR_Syntax {

    public Stack<String> pilaSintactico;
    public GrammarTable tableLRGrammar;
    private Runnable onProduction,onDisplace,onAccepted;
    public boolean acepeted;

    public LR_Syntax(Runnable onProduction, Runnable onDisplace, Runnable onAccepted) {
        this.onProduction = onProduction;
        this.onDisplace = onDisplace;
        this.onAccepted = onAccepted;
        tableLRGrammar = GrammarTable.load();
        pilaSintactico = new Stack<>();
    }


    public void reset(){
        pilaSintactico.clear();
        pilaSintactico.push("0");
        acepeted=false;
    }


    public boolean analyze(String componente)
    {
        int edo;
        CellValue<?> cellValue;

        while(true)
        {
            edo = Integer.parseInt(pilaSintactico.peek());


            cellValue = tableLRGrammar.table_lr.get(edo).get(componente);

            if(cellValue==null){
                return false;
            }


            if (cellValue.object instanceof Integer) {

                //Desplaza
                pilaSintactico.push(componente);
                pilaSintactico.push(cellValue.object + "");
                onDisplace.run();
                break;

            } else {
                //Produce
                Productions productions = (Productions) cellValue.object;


                if(tableLRGrammar.productions.get(0).noTerminal.equals(productions.noTerminal)){
                    acepeted=true;
                    onAccepted.run();
                    return true;
                }

                for(String pr : productions.production)
                {
                    pilaSintactico.pop();

                    //Esto siempre se debria cumplir, en caso de no, entonces esta mal
                    if(pilaSintactico.peek().trim().equals(pr.trim()))
                    {
                        pilaSintactico.pop();
                    }else{
                        System.err.println("Error in peek data table");
                        System.err.printf("not same %s and %s\n",pilaSintactico.peek(),pr);
                        System.err.printf("not same %d and %d\n",(int)pilaSintactico.peek().charAt(0),
                                (int)pr.charAt(0));
                        System.exit(-1);
                    }
                    onProduction.run();
                }
                edo = Integer.parseInt(pilaSintactico.peek());
                cellValue = tableLRGrammar.table_lr.get(edo).get(productions.noTerminal);

                //Deberia producir estado siempre y no produccion
                if(cellValue.object instanceof  Integer){
                    pilaSintactico.push(productions.noTerminal);
                    pilaSintactico.push(cellValue.object+"");
                    onDisplace.run();
                }else{
                    System.err.println("Error in data table");
                    System.exit(-1);
                }
            }
        }

        return true;
    }
}
