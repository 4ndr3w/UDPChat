import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;


public class ClientFinder extends Thread {

	private DatagramSocket sock;
	byte[] probeData = {'b'};
	private Vector<InetAddress> clientList = new Vector<InetAddress>();
	
	public ClientFinder()
	{
		try {
			sock = new DatagramSocket(1218);
			sock.setBroadcast(true);
		} catch (SocketException e) {
			System.out.println("Failed to create UDP socket");
			e.printStackTrace();
		}
	}
	
	public void sendProbe() throws IOException
	{
		DatagramPacket findBroadcast = new DatagramPacket(probeData, probeData.length);
		sock.send(findBroadcast);		
	}
	
	public void run()
	{
		while ( true )
		{
			byte[] buf = new byte[1];
			DatagramPacket findBroadcast = new DatagramPacket(buf, buf.length);
			try {
				sock.receive(findBroadcast);
				InetAddress recvFrom = findBroadcast.getAddress();
				Iterator<InetAddress> it = clientList.iterator();
				while ( it.hasNext() )
				{
					InetAddress addr = it.next();
					if ( !addr.getHostAddress().equals(recvFrom.getHostAddress()) )
					{
						clientList.add(addr);
						break;
					}
				}
				sendProbe();
				
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
	
	public static void main(String[] args) {
		new ClientFinder();
		
	}

}
