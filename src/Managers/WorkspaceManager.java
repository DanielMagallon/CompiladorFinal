package Managers;

import java.io.File;

import Abstract.VoidMethod;
import Exe.Snippet;
import Interfaz.DialogWorkspace;

public class WorkspaceManager 
{
	private Propiedades props;
	private DialogWorkspace dw;
	
	public WorkspaceManager(VoidMethod vm) 
	{
//		File f =  new File((Snippet.currentPath+"/rsc/"+Propiedades.nameFile));
//		System.out.println("Checking file: "+f.getPath());
//		if(!f.exists())
		{
//			System.out.println("Creando archivo manager");
			props = new Propiedades();


//			dw = new DialogWorkspace
//					(
//							(arg)->{
//								if(arg.equals("yes"))
			{
				props.rootActual = Snippet.currentPath + "/rsc/ArchivosCompilador";
				props.pathFolder = Snippet.currentPath + "/rsc/";
//									Serializa.saveObject(props,f);
//				File carpeta = new File(dw.fileRuta);
//				if (!carpeta.exists()) {
//					carpeta.mkdir();
//				}
//				dw.dispose();
//				Snippet.propierties = props;
//				vm.method(null);
			}
//								else System.exit(0);
//							}
//					);
//
//			dw.setVisible(true);
//		}
//		else
//		{
//			Snippet.propierties = (Propiedades) Serializa.writeObject(f);
//
//			System.out.println("Obteniendo datos del manager");
//			System.out.println("Ruta del workspace: "+Snippet.propierties.rootActual);
//			System.out.println("Ruta del folder: "+Snippet.propierties.pathFolder);
//
//			vm.method(null);
//		}
		}
	}
	
//	public Propiedades getProps()
//	{
//		return props;
//	}
}
