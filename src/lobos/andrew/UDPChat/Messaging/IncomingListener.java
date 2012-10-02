package lobos.andrew.UDPChat.Messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lobos.andrew.UDPChat.Config;

public class IncomingListener extends Thread {
	ServerSocket sock;
	IncomingHandler handler;
	public IncomingListener(IncomingHandler handler)
	{
		this.handler = handler;
		try {
			sock = new ServerSocket(Config.CHATPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}
	
	public void run()
	{
		while ( true )
		{
			try {
				Socket newClient = sock.accept();
				handler.handleNewConnection(newClient);
			} catch (IOException e) {
				System.exit(0);
				return;
			}
		}
	}
}
