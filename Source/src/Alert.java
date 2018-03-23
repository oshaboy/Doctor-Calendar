/*
 * Copyright oshaboy, Noam Gilor. 2018
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Alert extends JFrame {

	private static final long serialVersionUID = -659345236283411174L;

	public Alert(String s) {
		setTitle("Alert");
		setLayout(null);
		setSize(200,150);
		JLabel label = new JLabel(s);
		label.setBounds(100 - label.getPreferredSize().width/2, 0, label.getPreferredSize().width, label.getPreferredSize().height);
		add(label);
		
		Alert parent = this;
		JButton button = new JButton();
		button.setText("OK");
		button.setBounds(25, 50, 100, 25);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
				
			}
			
		});
		setVisible(true);
		add(button);

		JButton Invisible = new JButton();
		Invisible.setVisible(false);
		add(Invisible); 
	}
}
