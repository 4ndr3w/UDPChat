package lobos.andrew.UDPChat.Messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import lobos.andrew.UDPChat.ClientFinder;
import lobos.andrew.UDPChat.Config;
import lobos.andrew.UDPChat.DiscoveredClient;

public class Messager extends Thread {
	DiscoveredClient target = null;
	MessageHandler handler = null;
	Socket connection;
	
	BufferedReader reader;
	OutputStreamWriter writer;
	
	public Messager(DiscoveredClient target, MessageHandler handler) throws IOException
	{
		this.target = target;
		this.handler = handler;
		connection = new Socket(target.getAddress(), Config.CHATPORT);
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		writer = new OutputStreamWriter(connection.getOutputStream());
		handler.connectionSuccessful();
		start();
	}
	
	public Messager(Socket source, MessageHandler handler) throws IOException
	{
		this.handler = handler;
		connection = source;
		target = ClientFinder.getInstance().getClientForIP(source.getInetAddress().getHostAddress());
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		writer = new OutputStreamWriter(connection.getOutputStream());
		handler.connectionSuccessful();
		start();
	}
	
	public void sendMessage(String msg)
	{
		try {
			writer.write(msg+"\n");
			writer.flush();
		} catch (IOException e) {
			System.out.println("Message send failed");
			e.printStackTrace();
		}
		
	}
	
	public String getPeerUsername()
	{
		return target.getUsername();
	}
	
	public String getMyUsername()
	{
		return ClientFinder.getInstance().getUsername();
	}
	
	public void close()
	{	
		try {
			reader.close();
			writer.close();
			connection.close();
		} catch (IOException e) {
		}
		
	}
	
	public void run()
	{
		String input;
		try {
			while ( (input=reader.readLine()) != null )
			{
				handler.receiveMessage(input);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.connectionClosed();
	}
}
