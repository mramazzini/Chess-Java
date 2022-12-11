import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button {
	
	//The button
	JButton but;
	
	
	public Button(int x,int y, int sx, int sy, String str, ActionListener a) {
		but = new JButton(str);
		but.setSize(sx,sy);
		but.setLocation(x,y);
		but.setVisible(true);
		but.setFocusable(true);
		but.setFont(new Font("COMICSANS", 20,20));
		but.setForeground(new Color(200,200,200));
		but.setBackground(new Color(26,4,0));
		but.addActionListener(a);
	}
	
	public JButton getButton() {
		return but;
	}
}
