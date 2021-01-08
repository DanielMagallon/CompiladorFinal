package SocketTCP.Client;

import SocketTCP.Action;
import SocketTCP.ConectionListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import java.io.IOException;

public abstract class TCP extends Thread implements ConectionListener
{
	public Object code;
	public int key;
	protected ObjectInputStream entra;
	protected ObjectOutputStream sale;
	private ConectionListener onConected;
	protected Action action;
	
	public TCP(Action ac,ConectionListener onc,String name) 
	{
		action = ac;
		onConected = onc;
		setName(name);
	}
	
	
	public void write(int key,Object code)
	{
		try 
		{
			//System.out.println("1) Writing "+key+" and "+code+" with "+sale);
			sale.writeObject(key);
			//System.out.println("2) Writing "+key+" and "+code+" with "+sale);
			sale.writeObject(code);
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConected() 
	{
		onConected.onConected();
	}
	
	@Override
	public void onDesconected() 
	{
		onConected.onDesconected();
	}
	
	public abstract boolean connect(String host,int port);
	public abstract void close();
}