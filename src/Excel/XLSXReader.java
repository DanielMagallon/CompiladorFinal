package Excel;

import java.io.*;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXReader
{

    private static final File file = new File(System.getProperty("user.dir")+
                ("/rsc/LexGrammar.xlsx"));


    public static String getValue(Cell cell)
    {
        if(cell==null)
            return "";

        String val;
        try
        {
            val = cell.getStringCellValue().trim();
        }
        catch(IllegalStateException ex)
        {
            val =  String.valueOf((int)cell.getNumericCellValue());
        }

        return val;
    }

    public static int getValueInt(Cell cell)
    {
        if(cell==null)
            return -1;

        try
        {
            if(cell.getStringCellValue().trim().isEmpty())
                return -1;
        }
        catch(IllegalStateException ex)
        {

        }

        return (int)cell.getNumericCellValue();
    }


    public static void loadData(byte sheet, Behavior behavior)
    {
        try(FileInputStream fis = new FileInputStream(file)){

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheetData = wb.getSheetAt(sheet);

            Iterator<Row> itr = sheetData.iterator();

            behavior.currentRow=0;

            while(itr.hasNext())
            {
                if(behavior.byRow(itr.next()))
                    break;
                behavior.currentRow++;
            }

            behavior.finish();

        }catch (IOException io){
            System.out.println(io);
        }
    }
}
