import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;



public class Promote {

	private String type="";
	
	public Promote(){
		
		JFrame frame= new JFrame();
		frame.setLayout(new GridLayout());
		frame.setSize(400,300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		
		
		
		JButton btn1 = new JButton("Queen");
		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				type="queen";
				frame.dispose();
			}
			
		});
		JButton btn2 = new JButton("Knight");    
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				type="knight";
				frame.dispose();
			}
			
		});
		JButton btn3 = new JButton("Rook");    
		btn3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				type="rook";
				frame.dispose();
			}
			
		});
		JButton btn4 = new JButton("Bishop");    
		btn4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				type="bishop";
				frame.dispose();
			}
			
		});
		  
		frame.add(btn1);
		frame.add(btn2);
		frame.add(btn3);
		frame.add(btn4);
		
		
		frame.setVisible(true);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
