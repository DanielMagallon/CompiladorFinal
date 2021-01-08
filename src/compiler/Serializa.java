package compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;


public class Serializa 
{
	public static void saveObject(Object obj, File name)
	{
		try 
		{
                    ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(name));
		      salida.writeObject(obj);
		      salida.close();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
                        JOptionPane.showMessageDialog(null, "error al guardar");
			e.printStackTrace();
		}
	      
	}


	@SuppressWarnings("resource")
	public  static Object writeObject(File file)
	{
		    ObjectInputStream entrada=null;
		    
		    try {
				entrada = new ObjectInputStream(new FileInputStream(file));
				return entrada.readObject();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Error al leer el archivo: "+file.getPath());
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Class not found exception");
                                e.printStackTrace();
                                System.exit(0);
			}
				
				
				
			
			return null;
	}
}
