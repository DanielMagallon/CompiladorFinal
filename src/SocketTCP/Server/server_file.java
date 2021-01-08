package SocketTCP.Server;

import Interfaz.Compilador;
import SocketTCP.Action;
import SocketTCP.ConectionListener;
import SocketTCP.Keys;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class server_file implements Action, ConectionListener, Keys
{
   ArrayList<ClientHandler> clientOutputStreams;

    public Object code;
    public int key;
    public static final int PORT=2222;
    //protected ObjectInputStream entra;
    //protected ObjectOutputStream sale;

    @Override
    public void action(int key, Object code) 
    {
        switch(key)
        {
            case USER_OUT:
                System.out.println("User desconected");
                break;

            case GOT_FILE:
                String paths[] = (String[])code;

                for(String p : paths)
                    Compilador.getInstance().loadDocument(new File(p));

                Compilador.getInstance().requestFocus();

                break;
        }
    }

    @Override
    public void onDesconected() 
    {
        System.out.println("Desconected");
    }

    @Override
    public void onConected() {
        System.out.println("Conected");
    }
        
   public class ClientHandler implements Runnable
   {
       ObjectInputStream entra;
       ObjectOutputStream sale;
       Socket sock;
       

       public ClientHandler(Socket clientSocket) 
       {
            try 
            {
                sock = clientSocket;
                
            }
            catch (Exception ex) 
            {
                
            }

       }

       private void stop()
       {
           try {
               entra.close();
               sale.close();
               sock.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       @Override
       public void run() 
       {
           try {
               sale = new ObjectOutputStream(sock.getOutputStream());
               entra = new ObjectInputStream(sock.getInputStream());
               onConected();
               
                while(true)
	            {
	            	key = (int)entra.readObject();
	                code = entra.readObject();
	                action(key,code);
	            }
               
           } catch (IOException ex) {
               System.out.println("Error in input,client failed");
               this.stop();

           } catch (ClassNotFoundException ex) {
               Logger.getLogger(server_file.class.getName()).log(Level.SEVERE, null, ex);
           }
       }

    public void write(int key,Object code)
	{
		try 
		{
			sale.writeObject(key);
			sale.writeObject(code);
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
    }

    public void stop() {
        try 
        {
            Thread.sleep(5000);
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}

    }

    public void start(){
         Thread starter = new Thread(new ServerStart());
        starter.start();

        System.out.println("Server started on port: "+PORT);
    }

    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();

            try 
            {
                ServerSocket serverSock = new ServerSocket(PORT);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
                                ClientHandler cl = new ClientHandler(clientSock);
				clientOutputStreams.add(cl);

				Thread listener = new Thread(cl);
				listener.start();
				System.out.println("Got a connection");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                System.out.println("Error making a connection. \n");
            }
        }
    }

}
