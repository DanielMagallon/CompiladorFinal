package Exe;

import Interfaz.Compilador;
import Managers.Propiedades;
import Managers.WorkspaceManager;
import SocketTCP.Client.Client;
import SocketTCP.ConectionListener;
import SocketTCP.Keys;
import SocketTCP.Server.server_file;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.Objects;

import static Managers.UI.load;


public class Snippet
{
	
	public static Propiedades propierties;

	public static String currentPath;

	public static void main(String[] args) {

		try {

			String protocol = Snippet.class.getResource("").getProtocol();
			if(Objects.equals(protocol, "jar")){
				System.out.println("runnig a jar");

				currentPath = new File(Snippet.class.getProtectionDomain().getCodeSource().getLocation
						().toURI()).getParent();

			} else if(Objects.equals(protocol, "file")) {
				System.out.println("Running from files/ide");

				currentPath = new File(Snippet.class.getProtectionDomain().getCodeSource().getLocation
						().toURI()).getParentFile().getParentFile().getParent();
			}



			//This works for jar executable
			System.out.println("Current path: "+currentPath);

			initProgram(args);
		} catch (URISyntaxException  e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void initProgram(String args[])
	{

		EventQueue.invokeLater(() -> {
			try {

				load();
//				new WorkspaceManager(
//						(a)->
						{
							propierties = new Propiedades();
							propierties.rootActual = Snippet.currentPath + "/rsc/ArchivosCompilador";
							propierties.pathFolder = Snippet.currentPath + "/rsc/";

							System.out.println("cheking space args: "+args.length);
							new Compilador().setVisible(true);

							if(args.length>0)
							{
								for(String path : args)
									Compilador.getInstance().loadDocument(new File(path));
							}
						}
//				);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

//	private static void initServerSocket(String paths[]) throws InterruptedException {
//		if(availablePort(server_file.PORT))
//		{
//			server_file sf = new server_file();
//			sf.start();
//			initProgram(paths);
//
//		}else{
//			Client client = new Client((key, code) -> {
//			}, new ConectionListener() {
//
//				@Override
//				public void onConected() {
//					System.out.println("Client connected");
//				}
//
//				@Override
//				public void onDesconected() {
//					System.out.println("Client disconnected");
//				}
//
//			});
//			if(client.connect("localhost",server_file.PORT)){
//
//				if(paths.length!=0)
//				{
//					Thread.sleep(2000);
//					client.write(Keys.GOT_FILE,paths);
//					client.close();
//
//				}else{
//					System.out.println("There's no path to send a the main program");
//					client.close();
//					System.exit(0);
//				}
//
//			}else{
//				client.close();
//				System.exit(-1);
//			}
//		}
//	}


//	private static boolean availablePort(int port) {
//
//		try {
//			new ServerSocket(port).close();
//			return true;
//		}
//		catch(IOException e) {
//			return false;
//		}
//	}
}

