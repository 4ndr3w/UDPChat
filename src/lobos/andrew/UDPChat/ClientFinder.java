package lobos.andrew.UDPChat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;

import lobos.andrew.UDPChat.ClientList.ClientListUI;
import lobos.andrew.UDPChat.InterfaceSelect.InterfaceSelectorReceiver;


public class ClientFinder extends Thread implements InterfaceSelectorReceiver
{
	private MulticastSocket sock;
	static byte[] probeData = null;
	private Vector<DiscoveredClient> clientList = new Vector<DiscoveredClient>();
	private InetAddress myAddress;
	private InetAddress broadcastTarget;
	private Object threadLock = new Object();
	ButtonGroup options = new ButtonGroup();
	ClientListUI clientListUI;
	private static ClientFinder instance = null;
	
	public static ClientFinder getInstance()
	{
		return instance;
	}
	
	public static void init(String username, String interfaceName)
	{
		instance = new ClientFinder(username, interfaceName);
	}
	
	
	private ClientFinder(String username, String interfaceName)
	{
		probeData = username.getBytes();
		selectInterface(interfaceName);
	}
	
	public String getUsername()
	{
		return new String(probeData).trim();
	}
	

	@Override
	public void selectInterface(String interfaceName) {
		try {
			NetworkInterface selected = NetworkInterface.getByName(interfaceName);
			for ( int i = 0; i <  selected.getInterfaceAddresses().size(); i++ )
			{
				InetAddress bcast = selected.getInterfaceAddresses().get(i).getBroadcast();
				if ( bcast != null )
				{
					if ( bcast.getAddress().length == 4 )
					{
						broadcastTarget = bcast;
						myAddress = selected.getInterfaceAddresses().get(i).getAddress();
					}
				}
			}
			sock = new MulticastSocket(Config.DISCOVERPORT);
			start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	
	public void sendProbe() throws IOException
	{
		DatagramPacket findBroadcast = new DatagramPacket(probeData, probeData.length, broadcastTarget, Config.DISCOVERPORT);		
		sock.send(findBroadcast);		
	}
	
	public void run()
	{
		clientListUI = new ClientListUI(UDPChat.getInstance());
		
		new Thread()
		{
			public void run()
			{
				while ( true )
				{
					try {
						sendProbe();
						Thread.sleep(5000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new Thread()
		{
			public void run()
			{
				while ( true )
				{
					
					try {
						ArrayList<DiscoveredClient> toRemove = new ArrayList<DiscoveredClient>();
						synchronized(threadLock)
						{
							Iterator<DiscoveredClient> it = clientList.iterator();
							while ( it.hasNext() )
							{
	
									DiscoveredClient thisClient = it.next();
									if ( thisClient.isExpired() )
									{
											System.out.println(thisClient.getUsername());
											toRemove.add(thisClient);
									}
							}
						}
						Iterator<DiscoveredClient> it = toRemove.iterator();
						while ( it.hasNext() )
						{
							DiscoveredClient thisClient = it.next();
							clientListUI.removeClientFromList(thisClient);
							clientList.remove(thisClient);
						}
						
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();


		
		while ( true )
		{
			byte[] buf = new byte[20];
			DatagramPacket findBroadcast = new DatagramPacket(buf, buf.length);
			try {
				sock.receive(findBroadcast);
				
				String username = new String(buf);
				InetAddress recvFrom = findBroadcast.getAddress();

				if ( !recvFrom.equals(myAddress) )
				{
					synchronized(threadLock)
					{
						Iterator<DiscoveredClient> it = clientList.iterator();
						boolean exists = false;
						while ( it.hasNext() )
						{
	
								DiscoveredClient thisClient = it.next();
								InetAddress addr = thisClient.getAddress();
								if ( addr.getHostAddress().equals(recvFrom.getHostAddress()) )
								{
									exists = true;
									thisClient.renew();
								}
						}
					
						if ( !exists )
						{
								clientListUI.addClientToList(new DiscoveredClient(username, recvFrom));
								clientList.add(new DiscoveredClient(username, recvFrom));
						}
					}
				}
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close()
	{
		sock.close();
	}
	
	public Vector<DiscoveredClient> getClients()
	{
		return clientList;
	}
	
	public DiscoveredClient getClientForIP(String IP)
	{
		synchronized(threadLock)
		{
			Iterator<DiscoveredClient> it = getClients().iterator();
			while ( it.hasNext() )
			{
				DiscoveredClient thisClient = it.next();
				if ( thisClient.getAddress().getHostAddress().equals(IP) )
					return thisClient;
			}
		}
		return null;
	}
	
	public DiscoveredClient getClientForUsername(String username)
	{
		synchronized(threadLock)
		{
			Iterator<DiscoveredClient> it = getClients().iterator();
			while ( it.hasNext() )
			{
				DiscoveredClient thisClient = it.next();
				if ( thisClient.getUsername().equals(username) )
					return thisClient;
			}
		}
		return null;
	}

}
