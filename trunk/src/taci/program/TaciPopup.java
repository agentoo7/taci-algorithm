package taci.program;

import static taci.usercontrol.ReferenceConstants.*;
import static taci.usercontrol.ReferenceConstants.J_WIDTH;
import static taci.usercontrol.ReferenceConstants.MAX_POPUP_HEIGHT;
import static taci.usercontrol.ReferenceConstants.MAX_POPUP_WIDTH;
import static taci.usercontrol.ReferenceConstants.MOVE_DISTANCE;
import static taci.usercontrol.ReferenceConstants.RIGHT_BOARD_PADING;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BEST_BOARD_BACK_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BOARD_HAS_NEXT_BACK_COLOR;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JDialog;

import taci.usercontrol.TaciBoard;

/**
 * Pop-up for showing the detail per step.
 * @author HUY
 * @version 1.0 
 * */
public class TaciPopup extends JDialog {
	
	public TaciPopup(List<TaciBoard> taciBoards) {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		for (TaciBoard t : taciBoards) {
			if (t.getBackgroundJ().equals(TACI_CELL_BEST_BOARD_BACK_COLOR)) {
				setTitle("Step detail#" + t.getG());
				break;
			}
		}
		setResizable(false);
		setLayout(null);
		setSize(10, MAX_POPUP_HEIGHT);
		if(taciBoards.size() > 10) {
			setLocation(NEW_X_LOC_WHEN_FULL, J_HEIGHT / 4);
		} else {
			setLocation(J_WIDTH / 4, J_HEIGHT / 4);
		}
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});
		
		int newxLoc = 0;
		int newyLoc = 0;
		for (TaciBoard tb : taciBoards){
			if (newxLoc > MAX_POPUP_WIDTH - RIGHT_BOARD_PADING) {
				newxLoc = 0;
				newyLoc += MOVE_DISTANCE;
				setSize(getSize().width, getSize().height + MOVE_DISTANCE);
				if(getSize().height > J_HEIGHT - J_HEIGHT/4) {
					setLocation(NEW_X_LOC_WHEN_FULL, NEW_Y_LOC_WHEN_FULL);
				}
			}
			tb.setLocation(newxLoc, newyLoc);
			newxLoc += MOVE_DISTANCE;
			//Set popup size-width
			if(getSize().width <= MAX_POPUP_WIDTH) {
				setSize(getSize().width + MOVE_DISTANCE, getSize().height);
			}
			if(tb.getTaciNextAvaiableBoards().size() != 0) {
				if(!tb.isInScreen())
				{
					tb.setBackgroundJ(TACI_CELL_BOARD_HAS_NEXT_BACK_COLOR);
				}
			}
			add(tb);
		}
		
	}
	
	private static final long serialVersionUID = 1L;	
}
