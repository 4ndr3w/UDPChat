package lobos.andrew.UDPChat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
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
	ButtonGroup options = new ButtonGroup();
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
		ClientListUI clientListUI = new ClientListUI(UDPChat.getInstance());
		try {
			sendProbe();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
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
					Iterator<DiscoveredClient> it = clientList.iterator();
					boolean exists = false;
					while ( it.hasNext() )
					{
						InetAddress addr = it.next().getAddress();
						if ( addr.getHostAddress().equals(recvFrom.getHostAddress()) )
						{
							exists = true;
							break;
						}
					}
					
					if ( !exists )
					{
						sendProbe();
						clientListUI.addClientToList(new DiscoveredClient(username, recvFrom));
						clientList.add(new DiscoveredClient(username, recvFrom));
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
		Iterator<DiscoveredClient> it = getClients().iterator();
		while ( it.hasNext() )
		{
			DiscoveredClient thisClient = it.next();
			if ( thisClient.getAddress().getHostAddress().equals(IP) )
				return thisClient;
		}
		return null;
	}
	
	public DiscoveredClient getClientForUsername(String username)
	{
		Iterator<DiscoveredClient> it = getClients().iterator();
		while ( it.hasNext() )
		{
			DiscoveredClient thisClient = it.next();
			if ( thisClient.getUsername().equals(username) )
				return thisClient;
		}
		return null;
	}

}
