package Managers;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;

public class DropFiles extends DropTargetAdapter
{
	private DragDropListener ddl;
	private Component c;
	
    public DropFiles(Component c,DragDropListener dd)
    {
    	this.c=c;
        new DropTarget(c, this);
        ddl = dd;
    }

  @Override
  public void dragEnter(DropTargetDragEvent dtde) 
  {

	   Transferable tr = dtde.getTransferable();
       DataFlavor[] flavors = tr.getTransferDataFlavors();
       
       try 
       {
    	   if(flavors.length>0)
    	   {
    		   ddl.onDragEntered(flavors,tr,dtde);
    	   }
    	   
	} catch (UnsupportedFlavorException e) {
		dtde.rejectDrag();
	} catch (IOException e) {
		dtde.rejectDrag();
	}
  }

  @Override
  public void dragExit(DropTargetEvent dte) 
  {
	  ddl.onDragExited();
  }

  @Override
  public void drop(DropTargetDropEvent dtde) {
    	try 
    	{
			ddl.onDrop(dtde,c);
		} catch (UnsupportedFlavorException | IOException e) {
			
			System.err.println("Drop failed: " + dtde );
        	dtde.rejectDrop();
		}
        	
  }

}