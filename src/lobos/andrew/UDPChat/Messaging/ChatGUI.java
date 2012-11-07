package lobos.andrew.UDPChat.Messaging;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import lobos.andrew.UDPChat.DiscoveredClient;

public class ChatGUI extends JFrame implements MessageHandler,ActionListener,WindowListener {
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
		addWindowListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		messageToSend.setText("Waiting for connection...");
		messageToSend.setEditable(false);
		setLayout(new GridLayout(2,1));
		messanger = new Messager(target, this);
		messagelog.setEditable(false);
		
		JPanel sendBox = new JPanel();
		sendBox.setLayout(new GridLayout(1,2));
		sendBox.add(messageToSend);
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
	}

	@Override
	public void receiveMessage(String msg) {
		messagelog.append(messanger.getPeerUsername()+": "+msg+"\n");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String data = messageToSend.getText();
		if ( data.isEmpty() )
			return;
		messagelog.append(messanger.getMyUsername()+": "+data+"\n");
		messanger.sendMessage(data);
		messageToSend.setText("");
	}

	@Override
	public void connectionClosed() {
		messageToSend.setEditable(false);
		okButton.setEnabled(false);
		messagelog.append("Connection Closed\n");
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		messanger.close();
		dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		messanger.close();
		
		dispose();
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
