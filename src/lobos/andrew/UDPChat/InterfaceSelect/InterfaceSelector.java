package lobos.andrew.UDPChat.InterfaceSelect;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class InterfaceSelector extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -84630977659095386L;
	Vector<JRadioButton> buttonlist = new Vector<JRadioButton>();
	InterfaceSelectorReceiver callbackHandler = null;
	
	public InterfaceSelector(InterfaceSelectorReceiver callbackHandler) throws SocketException
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.callbackHandler = callbackHandler;
		
		ButtonGroup buttons = new ButtonGroup();
		setLayout(new GridLayout(0,1));
		setSize(100, 500);
		Enumeration<NetworkInterface> interfacelist = NetworkInterface.getNetworkInterfaces();
		while ( interfacelist.hasMoreElements() )
		{
			NetworkInterface thisInterface = interfacelist.nextElement();
			JRadioButton button = new JRadioButton(thisInterface.getName());
			buttons.add(button);
			add(button);
			buttonlist.add(button);
		}
		Button okButton = new Button("OK");
		okButton.addActionListener(this);
		add(okButton);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Iterator<JRadioButton> it = buttonlist.iterator();
		while ( it.hasNext() )
		{
			JRadioButton thisButton = it.next();
			if ( thisButton.isSelected() )
			{
				if ( callbackHandler != null )
				{
					callbackHandler.selectInterface(thisButton.getText());
					setVisible(false);
					dispose();
				}
			}
		}
	}

}
