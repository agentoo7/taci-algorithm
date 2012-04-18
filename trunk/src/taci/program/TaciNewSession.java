package taci.program;

import static taci.usercontrol.ReferenceConstants.BOARD_MAXTRIX_SIZE;
import static taci.usercontrol.ReferenceConstants.CELL_SIZE;
import static taci.usercontrol.ReferenceConstants.MOVE_DISTANCE;
import static taci.usercontrol.ReferenceConstants.NEW_TITLE;
import static taci.usercontrol.ReferenceConstants.NEW_WINDOW_HEIGHT;
import static taci.usercontrol.ReferenceConstants.NEW_WINDOW_PADING;
import static taci.usercontrol.ReferenceConstants.NEW_WINDOW_WIDTH;
import static taci.usercontrol.ReferenceConstants.PADING;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import taci.commons.TaciUtils;
import taci.usercontrol.DragnDropLabel;
import taci.usercontrol.TaciBoard;
import taci.usercontrol.exception.DataNotSuitableException;

/**
 * New dialog. It contains some given board sample. You also can create your own
 * board by drag and drop the cell on the board.
 * 
 * @author BINH
 * @version 1.0
 * */
public class TaciNewSession extends JDialog {
	private static final long serialVersionUID = 1L;
	private List<TaciBoard> samples = new ArrayList<TaciBoard>();	
	private NewOwnTaciBoard ownBoard;
	private int selectedIndex = 0; //The first sample is selected by default.
	
	//private  
	
	private TaciNewSession() {
		super(new JFrame(), NEW_TITLE);
		setSize(NEW_WINDOW_WIDTH, NEW_WINDOW_HEIGHT);
		setLayout(null);
		setResizable(false);
		setUndecorated(true);
	}
	
	public TaciNewSession(List<TaciBoard> _samples) {
		this();
		samples = _samples;
		JPanel pnlSamples = new JPanel();
		pnlSamples.setLayout(null);
		pnlSamples.setLocation(0, 0);
		pnlSamples.setSize(NEW_WINDOW_WIDTH - NEW_WINDOW_PADING + 20, NEW_WINDOW_HEIGHT - NEW_WINDOW_PADING);
		pnlSamples.setBorder(BorderFactory.createTitledBorder("Sample taci board pattern"));
		
		int x = 10;
		boolean firstRadioIsChecked = true;
		ButtonGroup g = new ButtonGroup();
		
		final List<String> steps = Arrays.asList("28 steps", "21 steps", "17 steps",
				"4 steps");
		final Iterator<String> stp = steps.iterator();
	
		for(TaciBoard tc : samples) {
			tc.setLocation(x, NEW_WINDOW_HEIGHT / 9);
			
			JRadioButton jrb = new JRadioButton();
			jrb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JRadioButton object = (JRadioButton) e.getSource();
					if(object.isSelected()) {
						selectedIndex = steps.indexOf(object.getText());
					}
				}
			});
			jrb.setLocation(x, NEW_WINDOW_HEIGHT * 3 / 5);
			jrb.setText(stp.next());
			jrb.setSize(90, 20);
			jrb.setSelected(firstRadioIsChecked);
			firstRadioIsChecked = false;
			g.add(jrb);
			
			x += MOVE_DISTANCE;
			pnlSamples.add(tc);
			pnlSamples.add(jrb);
			
			JButton btnMake = new JButton();
			btnMake.setSize(100, 20);
			btnMake.setLocation(330,145);
			btnMake.setText("Make it!!");
			btnMake.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			pnlSamples.add(btnMake);
		}
		
		//My own taci board
		ownBoard = new NewOwnTaciBoard(TaciUtils.getRandomData());
		ownBoard.setLocation(x, NEW_WINDOW_HEIGHT / 9);
		
		JRadioButton jrbYourOwn = new JRadioButton();
		g.add(jrbYourOwn);
		jrbYourOwn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton object = (JRadioButton) e.getSource();
				if(object.isSelected()) {
					selectedIndex = -1;
				}
			}
		});
		jrbYourOwn.setLocation(x, NEW_WINDOW_HEIGHT * 3 / 5);
		jrbYourOwn.setText("Use me");
		jrbYourOwn.setSize(90, 20);
		
		pnlSamples.add(ownBoard);
		pnlSamples.add(jrbYourOwn);
		
		add(pnlSamples);
		
	}
	
	public TaciBoard getSample() {
		if (selectedIndex != -1) {
			return TaciUtils.copyOfBoard(samples.get(selectedIndex));
		} else {
			try {
				return new TaciBoard(ownBoard.getData());
			} catch (DataNotSuitableException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * This class is just only used in this new Screen
	 * @author BINH
	 * */
	private class NewOwnTaciBoard extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private List<DragnDropLabel> cells = new ArrayList<DragnDropLabel>();

		public NewOwnTaciBoard(Integer[][] _data) {
			setLayout(null);
			setBorder(BorderFactory.createTitledBorder("Your own"));
			setSize(CELL_SIZE * BOARD_MAXTRIX_SIZE + PADING + 5, CELL_SIZE
					* BOARD_MAXTRIX_SIZE + PADING + 10);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			int x = PADING - 5, y = PADING;
			for (int i = 0; i < BOARD_MAXTRIX_SIZE; i++) {
				for (int j = 0; j < BOARD_MAXTRIX_SIZE; j++) {
					DragnDropLabel dndLbl = new DragnDropLabel(String.valueOf(_data[i][j]));
					dndLbl.setLocation(x, y);
					add(dndLbl);
					cells.add(dndLbl);
					x += CELL_SIZE;
				}
				y += CELL_SIZE;
				x = PADING - 5;
			}
		}
	
		public Integer[][] getData() {
			Integer[][] result = new Integer[BOARD_MAXTRIX_SIZE][BOARD_MAXTRIX_SIZE];
			for(int i = 0;i<BOARD_MAXTRIX_SIZE*BOARD_MAXTRIX_SIZE;i++) {
				String text = cells.get(i).getText();
				result[i / 3][i % 3] = Integer.parseInt("".equals(text) ? "0"
						: text);
			}
			
			return result;
		}
	}
}
