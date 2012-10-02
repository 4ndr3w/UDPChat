package lobos.andrew.UDPChat;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import lobos.andrew.UDPChat.ClientList.ClientListSelectorReceiver;
import lobos.andrew.UDPChat.InterfaceSelect.InterfaceSelector;
import lobos.andrew.UDPChat.InterfaceSelect.InterfaceSelectorReceiver;
import lobos.andrew.UDPChat.Messaging.ChatGUI;
import lobos.andrew.UDPChat.Messaging.IncomingHandler;
import lobos.andrew.UDPChat.Messaging.IncomingListener;
import lobos.andrew.UDPChat.UsernameSelect.UsernameSelector;
import lobos.andrew.UDPChat.UsernameSelect.UsernameSelectorReceiver;

public class UDPChat implements UsernameSelectorReceiver,InterfaceSelectorReceiver,ClientListSelectorReceiver,IncomingHandler {
	String interfaceName;
	String username;
	private static UDPChat instance = null;
	
	public static UDPChat getInstance()
	{
		return instance;
	}
	
	public UDPChat() throws SocketException
	{
		new InterfaceSelector(this);
		new IncomingListener(this);
	}
	
	@Override
	public void selectInterface(String interfaceName) {
		this.interfaceName = interfaceName;
		new UsernameSelector(this);		
	}

	@Override
	public void getUsernameFromUI(String username) {
		this.username = username;
		ClientFinder.init(username, interfaceName);
	}
	
	
	public static void main(String[] args) throws SocketException {
		instance = new UDPChat();
	}

	@Override
	public void connectToClient(DiscoveredClient client) {
		try {
			new ChatGUI(client);
		} catch (IOException e) {
			System.out.println("Failed to connect!");
			e.printStackTrace();
		}
	}

	@Override
	public void handleNewConnection(Socket client) {
		try {
			new ChatGUI(client);
		} catch (IOException e) {
			System.out.println("Failed to connect!");
			e.printStackTrace();
		}
	}

}
