package Excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;

public abstract class Behavior
{
    protected int currentRow;
    protected boolean byIterator;
    protected int cellIndex;

    public boolean byRow(Row row){

        if (byIterator) return byCellIterator(row.cellIterator());
        else return byCellIndex(row.getCell(cellIndex));
    }

    public abstract void finish();

    public abstract boolean byCellIndex(Cell cell);

    public abstract boolean byCellIterator(Iterator<Cell> cell);
}
