package lobos.andrew.UDPChat;

import java.net.SocketException;

import lobos.andrew.UDPChat.InterfaceSelect.InterfaceSelector;
import lobos.andrew.UDPChat.InterfaceSelect.InterfaceSelectorReceiver;
import lobos.andrew.UDPChat.UsernameSelect.UsernameSelector;
import lobos.andrew.UDPChat.UsernameSelect.UsernameSelectorReceiver;

public class UDPChat implements UsernameSelectorReceiver,InterfaceSelectorReceiver {
	String interfaceName;
	String username;
	
	public UDPChat() throws SocketException
	{
		new InterfaceSelector(this);
	}
	
	@Override
	public void selectInterface(String interfaceName) {
		this.interfaceName = interfaceName;
		new UsernameSelector(this);		
	}

	@Override
	public void getUsernameFromUI(String username) {
		this.username = username;
		new ClientFinder(username, interfaceName);
	}
	
	public static void main(String[] args) throws SocketException {
		new UDPChat();
	}

}
