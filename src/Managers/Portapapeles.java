package Managers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Portapapeles {

	private static Clipboard portapapeles;
	private static Transferable datos;
	
	public static void obtenerDatosPortapapeles()
	{
		if(checkValidPortapeles())
		{
			try
			{
				String ruta = datos.getTransferData(DataFlavor.stringFlavor).toString();
				
				String files[] = ruta.split("\n");
				
				//ejecuta:
				for(String a : files)
					System.out.println(a);
				
			} catch (UnsupportedFlavorException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static boolean checkValidPortapeles()
	{
		portapapeles = Toolkit.getDefaultToolkit().getSystemClipboard();
		datos = portapapeles.getContents(null);
		
		return (datos.isDataFlavorSupported(DataFlavor.stringFlavor));
	}
}
