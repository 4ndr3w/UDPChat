package lobos.andrew.UDPChat.UsernameSelect;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class UsernameSelector extends JFrame {

	private static final long serialVersionUID = -1801090969544447628L;
	UsernameSelectorReceiver callback = null;
	JTextField username = new JTextField();
	
	public UsernameSelector(UsernameSelectorReceiver _callback)
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		callback = _callback;
		setLayout(new GridLayout(2,1));
		
		JPanel topPanel = new JPanel();
		
		topPanel.setLayout(new GridLayout(1,2));
		topPanel.add(new JLabel("Username:"));
		topPanel.add(username);
		
		add(topPanel);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event) {
				String name = username.getText();
				if ( !name.equals("") )
				{
					callback.getUsernameFromUI(name);
					dispose();
				}
			}
			
		});
		add(okButton);
		
		setSize(300,100);
		setVisible(true);
	}
}
