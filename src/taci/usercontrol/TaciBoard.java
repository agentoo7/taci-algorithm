package taci.usercontrol;

import static taci.commons.GoalKind.A;
import static taci.commons.GoalKind.B;
import static taci.commons.TaciUtils.calculateGoalKind;
import static taci.usercontrol.ReferenceConstants.BOARD_MAXTRIX_SIZE;
import static taci.usercontrol.ReferenceConstants.CELL_SIZE;
import static taci.usercontrol.ReferenceConstants.EMPTY_VALUE;
import static taci.usercontrol.ReferenceConstants.GOAL_A;
import static taci.usercontrol.ReferenceConstants.GOAL_B;
import static taci.usercontrol.ReferenceConstants.GOAL_DATA_A;
import static taci.usercontrol.ReferenceConstants.GOAL_DATA_B;
import static taci.usercontrol.ReferenceConstants.GOAL_TITLE;
import static taci.usercontrol.ReferenceConstants.INITIAL_LEFT_BOARD;
import static taci.usercontrol.ReferenceConstants.J_WIDTH;
import static taci.usercontrol.ReferenceConstants.MOVE_DISTANCE;
import static taci.usercontrol.ReferenceConstants.NEXT_TITLE;
import static taci.usercontrol.ReferenceConstants.PADING;
import static taci.usercontrol.ReferenceConstants.RIGHT_BOARD_PADING;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BACK_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BORDER_COLOR;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_FORE_COLOR;
import static taci.usercontrol.ReferenceConstants.TOOLTIP_BACKGROUND_COLOR;
import static taci.usercontrol.ReferenceConstants.TOOLTIP_BORDER_COLOR;
import static taci.usercontrol.ReferenceConstants.TOOLTIP_FORE_COLOR;
import static taci.usercontrol.ReferenceConstants.TOOL_TIP_BOARD_TEMPLATE;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import taci.commons.Direction;
import taci.commons.GoalKind;
import taci.commons.TaciUtils;
import taci.program.TaciPopup;
import taci.usercontrol.exception.DataNotSuitableException;

/**
 * The board with collection of {@link TaciCell}.
 * @author HUY
 * @version 1.0
 * */
public class TaciBoard extends JPanel implements Cloneable {
	private static final long serialVersionUID = 1L;
	
	private Integer[][] data;
	private List<TaciCell> taciCells = new ArrayList<TaciCell>();
	private static TaciBoard instance;
	private GoalKind kind; 
	private int g; //The number of step is over
	private TaciPopup taciPopup;
	private List<TaciBoard> taciNextAvaiableBoards = new ArrayList<TaciBoard>();
	
	private TaciBoard previous;
	private boolean inScreen = false;
	private static List<TaciCell> goal;
	
	public static Stack<Way> directions = new Stack<Way>();
	
	private TaciBoard() {
		setBorder(BorderFactory.createTitledBorder(""));
		setLayout(null);
		setVisible(true);
	}
	
	public static TaciBoard getGoalTaciBoard(GoalKind kind) {
		if (kind == A) {
			instance = new TaciBoard();
			instance.setTaciCells(GOAL_A);
			for (TaciCell tc : GOAL_A) {
				instance.kind = A;
				instance.data = GOAL_DATA_A;
				instance.add(tc);
			}
		} else {
			instance = new TaciBoard();
			instance.setTaciCells(GOAL_B);
			for (TaciCell tc : GOAL_B) {
				instance.kind = B;
				instance.data = GOAL_DATA_B;
				instance.add(tc);
			}
		}
		instance.setSize();

		return instance;
	}
	
	public TaciBoard(Integer[][] _data) throws DataNotSuitableException {
		if (_data == null) {
			throw new DataNotSuitableException(0, 0);
		}
		if (_data.length != _data[0].length) {
			throw new DataNotSuitableException(_data.length, _data[0].length);
		}
		if (_data.length < BOARD_MAXTRIX_SIZE) {
			throw new DataNotSuitableException(_data.length, _data.length);
		}
		
		this.data = _data;
		//Set kind of goal of this puzzle
		kind = calculateGoalKind(_data);
		if(kind == A) {
			goal = GOAL_A;
		} else {
			goal = GOAL_B;
		}
		makeupBoard(_data);
		setLayoutAndEvent();
	}
	
	/**
	 * Make the board well-layouted and also add the event for this board.
	 * */
	private void setLayoutAndEvent() {
		setLayout(null);
		setBorder(BorderFactory.createTitledBorder(""));
		setSize();
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		//setDoubleBuffered(true);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if(getTaciNextAvaiableBoards()!= null && getTaciNextAvaiableBoards().size() != 0) {
							taciPopup = new TaciPopup(getTaciNextAvaiableBoards());
							taciPopup.setVisible(true);
						}
					}
				});
			}
		});
	}
	
	/**
	 * Initialize the board with the input data
	 * @param _data the two-dimensioned array.
	 */
	private void makeupBoard(Integer[][] _data) {
		int x = PADING - 5, y = PADING;
		for (int i = 0; i < _data.length; i++) {
			for (int j = 0; j < _data.length; j++) {
				Point location = new Point(x, y);
				Point index = new Point(i, j); 
				TaciCell tc = new TaciCell(CELL_SIZE, TACI_CELL_FORE_COLOR,
						location, index, _data[i][j]);
				tc.setBackground(TACI_CELL_BACK_COLOR);
				tc.setOpaque(true);
				tc.setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));
				//set the target index for each cell
				tc.setGoalIndex(getGoalIndexOf(_data[i][j]));
				//Calculate the G, H value
				g = 0;
				tc.setH(calculateH(tc));
				add(tc);
				getTaciCells().add(tc);
				x += CELL_SIZE;
			}
			y += CELL_SIZE;
			x = PADING - 5;
		}
	}
	
	/**
	 * Set the tool tip for the board.
	 * The tool tip shows basically about the f, g, and also h.
	 * */
	private void setToolTip() {
		int h = getH();
		setToolTipText(String.format(TOOL_TIP_BOARD_TEMPLATE, g, h, g + h));
	}

	private Point getGoalIndexOf(Integer integer) {
		return goal.get(integer).getIndex();
	}

	private int calculateH(TaciCell tc) {
		Point currentIndex = tc.getIndex();
		Point goalIndex = tc.getGoalIndex();
		if(currentIndex.distance(goalIndex) == 0) {
			return 0;
		}
		
		return Math.abs(currentIndex.x - goalIndex.x)
				+ Math.abs(currentIndex.y - goalIndex.y);
	}

	private void setSize() {
		setSize(CELL_SIZE * BOARD_MAXTRIX_SIZE + PADING + 5, CELL_SIZE
				* BOARD_MAXTRIX_SIZE + PADING + 10);
	}
	
	public void setTitle(String title) {
		((TitledBorder)getBorder()).setTitle(title);
	}
	
	public String getTitle() {
		return ((TitledBorder)getBorder()).getTitle();
	}
		
	public List<TaciBoard> moveNext() throws DataNotSuitableException {
		TaciCell emptyCell = getEmptyCell();
		List<TaciBoard> nextAvailableBoard = new ArrayList<TaciBoard>();
		//TODO: keep track for all movable case here
		for (TaciCell t : getMovableCells()) {
			TaciBoard tb = new TaciBoard(swap(t, emptyCell));
			int newxLoc = getLocation().x + MOVE_DISTANCE;
			int newyLoc = getLocation().y;
			if (newxLoc > J_WIDTH - RIGHT_BOARD_PADING) {
				newxLoc = INITIAL_LEFT_BOARD;
				newyLoc += MOVE_DISTANCE;
			}
			tb.setPre(this);
			tb.setLocation(newxLoc, newyLoc);
			// Increase g
			tb.g = g + 1;
			if (tb.getH() != 0) {
				tb.setTitle(NEXT_TITLE + tb.g);
			} else {
				tb.setTitle(GOAL_TITLE); // Get goal state
			}
			
			nextAvailableBoard.add(tb);
		}
		return nextAvailableBoard;
	}
	
	/**
	 * Keep tracks of all opened board for this current board
	 * */
	public void addAvailableMovements(TaciBoard board) {
		taciNextAvaiableBoards.add(board);
	}

	public void setBackgroundJ(Color bg) {
		if (this.getTaciCells() != null) {
			for (TaciCell tc : getTaciCells()) {
				if (tc.getValue().equals(EMPTY_VALUE)) {
					tc.setBackground(ReferenceConstants.TACI_EMPTY_CELL_BACK_COLOR);
				} else {
					tc.setBackground(bg);
				}
			}
		}
	}
	
	public Color getBackgroundJ() {
		if(this.getTaciCells() != null && this.getTaciCells().size() != 0) {
			return this.getTaciCells().get(1).getBackground();
		}
		
		return TACI_CELL_BACK_COLOR;
	}

	private Integer[][] swap(TaciCell cell, TaciCell empty) {
		Integer[][] newData = new Integer[BOARD_MAXTRIX_SIZE][BOARD_MAXTRIX_SIZE];
		for (int i = 0; i < BOARD_MAXTRIX_SIZE * BOARD_MAXTRIX_SIZE; i++) {
			newData[i / 3][i % 3] = data[i / 3][i % 3];
		}
		Point cellIndex = cell.getIndex();
		Point emptyCellIndex = empty.getIndex();
		newData[emptyCellIndex.x][emptyCellIndex.y] = newData[cellIndex.x][cellIndex.y];
		newData[cellIndex.x][cellIndex.y] = EMPTY_VALUE;
		
		return newData;
	}
	
	@Override
	public JToolTip createToolTip() {
		JToolTip jToolTips =  super.createToolTip();
		jToolTips.setBorder(BorderFactory.createLineBorder(TOOLTIP_BORDER_COLOR));
		jToolTips.setBackground(TOOLTIP_BACKGROUND_COLOR);
		jToolTips.setForeground(TOOLTIP_FORE_COLOR);
		
		return jToolTips;
	}
	
	@Override
	public boolean contains(int x, int y) {
		Point emptyCellLoc = getEmptyCell().getLocation();
		Rectangle rect = new Rectangle(emptyCellLoc, new Dimension(CELL_SIZE,
				CELL_SIZE));
		if(rect.contains(x, y)) {
			return false;
		}
		setToolTip();
		return super.contains(x, y);
	}
	
	public int getH() {
		int h = 0;
		for(TaciCell tc : getTaciCells()) {
			h += tc.getH();
		}
		
		return h;
	}

	public int getG() {
		return g;
	}
	
	public void setG(int _g) {
		this.g = _g;
	}
	
	public TaciCell getEmptyCell() {
		for(TaciCell tc : getTaciCells()) {
			if(tc.getValue() == EMPTY_VALUE) {
				return tc;
			}
		}
		return null;
	}
	
	private List<TaciCell> getMovableCells() {
		TaciCell cell = getEmptyCell();
		List<TaciCell> results = new ArrayList<TaciCell>();
		Point cellIndex = cell.getIndex();
		for(TaciCell tc : getTaciCells()) {
			Point index = tc.getIndex();
			if(index.x == cellIndex.x) {//Case 1, in same column but different row
				if(Math.abs(index.y - cellIndex.y) == 1) {
					results.add(tc);
				}
			}
			if(index.y == cellIndex.y) {//case 2, in same row but different column
				if(Math.abs(index.x - cellIndex.x) == 1) {
					results.add(tc);
				}
			}
		}
		return results;
	}

	public List<TaciBoard> getTaciNextAvaiableBoards() {
		return taciNextAvaiableBoards;
	}
	
	public void setTaciCells(List<TaciCell> taciCells) {
		this.taciCells = taciCells;
	}

	public List<TaciCell> getTaciCells() {
		return taciCells;
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
	}

	

	public Integer[][] getData() {
		return data;
	}

	public void setTaciNextAvaiableBoards(List<TaciBoard> taciNextAvaiableBoards) {
		this.taciNextAvaiableBoards = taciNextAvaiableBoards;
	}

	public void setPre(TaciBoard pre) {
		this.previous = pre;
	}

	public TaciBoard getPre() {
		return previous;
	}

	public boolean isInScreen() {
		return inScreen;
	}

	public void setInScreen(boolean inScreen) {
		this.inScreen = inScreen;
	}
	
	/**
	 * Play the animation from the start state to the goal state.
	 * Whole of the time 
	 * @throws InterruptedException when thread is sleeping, occupying
	 * */
	public void doAutomaticSolution() {
		
				while (!directions.isEmpty()) {
					Way way = directions.pop();
					TaciCell cell = TaciUtils.getCellByIndex(taciCells,
							way.getIndex());
					Point loc = cell.getLocation();
					TaciCell emptyCell = getEmptyCell();
					Direction dir = way.getDir();
					while (loc.distance(emptyCell.getLocation()) != 0) {
						doAnimation(dir, cell, emptyCell);
						try {
							Thread.sleep(ReferenceConstants.SWAP_DELAY_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					data[emptyCell.getIndex().x][emptyCell.getIndex().y] = data[cell
							.getIndex().x][cell.getIndex().y];
					data[cell.getIndex().x][cell.getIndex().y] = EMPTY_VALUE;
					Point temp = new Point(emptyCell.getIndex().x,
							emptyCell.getIndex().y);
					emptyCell.setIndex(new Point(cell.getIndex().x, cell
							.getIndex().y));
					cell.setIndex(temp);
				}
				
				//Update h for all TaciBoard 
				for(TaciCell tc : getTaciCells()) {
					tc.setH(0);
				}
	}

	private void doAnimation(Direction dir, TaciCell cell, TaciCell emptyCell) {
		switch (dir) {
		case EAST:
			cell.setLocation(cell.getLocation().x + 1, cell.getLocation().y);
			emptyCell.setLocation(emptyCell.getLocation().x - 1,
					emptyCell.getLocation().y);
			break;
		case WEST:
			cell.setLocation(cell.getLocation().x - 1, cell.getLocation().y);
			emptyCell.setLocation(emptyCell.getLocation().x + 1,
					emptyCell.getLocation().y);
			break;
		case SOUTH:
			cell.setLocation(cell.getLocation().x, cell.getLocation().y + 1);
			emptyCell.setLocation(emptyCell.getLocation().x,
					emptyCell.getLocation().y - 1);
			break;
		case NORTH:
			cell.setLocation(cell.getLocation().x, cell.getLocation().y - 1);
			emptyCell.setLocation(emptyCell.getLocation().x,
					emptyCell.getLocation().y + 1);
			break;
		}
	}
}