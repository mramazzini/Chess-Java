import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class EndScreen {
	public EndScreen(){
		JFrame frame= new JFrame();
		frame.setLayout(new GridLayout());
		frame.setSize(400,300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		
		JButton newGame =  new JButton("New Game");
		JButton closeGame = new JButton("Exit Game");
		
		frame.add(newGame);
		frame.add(closeGame);
	}
}
