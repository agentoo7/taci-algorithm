package taci.usercontrol;

import static taci.usercontrol.ReferenceConstants.BOARD_MAXTRIX_SIZE;
import static taci.usercontrol.ReferenceConstants.EMPTY_VALUE;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import taci.commons.GoalKind;
import taci.commons.TaciUtils;

/**
 * This is equivalent to {@link TaciBoard} class but it is the reduced version.
 * This is used for automation mode which does not to tracks all opened boards.
 * @author HUY
 * @version 1.0
 * */
public class TaciNode {
	private TaciNode previous;
	
	private Integer[][] data;
	private Integer[][] goal;
	private GoalKind kind; 
	private int g; //The number of step is over
	public static Stack<Way> directions = new Stack<Way>();
	
	public TaciNode(Integer[][] _data) {
		setData(_data);
		previous = null;
		kind = TaciUtils.calculateGoalKind(_data);
		if(kind == GoalKind.A) {
			goal = ReferenceConstants.GOAL_DATA_A;
		} else {
			goal = ReferenceConstants.GOAL_DATA_B;
		}
		g = 0;
	}
	
	public List<TaciNode> moveNext() {
		List<TaciNode> nextAvailableNode = new ArrayList<TaciNode>();
		for (Point p : getMovableIndices()) {
			TaciNode tn = new TaciNode(swap(p));
			tn.previous = this;
			// Increase g
			tn.g = g + 1;
			
			nextAvailableNode.add(tn);
		}
		
		return nextAvailableNode;
	}
	
	public Point getEmptyIndex() {
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			if (getData()[i / BOARD_MAXTRIX_SIZE][i % BOARD_MAXTRIX_SIZE]
					.equals(EMPTY_VALUE)) {
				return new Point(i / BOARD_MAXTRIX_SIZE, i % BOARD_MAXTRIX_SIZE);
			}
		}

		return null;
	}
	
	private List<Point> getMovableIndices() {
		Point emptyIndex = getEmptyIndex();
		List<Point> results = new ArrayList<Point>();
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			if ((i / BOARD_MAXTRIX_SIZE) == emptyIndex.x) {// Case 1, in same column but different row
				if (Math.abs((i % BOARD_MAXTRIX_SIZE) - emptyIndex.y) == 1) {
					results.add(new Point(i / BOARD_MAXTRIX_SIZE, i
							% BOARD_MAXTRIX_SIZE));
				}
			}
			if ((i % BOARD_MAXTRIX_SIZE) == emptyIndex.y) {// case 2, in same row but different column
				if (Math.abs((i / BOARD_MAXTRIX_SIZE) - emptyIndex.x) == 1) {
					results.add(new Point(i / BOARD_MAXTRIX_SIZE, i
							% BOARD_MAXTRIX_SIZE));
				}
			}
		}
		
		return results;
	}

	//Do swap between the needed index and empty index
	private Integer[][] swap(Point index) {
		Integer[][] newData = new Integer[BOARD_MAXTRIX_SIZE][BOARD_MAXTRIX_SIZE];
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			newData[i / BOARD_MAXTRIX_SIZE][i % BOARD_MAXTRIX_SIZE] = getData()[i
					/ BOARD_MAXTRIX_SIZE][i % BOARD_MAXTRIX_SIZE];
		}
		//Swap
		Point emptyCellIndex = getEmptyIndex();
		newData[emptyCellIndex.x][emptyCellIndex.y] = newData[index.x][index.y]; 
		newData[index.x][index.y] = EMPTY_VALUE;
		
		return newData;
	}

	public void setData(Integer[][] data) {
		this.data = data;
	}

	public Integer[][] getData() {
		return data;
	}
	
	public int getH() {
		int h = 0;
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			Point currentIndex = new Point(i / BOARD_MAXTRIX_SIZE, i % BOARD_MAXTRIX_SIZE);
			Point goalIndex = TaciUtils.getIndexOf(goal, data[i / BOARD_MAXTRIX_SIZE][i % BOARD_MAXTRIX_SIZE]);
			h += Math.abs(currentIndex.x - goalIndex.x)
					+ Math.abs(currentIndex.y - goalIndex.y);
		}
		return h;
	}

	public int getG() {
		return g;
	}

	public TaciNode getPre() {
		return previous;
	}
}
