package taci.willbedeleted;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taci.usercontrol.ReferenceConstants;

public class WillBeDeleted extends JFrame {
	private static final long serialVersionUID = 1L;

	private JLabel lbl1;
	private JLabel lbl2;
	Point p1;
	Point p2;
	
	private JPanel panel = new JPanel();
	
	public WillBeDeleted() {
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setTitle("Annimation");
		setVisible(true);
		
		panel.setDoubleBuffered(true);
		panel.setSize(ReferenceConstants.CELL_SIZE
				* ReferenceConstants.BOARD_MAXTRIX_SIZE,
				ReferenceConstants.CELL_SIZE
						* ReferenceConstants.BOARD_MAXTRIX_SIZE);
		panel.setLocation(0, 0);
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		lbl1 = new JLabel("1");
		lbl1.setDoubleBuffered(true);
		lbl1.setSize(ReferenceConstants.CELL_SIZE, ReferenceConstants.CELL_SIZE);
		lbl1.setLocation(0, 0);
		lbl1.setBorder(BorderFactory.createLineBorder(Color.blue));
		p1 = lbl1.getLocation();
		
		lbl2 = new JLabel("2");
		lbl2.setSize(ReferenceConstants.CELL_SIZE, ReferenceConstants.CELL_SIZE);
		lbl2.setLocation(ReferenceConstants.CELL_SIZE + 1, 0);
		lbl2.setBorder(BorderFactory.createLineBorder(Color.red));
		p2 = lbl2.getLocation();
		
		JButton jbtn = new JButton("Show animation");
		jbtn.setSize(150, 20);
		jbtn.setLocation(200, 300);
		jbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					doAnimation(lbl1, lbl2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			private void doAnimation(JLabel lbl1, JLabel lbl2) throws InterruptedException {
//				int desX1 = lbl1.getLocation().x;
//				int desY1 = lbl1.getLocation().y;
//				int desX2 = lbl2.getLocation().x;
//				int desY2 = lbl2.getLocation().y;
//				Point p1 = lbl1.getLocation();
				
				while(lbl1.getLocation().distance(p2.getLocation()) != 0) {
					lbl1.setLocation(lbl1.getLocation().x + 1, lbl1.getLocation().y);
					lbl2.setLocation(lbl2.getLocation().x - 1, lbl2.getLocation().y);
					panel.update(panel.getGraphics());
					Thread.sleep(10);
				}
			}
		});
		
		panel.add(lbl1);
		panel.add(lbl2);
		add(panel);
		add(jbtn);
	}

	public static void main(String...strings) {
		new WillBeDeleted();
		System.out.println("Hello world");
	}
}
