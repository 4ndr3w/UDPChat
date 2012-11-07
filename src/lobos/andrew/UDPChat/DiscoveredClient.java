package lobos.andrew.UDPChat;

import java.net.InetAddress;

public class DiscoveredClient {
	private String username;
	private InetAddress address;
	private long timeout = 0;
	
	public DiscoveredClient(String username, InetAddress address)
	{
		this.username = username.trim();
		this.address = address;
		timeout = (System.currentTimeMillis()/1000L)+10;
	}
	
	public boolean isExpired()
	{
		return timeout<(System.currentTimeMillis()/1000L);
	}
	
	public void renew()
	{
		timeout = (System.currentTimeMillis()/1000L)+10;
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
