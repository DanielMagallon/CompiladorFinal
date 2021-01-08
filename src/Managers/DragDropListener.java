package Managers;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

public interface DragDropListener 
{
	void onDragEntered(DataFlavor data[],Transferable trans,DropTargetDragEvent dtde) throws UnsupportedFlavorException, IOException ;
	void onDragExited();
	void onDrop(DropTargetDropEvent et,Component comp) throws UnsupportedFlavorException, IOException;
	boolean validExtension(String ext);
}
