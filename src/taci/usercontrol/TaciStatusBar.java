package taci.usercontrol;

import static taci.usercontrol.ReferenceConstants.CELL_SIZE;
import static taci.usercontrol.ReferenceConstants.J_HEIGHT;
import static taci.usercontrol.ReferenceConstants.J_WIDTH;
import static taci.usercontrol.ReferenceConstants.LEGEND_TC_BEST_BOARD;
import static taci.usercontrol.ReferenceConstants.LEGEND_TC_DIED_BOARD;
import static taci.usercontrol.ReferenceConstants.LEGEND_TC_HAVE_NEXT_AVAILABLE_MOVEMENT;
import static taci.usercontrol.ReferenceConstants.MOVE_DISTANCE;
import static taci.usercontrol.ReferenceConstants.STATUS;
import static taci.usercontrol.ReferenceConstants.STATUS_BAR_CLOSED_NUMBER;
import static taci.usercontrol.ReferenceConstants.STATUS_BAR_OPENED_NUMBER;
import static taci.usercontrol.ReferenceConstants.STATUS_BAR_STEPS_NUMBER;
import static taci.usercontrol.ReferenceConstants.STATUS_BAR_TIME_COST;
import static taci.usercontrol.ReferenceConstants.STATUS_BAR_TITLE;
import static taci.usercontrol.ReferenceConstants.STATUS_SWAPING_SPEED;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BACK_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BEST_BOARD_BACK_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BOARD_HAS_NEXT_BACK_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BORDER_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_FORE_COLOR;
import static taci.usercontrol.ReferenceConstants.*;

import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taci.program.TaciProgram;

/**
 * Status bar which inform close, open and number of step from the initial state to goal state.
 * It also have the legend for Taci program.
 * @author BINH
 * @version 1.0
 * */
public class TaciStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int X_LABEL = 20;
	private static final int X_LABEL_DELTA = 100;
	private TaciBoard goal;
	/*private JLabel jlbOpens;
	private JLabel jlbCloses;*/
	private JLabel jlbTimeCost;
	private JLabel jlbSteps;
	private JLabel jlbCurrentStatus;
	//private JLabel jlbCurrentSwappingSpeed;
	
	//Legend
	/*private TaciCell tcHavingNextAvailableMovement; //The board have next movable boards 
	private TaciCell tcDiedBoard;//The board has no next movable boards
	private TaciCell tcBestBoard;//The board has next movable boards and is the best board for each step
	private JLabel jlbTcHavingNextAvailableMovement;
	private JLabel jlbTcDiedBoard;
	private JLabel jlbTcBestBoard;*/
	
	public TaciStatusBar() {
		setSize(WIDTH_SCREEN - 15, MOVE_DISTANCE * 3/2);
		setBorder(BorderFactory.createTitledBorder(STATUS_BAR_TITLE));
		setLayout(null);
		setLocation(0, HEIGHT_SCREEN - MOVE_DISTANCE * 6 / 3);

		/*jlbCloses = new JLabel();
		jlbCloses.setSize(200, 20);
		jlbCloses.setLocation(X_LABEL, 20);
		jlbCloses.setText(String.format(STATUS_BAR_CLOSED_NUMBER, 0));

		jlbOpens = new JLabel();
		jlbOpens.setSize(200, 20);
		jlbOpens.setLocation(X_LABEL, 60);
		jlbOpens.setText(String.format(STATUS_BAR_OPENED_NUMBER, 0));*/

		jlbTimeCost = new JLabel();
		jlbTimeCost.setSize(200, 20);
		jlbTimeCost.setLocation(X_LABEL + X_LABEL_DELTA, 20);
		jlbTimeCost.setText(String.format(STATUS_BAR_TIME_COST, 0.0));

		jlbSteps = new JLabel();
		jlbSteps.setSize(200, 20);
		jlbSteps.setLocation(X_LABEL + X_LABEL_DELTA, 60);
		jlbSteps.setText(String.format(STATUS_BAR_STEPS_NUMBER, 0));
		
		jlbCurrentStatus = new JLabel();
		jlbCurrentStatus.setSize(400, 20);
		jlbCurrentStatus.setLocation(X_LABEL + X_LABEL_DELTA, 100);
		jlbCurrentStatus.setText(String.format(STATUS, TaciProgram.READY));
		
		/*jlbCurrentSwappingSpeed = new JLabel();
		jlbCurrentSwappingSpeed.setSize(400, 20);
		jlbCurrentSwappingSpeed.setLocation(X_LABEL + X_LABEL_DELTA * 4, 60);
		jlbCurrentSwappingSpeed.setText(String.format(STATUS_SWAPING_SPEED, "Normal"));//By default, it is normal speed
		
		tcBestBoard = new TaciCell(CELL_SIZE, TACI_CELL_FORE_COLOR, new Point(
				200 + MOVE_DISTANCE * 7, 20), null, 0);
		tcBestBoard.setOpaque(true);
		tcBestBoard.setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));
		tcBestBoard.setBackground(TACI_CELL_BEST_BOARD_BACK_COLOR);
		
		tcDiedBoard = new TaciCell(CELL_SIZE, TACI_CELL_FORE_COLOR, new Point(
				200 + MOVE_DISTANCE * 7, 45), null, 0);
		tcDiedBoard.setOpaque(true);
		tcDiedBoard.setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));
		tcDiedBoard.setBackground(TACI_CELL_BACK_COLOR);
		
		tcHavingNextAvailableMovement = new TaciCell(CELL_SIZE, TACI_CELL_FORE_COLOR, new Point(
				200 + MOVE_DISTANCE * 7, 70), null, 0);
		tcHavingNextAvailableMovement.setOpaque(true);
		tcHavingNextAvailableMovement.setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));
		tcHavingNextAvailableMovement.setBackground(TACI_CELL_BOARD_HAS_NEXT_BACK_COLOR);
		
		jlbTcHavingNextAvailableMovement = new JLabel();
		jlbTcHavingNextAvailableMovement.setSize(400, 20);
		jlbTcHavingNextAvailableMovement.setLocation(200 + MOVE_DISTANCE * 7 + CELL_SIZE + 5, 20);
		jlbTcHavingNextAvailableMovement.setText(LEGEND_TC_BEST_BOARD);
		
		jlbTcBestBoard = new JLabel();
		jlbTcBestBoard.setSize(400, 20);
		jlbTcBestBoard.setLocation(200 + MOVE_DISTANCE * 7 + CELL_SIZE + 5, 45);
		jlbTcBestBoard.setText(LEGEND_TC_DIED_BOARD);
		
		jlbTcDiedBoard = new JLabel();
		jlbTcDiedBoard.setSize(400, 20);
		jlbTcDiedBoard.setLocation(200 + MOVE_DISTANCE * 7 + CELL_SIZE + 5, 70);
		jlbTcDiedBoard.setText(LEGEND_TC_HAVE_NEXT_AVAILABLE_MOVEMENT);*/
		
		//add(jlbCloses);
		//add(jlbOpens);
		add(jlbTimeCost);
		add(jlbSteps);
		add(jlbCurrentStatus);
		//add(jlbCurrentSwappingSpeed);
		//add(tcBestBoard);
		//add(tcDiedBoard);
		//add(tcHavingNextAvailableMovement);
		//add(jlbTcBestBoard);
		//add(jlbTcDiedBoard);
		//add(jlbTcHavingNextAvailableMovement);
	}

	public void setGoal(TaciBoard newGoal) {
		if (this.goal == null) {
			this.goal = newGoal;
			add(newGoal);
			newGoal.updateUI();
		} 
		remove(this.goal);
		this.goal = newGoal;
		add(newGoal);
		newGoal.updateUI();
	}

	/*public void setOpens(int open) {
		this.jlbOpens.setText(String.format(STATUS_BAR_OPENED_NUMBER, open));
	}*/

	/*public void setCloses(int close) {
		this.jlbCloses.setText(String.format(STATUS_BAR_CLOSED_NUMBER, close));
	}*/

	public void setTimeCost(float timeCost) {
		this.jlbTimeCost.setText(String.format(STATUS_BAR_TIME_COST, timeCost));
	}

	public void setSteps(int step) {
		this.jlbSteps.setText(String.format(STATUS_BAR_STEPS_NUMBER, step));
	}

	public void setCurrentStatus(String status) {
		this.jlbCurrentStatus.setText(String.format(STATUS, status));
	}

	/*public void setCurrentSwappingSpeed(String swappingSpeed) {
		this.jlbCurrentSwappingSpeed.setText(String.format(STATUS_SWAPING_SPEED, swappingSpeed));
	}*/
}
