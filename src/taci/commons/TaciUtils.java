package taci.commons;

import static taci.commons.GoalKind.A;
import static taci.commons.GoalKind.B;
import static taci.usercontrol.ReferenceConstants.BOARD_MAXTRIX_SIZE;
import static taci.usercontrol.ReferenceConstants.EMPTY_VALUE;
import static taci.usercontrol.ReferenceConstants.GOAL_TITLE;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BEST_BOARD_BACK_COLOR;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import taci.usercontrol.TaciBoard;
import taci.usercontrol.TaciCell;
import taci.usercontrol.TaciNode;
import taci.usercontrol.exception.DataNotSuitableException;

/**
 * This class holds common functions for Taci. All its functions will be used many times in program.
 * @author HUY
 * @version 1.0 
 * */
public class TaciUtils {
	
	public static GoalKind calculateGoalKind(Integer[][] data) {
		int sum = 0;
		List<Integer> lstCells = new ArrayList<Integer>();
		for(int i = 0;i<data.length;i++) {
			lstCells.addAll(Arrays.asList(data[i]));
		}
		
		for(Integer I : lstCells) {
			if (I != EMPTY_VALUE) {
				for (int i = lstCells.indexOf(I); i < lstCells.size(); i++) {
					if(lstCells.get(i) != EMPTY_VALUE) {
						if(I > lstCells.get(i)) {
							sum++;
						}
					}
				}
			}
		}
		
		if(sum % 2 == 0) {
			return B;
		} else {
			return A;
		}
	}
	
	public static TaciBoard copyOfBoard(TaciBoard tb) {
		try {
			TaciBoard result = new TaciBoard(tb.getData());
			result.setLocation(tb.getLocation());
			result.setTitle(tb.getTitle());
			result.setG(tb.getG());
			result.setTaciNextAvaiableBoards(tb.getTaciNextAvaiableBoards());
			if(result.getTitle().equals(GOAL_TITLE)) {
				result.setBackgroundJ(TACI_CELL_BEST_BOARD_BACK_COLOR);
			} else {
				result.setBackgroundJ(tb.getBackgroundJ());
			}
			return result;
		} catch (DataNotSuitableException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isContain(List<TaciNode> openedNodes, TaciNode node) {
		for (TaciNode t : openedNodes) {
			if(same(t, node)) {
				return true;
			}
		}
		return false;
	}

	public static boolean same(TaciBoard t, TaciBoard tb) {
		for (int i = 0; i < BOARD_MAXTRIX_SIZE; i++) {
			for (int j = 0; j < BOARD_MAXTRIX_SIZE; j++) {
				if (!t.getData()[i][j].equals(tb.getData()[i][j])) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean same(TaciNode tn1, TaciNode tn2) {
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			if (!tn1.getData()[i / BOARD_MAXTRIX_SIZE][i % BOARD_MAXTRIX_SIZE]
					.equals(tn2.getData()[i / BOARD_MAXTRIX_SIZE][i
							% BOARD_MAXTRIX_SIZE])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get TaciBoard from the list of taciboard by g. 
	 * If some boards have the same g, the first one will be returned.
	 * If there is no TaciBoard with g, return null value.
	 * @param lstBoards the list of taci boards.  
	 * @param g find the taci board which has the g.
	 * @return {@link TaciBoard} 
	 * */
	public static TaciBoard getBoardbyG(List<TaciBoard>lstBoards, int g) {
		for(TaciBoard tb : lstBoards) {
			if(tb.getG() == g) {
				return tb;
			}
		}
		
		return null;
	}
	
	public static boolean isIn(List<TaciBoard> lstBoards, TaciBoard tb) {
		for(TaciBoard t : lstBoards) {
			if(same(t, tb)) {
				return true;
			}
		}
		
		return false;
	}

	public static Integer[][] getRandomData() {
		Integer[][] data = new Integer[BOARD_MAXTRIX_SIZE][BOARD_MAXTRIX_SIZE];
		List<Integer> rawData = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
		Collections.shuffle(rawData);
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			data[i / 3][i % 3] = rawData.get(i);
		}
		
		return data;
	}
	
	public static TaciCell getCellByIndex(List<TaciCell> cells, Point index) {
		for (TaciCell tc : cells) {
			if (tc.getIndex().distance(index) == 0) {
				return tc;
			}
		}

		return null;
	}

	public static Point getIndexOf(Integer[][] data, Integer value) {
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			if (data[i / BOARD_MAXTRIX_SIZE][i % BOARD_MAXTRIX_SIZE].equals(value)) {
				return new Point(i / BOARD_MAXTRIX_SIZE, i % BOARD_MAXTRIX_SIZE);
			}
		}

		return null;
	}
}
