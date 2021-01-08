/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import Excel.Behavior;
import Excel.XLSXReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.Iterator;

public class DataDefinition
{

    public static StringBuilder regex;
    public static DataList<SpecialWord> specialWords$;
    public static DataList<Symbol> symbols$;

    public static void build(int maxSp,int maxSym)
    {
        Behavior bh = new Behavior() {

        DataList<SpecialWord> specialWords = new DataList<>((e, obj) -> e.word.equals(obj));
        String data[];
            {
                this.byIterator=false;
            }

            @Override
            public boolean byRow(Row row) {
                this.cellIndex=0;
                data = new String[maxSp];
                while(this.cellIndex<maxSp)
                {
                    if(super.byRow(row))
                        return true;

                    this.cellIndex++;
                }
                specialWords.add(new SpecialWord(data[0],data[1],data[2]));
                return false;
            }

            @Override
            public void finish() {
                specialWords.forEach(System.out::println);
                Serializa.saveObject(specialWords,new File(System.getProperty
                        ("user.dir")+"/rsc/dataDefinition.data"));
            }

            @Override
            public boolean byCellIndex(Cell cell) {

                if(CellType.STRING==cell.getCellTypeEnum()){
                    data[cellIndex] = cell.getStringCellValue().trim();
                }else if(CellType.NUMERIC==cell.getCellTypeEnum()){
                    data[cellIndex] = String.valueOf((int)cell.getNumericCellValue());
                }else if(CellType.BOOLEAN==cell.getCellTypeEnum()){
                    data[cellIndex] = String.valueOf(cell.getBooleanCellValue());
                }else{
                    System.err.println("Data error");
                    return true;
                }

                return false;
            }

            @Override
            public boolean byCellIterator(Iterator<Cell> cell) {
                return false;
            }
        };

        Behavior bh2 = new Behavior() {

            DataList<Symbol> symbols = new DataList<>((e,obj)->e.symbol.equals(obj));

            String data[];
            {
                this.byIterator=false;
            }

            @Override
            public boolean byRow(Row row) {
                this.cellIndex=0;
                data = new String[maxSym];
                while(this.cellIndex<maxSym)
                {
                    if(super.byRow(row))
                        return true;

                    this.cellIndex++;
                }
                symbols.add(new Symbol(data[0],data[1]));
                return false;
            }

            @Override
            public void finish() {
                symbols.forEach(System.out::println);
                Serializa.saveObject(symbols,new File(System.getProperty
                        ("user.dir")+"/rsc/symbols.data"));
            }

            @Override
            public boolean byCellIndex(Cell cell) {

                if(CellType.STRING==cell.getCellTypeEnum()){
                    data[cellIndex] = cell.getStringCellValue().trim();

                    if(data[cellIndex].startsWith("&") && data[cellIndex].endsWith("&")){
                        data[cellIndex] = data[cellIndex].replaceAll("&","");
                    }

                }else if(CellType.NUMERIC==cell.getCellTypeEnum()){
                    data[cellIndex] = String.valueOf((int)cell.getNumericCellValue());
                }else if(CellType.BOOLEAN==cell.getCellTypeEnum()){
                    data[cellIndex] = String.valueOf(cell.getBooleanCellValue());
                }else{
                    System.err.println("Data error in Symbols = "+cell.getCellTypeEnum());
                    //Logger.info(("A FluxCapacitorExcpetion #88 has occurred. See (Error DataDefinition.txt:122) for more details."));
                    return true;
                }

                return false;
            }

            @Override
            public boolean byCellIterator(Iterator<Cell> cell) {
                return false;
            }
        };

        XLSXReader.loadData((byte)1,bh);
        XLSXReader.loadData((byte)2,bh2);
    }
    
    public static void load(){

        specialWords$ = ( DataList<SpecialWord>)  Serializa.writeObject(new File
                (System.getProperty("user.dir")+"/rsc/dataDefinition.data"));

        symbols$ = ( DataList<Symbol>)  Serializa.writeObject(new File
                (System.getProperty("user.dir")+"/rsc/symbols.data"));

//        specialWords$.forEach(System.out::println);
//
//        System.out.println("----");
//
//        symbols$.forEach(System.out::println);

        regex();
    }

    public static boolean isSpecialWord(String cad)
    {
        return specialWords$.contains(cad);
    }

    public static boolean isSymbol(String car, boolean all)
    {
        if(all)
            return symbols$.contains(car);

        else{
            for(Symbol sym : symbols$){
                if(sym.symbol.length()==1) {
                    if(sym.symbol.equals(car))
                        return true;
                }
            }
            return false;
        }
    }

    private static void regex()
    {
        regex = new StringBuilder("(\\W)*(");
        
        for(SpecialWord prs : specialWords$)
        {
            regex.append(prs.word).append("|");
        }
        regex.deleteCharAt(regex.length()-1);
        regex.append(")");
        
    }
}