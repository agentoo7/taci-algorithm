package taci.usercontrol;

import static taci.usercontrol.ReferenceConstants.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import taci.program.TaciProgram;

/**
 * Status bar which inform close, open and number of step from the initial state to goal state.
 * It also have the legend for Taci program.
 * @author HUY
 * @version 1.0
 * */
public class TaciStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int X_LABEL = 20;
	private static final int X_LABEL_DELTA = 100;
	private TaciBoard goal;
	private JLabel jlbTimeCost;
	private JLabel jlbSteps;
	private JLabel jlbCurrentStatus;
	
	public TaciStatusBar() {
		setSize(WIDTH_SCREEN - 15, MOVE_DISTANCE * 3/2);
		setBorder(BorderFactory.createTitledBorder(STATUS_BAR_TITLE));
		setLayout(null);
		setLocation(0, HEIGHT_SCREEN - MOVE_DISTANCE * 6 / 3);

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
		
		add(jlbTimeCost);
		add(jlbSteps);
		add(jlbCurrentStatus);
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

	public void setTimeCost(float timeCost) {
		this.jlbTimeCost.setText(String.format(STATUS_BAR_TIME_COST, timeCost));
	}

	public void setSteps(int step) {
		this.jlbSteps.setText(String.format(STATUS_BAR_STEPS_NUMBER, step));
	}

	public void setCurrentStatus(String status) {
		this.jlbCurrentStatus.setText(String.format(STATUS, status));
	}
}
