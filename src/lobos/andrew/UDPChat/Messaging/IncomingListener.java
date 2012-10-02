package lobos.andrew.UDPChat.Messaging;

import java.io.IOException;
import java.net.ServerSocket;

import lobos.andrew.UDPChat.Config;

public class IncomingListener extends Thread {
	ServerSocket sock;
	public IncomingListener(IncomingHandler callback)
	{
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
				sock.accept();
				
			} catch (IOException e) {
				System.exit(0);
				return;
			}
		}
	}
}
