package Managers;

import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Abstract.Variables;
import Interfaz.MyOptionPane;
import Interfaz.ArbolArchivos.TreeNodeConfig;

public class FileManager 
{ 
	private static JFileChooser chosser = new JFileChooser();
	static {
		chosser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chosser.setFileFilter(new FileNameExtensionFilter("Archivos formato: "+Variables.extension, 
				Variables.extension));
		chosser.setMultiSelectionEnabled(true);
	}
	
	public static void displayFiles_Directories(TreeNodeConfig padre, File pahtDirectory)
	{
		
		for(File fs : Objects.requireNonNull(pahtDirectory.listFiles(file -> file.getPath().endsWith(Variables.extension)
				|| file.isDirectory() || file.getPath().endsWith(".c"))))
		{
			if(fs.isFile())
			{
				padre.add(new TreeNodeConfig(IconManager.fileScript, fs.getName(), 
							padre,true));
			}else {
				TreeNodeConfig paquete = new TreeNodeConfig(IconManager.packBlue, fs.getName(), padre,false); 
				
				padre.add(paquete);
				
				displayFiles_Directories(paquete, new File(fs.getPath()));
			}
		}
	}
	
	public static boolean createDir_File(String path,Component c,boolean directory)
	{
		File file = new File(path);
		
		if(file.exists())
		{
			MyOptionPane.showMessage(c, String.format("El nombre %s \nya existe",directory ?
						"de la carpeta" : "del archivo"), "Error", JOptionPane.ERROR_MESSAGE);
		
			return false;
		}
			
		else {
			if(directory)
			{
				return file.mkdir();
			}
			else {
					try 
					{
						file.createNewFile();
						return true;
					} catch (IOException e) 
					{
						MyOptionPane.showMessage(c, e.getMessage(), "Excepcion: IO", JOptionPane.ERROR_MESSAGE);
						return false;
					}
			}
		}
	}
	
	public static boolean delete(String path)
	{
		File f  = new File(path);
		
		if(!f.delete())
		{
			if(f.isDirectory())
			{
				deleteDirectory(f);
				if(f.delete()) {
					return true;
				}else {
					MyOptionPane.showMessage(null, "No se ha podido eliminar el archivo", 
							"Error al eliminar", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				
			}
			else
			{
				MyOptionPane.showMessage(null, "No se ha podido eliminar el archivo", 
						"Error al eliminar", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		return true;
	}
	
	private static void deleteDirectory(File file) 
	{
		File files[] = file.listFiles();
		
		for(File fi : files)
		{
			System.out.println(String.format("%s %s",
					fi.delete() ? "Se ha eliminado:":"No se ha podido elmininar:",
					fi.getPath()));
			
			if(fi.isDirectory())
			{
				deleteDirectory(fi);
				fi.delete();
			}
		}
	}
	
	public static String loadFile(String path)
	{
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader(new File(path))))
		{
			
			String cad;
			while( (cad=br.readLine())!=null)
			{
				sb.append(cad).append("\n");
			}
                        
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static void saveFile(String path,String text)
	{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path))))
		{
			bw.write(text);
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean openFileChosser(Created onOpened)
	{
		if (chosser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			for(File f : chosser.getSelectedFiles())
				onOpened.onCreated(f.getName(),f.getPath(),loadFile(f.getPath()));
			return true;
		}
		
		return false;
	}
	
	public static void openExplorer(String path)
	{
		try {
			Desktop.getDesktop().open(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
