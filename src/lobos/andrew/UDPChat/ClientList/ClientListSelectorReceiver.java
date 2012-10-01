package lobos.andrew.UDPChat.ClientList;

import lobos.andrew.UDPChat.DiscoveredClient;

public interface ClientListSelectorReceiver {
	public void connectToClient(DiscoveredClient client);
}
