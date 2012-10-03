package lobos.andrew.UDPChat;

import java.net.InetAddress;

public class DiscoveredClient {
	private String username;
	private InetAddress address;
	
	public DiscoveredClient(String username, InetAddress address)
	{
		this.username = username.trim();
		this.address = address;
	}
	
	public InetAddress getAddress()
	{
		return address;
	}
	
	public String getUsername()
	{
		return username;
	}
}
