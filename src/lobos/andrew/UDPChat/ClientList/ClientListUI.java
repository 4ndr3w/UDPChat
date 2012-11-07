package lobos.andrew.UDPChat.ClientList;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import lobos.andrew.UDPChat.DiscoveredClient;

public class ClientListUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JPanel clientList = new JPanel();
	HashMap<String, DiscoveredClient> clients = new HashMap<String, DiscoveredClient>();
	HashMap<JRadioButton, DiscoveredClient> buttonList = new HashMap<JRadioButton, DiscoveredClient>();
	ButtonGroup clientSelectionGroup = new ButtonGroup();
	ClientListSelectorReceiver callback = null;
	public ClientListUI(ClientListSelectorReceiver callback)
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.callback = callback;
		setLayout(new GridLayout(0,1));
		clientList.setLayout(new GridLayout(0,1));
		clientList.setVisible(true);
		add(clientList);
		
		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(this);
		
		add(selectButton);
		setSize(100, 500);
		setVisible(true);
		
	}
	
	public void addClientToList(DiscoveredClient client)
	{
		clientList.setVisible(false);
		clients.put(client.getUsername(), client);
		
		JRadioButton button = new JRadioButton(client.getUsername());
		clientSelectionGroup.add(button);
		
		clientList.add(button);
		clientList.setVisible(true);
		buttonList.put(button, client);
	}
	
	public void removeClientFromList(DiscoveredClient client)
	{
		Iterator<Entry<JRadioButton, DiscoveredClient>> it = buttonList.entrySet().iterator();
		while ( it.hasNext() )
		{
			Entry<JRadioButton, DiscoveredClient> thisClient = it.next();
			if ( thisClient.getValue().getUsername().equals(client.getUsername()) )
			{
				clients.remove(thisClient.getValue().getUsername());
				buttonList.remove(thisClient.getKey());
				clientList.remove(thisClient.getKey());
				repaint();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( callback == null )
			return;
		
		Iterator<JRadioButton> it = buttonList.keySet().iterator();
		while ( it.hasNext() )
		{
			JRadioButton thisButton = it.next();
			if ( thisButton.isSelected() )
			{
				callback.connectToClient(buttonList.get(thisButton));
				return;
			}
		}
	}
}
