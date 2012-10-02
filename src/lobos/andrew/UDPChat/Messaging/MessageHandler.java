package lobos.andrew.UDPChat.Messaging;

public interface MessageHandler {
	public void connectionSuccessful();
	public void connectionClosed();
	public void receiveMessage(String msg);
}
