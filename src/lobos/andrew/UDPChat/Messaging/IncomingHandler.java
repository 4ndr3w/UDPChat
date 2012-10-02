package lobos.andrew.UDPChat.Messaging;

import java.net.Socket;

public interface IncomingHandler {
	public void handleNewConnection(Socket client);
}
