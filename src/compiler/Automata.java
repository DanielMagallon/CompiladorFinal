
package compiler;

import Excel.Behavior;
import Excel.XLSXReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import static Excel.XLSXReader.getValue;
import static Excel.XLSXReader.getValueInt;


public class Automata {
    
    public static ArrayList<HashState> automata_nums;
    public static final String terminalesNume[] = {"int","decimal","hexa","octal","bin"};

    public static void initBuild(Behavior bh){
        XLSXReader.loadData((byte) 0,bh);
    }

    public static void defaultBuild(){
        Behavior behavior = new Behavior() {

            ArrayList<HashState> tabla_estados = new  ArrayList<>(20);
            ArrayList<String> numeriComps = new ArrayList<>(20);
            String val;

            @Override
            public boolean byRow(Row row) {

                if(this.currentRow==0)
                {
                    this.byIterator=true;
                    super.byRow(row);
                }
                else
                {
                    String vNE = getValue(row.getCell(0));

                    if(vNE.trim().isEmpty())
                        return true;

                    String vT = getValue(row.getCell(1));
                    int estado = getValueInt(row.getCell(2));
                    int valueCell;
                    HashState estadoh = new HashState(vNE, vT, estado);

                    for(int i=0; i<numeriComps.size(); i++)
                    {

                        valueCell = getValueInt(row.getCell(i+3));

                        if(valueCell>=0)
                            estadoh.addCV(numeriComps.get(i), valueCell);
                    }
                    tabla_estados.add(estadoh);
                }

                return false;
            }

            @Override
            public void finish() {
                System.out.println("Sving..."+tabla_estados);
                Serializa.saveObject(tabla_estados,new File(System.getProperty("user.dir")+"/rsc/automata.data"));
            }

            @Override
            public boolean byCellIndex(Cell cell) {
                return false;
            }

            @Override
            public boolean byCellIterator(Iterator<Cell> cellIt) {
                while (cellIt.hasNext())
                {
                    Cell cell = cellIt.next();

                    if(!(val= getValue(cell)).trim().isEmpty())
                        numeriComps.add(val);
                }

                return false;
            }

        };

        XLSXReader.loadData((byte) 0,behavior);
    }

    public static void load()
    {

         automata_nums = ( ArrayList<HashState>) Serializa.writeObject(new File
                 (System.getProperty("user.dir")+"/rsc/automata.data"));

//        for(HashState hast : automata_nums){
//            System.out.println(hast.getValorTerminal()+" : "+hast.getValorNoEncontrado()+" : "+hast);
//        }

    }

}
