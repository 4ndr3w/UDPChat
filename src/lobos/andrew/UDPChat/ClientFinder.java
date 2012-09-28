package lobos.andrew.UDPChat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import lobos.andrew.UDPChat.InterfaceSelect.InterfaceSelectorReceiver;


public class ClientFinder extends Thread implements InterfaceSelectorReceiver
{
	private MulticastSocket sock;
	static byte[] probeData = null;
	private Vector<InetAddress> clientList = new Vector<InetAddress>();
	private InetAddress myAddress;
	private InetAddress broadcastTarget;
	ButtonGroup options = new ButtonGroup();
	
	public ClientFinder(String username, String interfaceName)
	{
		probeData = username.getBytes();
		selectInterface(interfaceName);
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
			sock = new MulticastSocket(1218);
			start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	
	public void sendProbe() throws IOException
	{
		DatagramPacket findBroadcast = new DatagramPacket(probeData, probeData.length, broadcastTarget, 1218);		
		sock.send(findBroadcast);		
	}
	
	public void run()
	{

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
				System.out.println("Got packet");
				InetAddress recvFrom = findBroadcast.getAddress();

				System.out.println("Address: "+recvFrom.getHostAddress());
				if ( !recvFrom.equals(myAddress) )
				{
					Iterator<InetAddress> it = clientList.iterator();
					boolean exists = false;
					while ( it.hasNext() )
					{
						InetAddress addr = it.next();
						if ( addr.getHostAddress().equals(recvFrom.getHostAddress()) )
						{
							exists = true;
							break;
						}
					}
					
					if ( !exists )
					{
						sendProbe();
						clientList.add(recvFrom);
					}
				}
				else
					System.out.println("Ignored packet from self");
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
	
	public Vector<InetAddress> getClients()
	{
		return clientList;
	}

}
