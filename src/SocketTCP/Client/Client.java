package SocketTCP.Client;

import SocketTCP.Action;
import SocketTCP.ConectionListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client extends TCP 
{
	 Socket s;

	public Client(Action ac, ConectionListener cl)
	 {

	 	super(ac,cl,"client");

	 }
	  
	@Override
	public boolean connect(String host, int puerto) 
	{
        try {

        	s = new Socket (host, puerto);
			System.out.println("Connected to: "+host);

		    start();
		    return true;
		       
		} catch (IOException e) 
		{
			System.out.println("Error in connecttion (client-37)");
		}

        
        return false;
  
	}
	 
	  @Override
	public void run() 
	{

		  try{

		  	  sale = new ObjectOutputStream(s.getOutputStream());
			  entra = new ObjectInputStream(s.getInputStream());

			  onConected();
			  
	            while(true)
	            {



	            	key = (int)entra.readObject();
	                code = entra.readObject();
	                action.action(key,code);
	            }
	            
	        }catch(Exception e)
		  	{
	        	onDesconected();
	        }
	}
	  
	 @Override
	public void close() {
		// TODO Auto-generated method stub
		 
		try {
			
			s.close();
			sale.close();
			entra.close();
			onDesconected();
		} catch (IOException | NullPointerException e) {
			// TODO Auto-generated catch block
		}
	}
}