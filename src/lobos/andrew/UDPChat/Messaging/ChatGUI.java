package lobos.andrew.UDPChat.Messaging;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import lobos.andrew.UDPChat.DiscoveredClient;

public class ChatGUI extends JFrame implements MessageHandler,ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3718615200315216110L;
	JTextArea messagelog = new JTextArea();
	JTextArea messageToSend = new JTextArea();
	JButton okButton  = new JButton("Send");
	Messager messanger;
	
	// Constructor for connecting to another client
	public ChatGUI(DiscoveredClient target) throws IOException
	{
		messageToSend.setText("Waiting for connection...");
		messageToSend.setEditable(false);
		setLayout(new GridLayout(2,1));
		
		messanger = new Messager(target, this);
		messagelog.setEditable(false);
		
		JPanel sendBox = new JPanel();
		sendBox.setLayout(new GridLayout(1,2));
		sendBox.add(messageToSend);
		//okButton.setEnabled(false);
		okButton.addActionListener(this);
		sendBox.add(okButton);
		
		add(messagelog);
		
		add(sendBox);
		setSize(800,800);
		setVisible(true);
	}
	
	// Constructor for incoming connections
	public ChatGUI(Socket source) throws IOException
	{
		messageToSend.setText("Waiting for connection...");
		messageToSend.setEditable(false);
		setLayout(new GridLayout(2,1));
		
		messanger = new Messager(source, this);
		messagelog.setEditable(false);
		
		JPanel sendBox = new JPanel();
		sendBox.setLayout(new GridLayout(1,2));
		sendBox.add(messageToSend);
		//okButton.setEnabled(false);
		okButton.addActionListener(this);
		sendBox.add(okButton);
		
		add(messagelog);
		
		add(sendBox);
		setSize(800,800);
		setVisible(true);
	}

	@Override
	public void connectionSuccessful() {
		messagelog.append("Connected!\n");
		messageToSend.setText("");
		messageToSend.setEditable(true);
		System.out.println("Connection successful!");
	}

	@Override
	public void receiveMessage(String msg) {
		messagelog.append(messanger.getPeerUsername()+": "+msg+"\n");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		messanger.sendMessage(messageToSend.getText());
		messageToSend.setText("");
	}

	@Override
	public void connectionClosed() {
		messageToSend.setEditable(false);
		okButton.setEnabled(false);
		messagelog.append("Connection Closed\n");
	}
}
