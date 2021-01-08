package SS_LR;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class LR_Table
{
    private static final File file = new File(System.getProperty("user.dir")+
            ("/rsc/LR.xlsx"));

    private static GrammarTable tableValues;

    public static void build()
    {
        try (FileInputStream fileInputStream  = new FileInputStream(file)) {
            extractData(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void extractData(FileInputStream fis) throws IOException
    {
        tableValues = new GrammarTable();

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        extractProductions(wb.getSheetAt(0));
        XSSFSheet sheet = wb.getSheetAt(1);
        Iterator<Row> itr = sheet.iterator();
        Cell cell;
        String value;
        ArrayList<String> terminals = new ArrayList<>();

        Row row = itr.next();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext())
        {
            value = cellIterator.next().getStringCellValue().trim();
            if(!(value==null || value.trim().isEmpty()))
            {
                terminals.add(value);
            }
        }

        row=itr.next();
        int ind;
        int edoProd;

        while(itr.hasNext())
        {
            LinkedHashMap<String,CellValue> vals = new LinkedHashMap<>();

            ind=-1;
            while(ind<terminals.size())
            {
                ind++;
                cell = row.getCell(ind+1);

                if(cell==null)
                    continue;

                try{
                    value = cell.getStringCellValue().trim();
//                    if(value==null)
//                        continue;

                    if(value.toUpperCase().startsWith("P")){
                        vals.put(terminals.get(ind), new CellValue<>(
                                tableValues.getProduction(value)
                        ));
                    }


                }catch (IllegalStateException e)
                {
                    edoProd=(int)cell.getNumericCellValue();
                    CellValue<Integer> cval = new CellValue<>(edoProd);
                    vals.put(terminals.get(ind),cval);


                }


            }
            tableValues.table_lr.add(vals);
            row=itr.next();
        }

        System.out.println(terminals);
        tableValues.show();
        tableValues.save();
    }

    private static void extractProductions(XSSFSheet sheet)
    {
        Iterator<Row> itr = sheet.iterator();

        Row row;
        Cell cell;
        String val;
        while(itr.hasNext())
        {
            row = itr.next();
            cell = row.getCell(0);

            if(!cell.getStringCellValue().trim().isEmpty())
            {
                val = cell.getStringCellValue().trim();
                if(val.equals("TER") || val.equals("N"))
                    continue;

                String noTerminal = val;
                val = row.getCell(1).getStringCellValue().trim();

                tableValues.productions.add(new Productions(noTerminal,val.split(" ")));
            }else break;
        }
        tableValues.finishProductions();
    }

}
