package taci.program;

import static taci.usercontrol.ReferenceConstants.*;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import taci.commons.Direction;
import taci.commons.GoalKind;
import taci.commons.TaciUtils;
import taci.usercontrol.TaciBoard;
import taci.usercontrol.TaciNode;
import taci.usercontrol.TaciStatusBar;
import taci.usercontrol.Way;
import taci.usercontrol.exception.DataNotSuitableException;
import taci.usercontrol.exception.TargetUnreachableException;

/**
 * Main class for this program. It holds all functions and actions for this Taci program.<br/>
 * Why not use the {@link PriorityQueue} to save all opened board.
 * @author HUY
 * @version 1.0
 * */
public class TaciProgram extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final String READY = "Sẵn sàng";
	public static final String SWAPPING = "Đang thực hiện ...";
	public static final String CALCULATING = "Đang tính toán ...";
	
	/*For step by step mode*/
	private List<TaciBoard> screenBoards = new ArrayList<TaciBoard>();
	private List<TaciBoard> openedBoards = new ArrayList<TaciBoard>();
	private List<TaciBoard> closedBoards = new ArrayList<TaciBoard>();
	
	private TaciBoard initial;
	private TaciBoard goal;
	
	//private List<TaciBoard> samples;
	private TaciStatusBar pnlStatus = new TaciStatusBar();
	
	private int opens = 0;
	/*For automation mode*/
	private TaciNode initialNode;
	private List<TaciNode> openedNodes = new ArrayList<TaciNode>();
	private List<TaciNode> closedNodes = new ArrayList<TaciNode>();
	private int step = 0;
	
	
	private JMenuItem fileNew = new JMenuItem("Tạo mới");
	private JMenuItem jmRun = new JMenuItem("Chạy");
	//private JMenuItem jmSwapWhenEqual = new JCheckBoxMenuItem("Swap when equal", true);
	
	public TaciProgram() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH_SCREEN, HEIGHT_SCREEN);
		setLayout(null);
		setTitle("Chương trình mô phỏng giải bài toán Taci - Nguyễn Công Huy");
		setResizable(RESIZABLE);
		
		//createNewPopupData();
	
		//Create the menu
		JMenuBar menubar = new JMenuBar();
	    JMenu jmFile = new JMenu("Tệp");
	    jmFile.setMnemonic('T');
	    jmFile.add(new JSeparator());
	    
	    jmRun.setEnabled(false);
	    jmRun.setMnemonic('C');
	    jmRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final long start = System.currentTimeMillis();
				Thread th = new Thread() {
					public void run() {
						fileNew.setEnabled(false);
						jmRun.setEnabled(false);
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						updateStatus(0, 0, 0.0f, 0,
								CALCULATING, SWAP_DELAY_TIME);
						
						try {
							automaticAction();
						} catch (DataNotSuitableException e) {
							JOptionPane.showMessageDialog(
									null,
									String.format(
											"Please check your input data, be sure that the size is %d!",
											BOARD_MAXTRIX_SIZE));
							e.printStackTrace();
						} catch (TargetUnreachableException e) {
							JOptionPane.showMessageDialog(
									null,
									e.getMessage());
							e.printStackTrace();
						}
						
						final float time = (float) (System.currentTimeMillis() - start) / 1000; 
						updateStatus(opens, closedNodes.size(), time, step,
								SWAPPING, SWAP_DELAY_TIME); //Subtract the initial and goal state
						
						initial.doAutomaticSolution();
						initial.setTitle(GOAL_TITLE); //After swapping, the initial become goal
						initial.updateUI();
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						updateStatus(opens, closedNodes.size(), time, step,
								READY, SWAP_DELAY_TIME);
						fileNew.setEnabled(true);
					}
				};

				th.start();
				
			}
		});
	    
	    fileNew.setMnemonic('n');
	    fileNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doClean();
				//New Popup
				TaciNewSession newScreen = new TaciNewSession();
				newScreen.setModal(true);
				newScreen.setLocation(J_WIDTH/2 - NEW_WINDOW_WIDTH/2 , J_HEIGHT/2 - NEW_WINDOW_HEIGHT/2);
				newScreen.setVisible(true);
				
				initial = newScreen.getSample();
				initial.setTitle(INITIAL_TITLE);
				initial.setLocation(INITIAL_LEFT_BOARD, INITIAL_TOP_BOARD);
				add(initial);
				initial.updateUI();
				open(initial);
				screenBoards.add(initial);
				
				//Set initialization for automatic mode
				initialNode = new TaciNode(newScreen.getSample().getData());
				open(initialNode);
				
				GoalKind kind = TaciUtils.calculateGoalKind(initial.getData());
				goal = TaciBoard.getGoalTaciBoard(kind);
				goal.setTitle(GOAL_TITLE);
				goal.setLocation(20, 20);
				
				pnlStatus.setGoal(goal);
				updateStatus(0, 0, 0.0f, 0, READY, SWAP_DELAY_TIME);
			}
		});
	    
	    JMenuItem fileExit = new JMenuItem("Exit");
	    fileExit.setMnemonic('x');
	    fileExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	    
	    
	    jmFile.add(fileNew);
	    jmFile.add(jmRun);
	    jmFile.addSeparator();
	    jmFile.add(fileExit);
	    
	    //author's information menu
	    JMenu jmInfo = new JMenu();
	    jmInfo.setText("Trợ giúp");
	    
	    JMenuItem jmiAbout = new JMenuItem("Về chương trình");
	    jmInfo.add(jmiAbout);
	    jmiAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						JOptionPane.showMessageDialog(null,
								INFORMATION_MESSAGE, "Thông tin",
								JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
		});
	    
	    
	    
	    menubar.add(jmFile);
	    //menubar.add(jmStyle);
	    menubar.add(jmInfo);
	    setJMenuBar(menubar);
	    	    
	    add(pnlStatus);
	    
	    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	
	
	private void doClean() {
		//Enable the menu run
		jmRun.setEnabled(true);
		for(TaciBoard tb : screenBoards) {
			remove(tb);
		}
		
		openedBoards.clear();
		screenBoards.clear();
		closedBoards.clear();
		opens = 0;
		SwingUtilities.updateComponentTreeUI(this);
		
		//Clean for automatic mode
		openedNodes.clear();
		closedNodes.clear();
		step = 0;
		
		/*try {
			createNewPopupData();
		} catch (DataNotSuitableException e) {
			e.printStackTrace();
		}*/
	}
	
	private void updateStatus(int opens, int close, float f, int step, String status, int speed) {
		pnlStatus.setTimeCost(f);
		pnlStatus.setSteps(step);
		pnlStatus.setCurrentStatus(status);
	}
	
	/*private void createNewPopupData() throws DataNotSuitableException {
		Integer[][] i1 = new Integer[][] { new Integer[] { 3, 2, 1 },
				new Integer[] { 4, 5, 8 }, new Integer[] { 6, 0, 7 }}; //28 steps
		
		Integer[][] i2 = new Integer[][] { new Integer[] { 1, 2, 7 },
				new Integer[] { 3, 5, 8 }, new Integer[] { 4, 6, 0 }}; //21 steps
		
		Integer[][] i3 = new Integer[][] { new Integer[] { 5, 3, 1 },	//17steps
				new Integer[] { 8, 2, 7 }, new Integer[] { 0, 4, 6 }};
		
		Integer[][] i4 = new Integer[][] { new Integer[] { 3, 1, 2 },	//4 step
				new Integer[] { 4, 5, 8 }, new Integer[] { 6, 0, 7 }};
		
		samples = new ArrayList<TaciBoard>();
		
		TaciBoard tb1 = new TaciBoard(i1);
		tb1.setTitle("Sample 1");
		samples.add(tb1);
		
		TaciBoard tb2 = new TaciBoard(i2);
		tb2.setTitle("Sample 2");
		samples.add(tb2);
		
		TaciBoard tb3 = new TaciBoard(i3);
		tb3.setTitle("Sample 3");
		samples.add(tb3);
		
		TaciBoard tb4 = new TaciBoard(i4);
		tb4.setTitle("Sample 4");
		samples.add(tb4);
	}*/

	private void setLookAndFeel(String clzz) throws Exception {
		UIManager.setLookAndFeel(clzz);
		SwingUtilities.updateComponentTreeUI(this);
	}
	
	private void automaticAction() throws DataNotSuitableException,
			TargetUnreachableException {
		while (openedNodes != null && openedNodes.size() != 0) {
			 TaciNode bestNode = getBestNode();
			if (bestNode.getG() > MAX_STEP) {
				throw new TargetUnreachableException(bestNode.getG());
			}

			openedNodes.remove(bestNode);
			closedNodes.add(bestNode);

			if (bestNode.getH() == 0) {
				step = bestNode.getG() - 1;
				while (bestNode.getPre() != null) {
					getDirection(bestNode.getPre(), bestNode);
					bestNode = bestNode.getPre();
				}
				break;
			}

			move(bestNode);
		}
	}

	private void getDirection(TaciNode pre, TaciNode nowNode) {
		Point preEmptyIndex = pre.getEmptyIndex();
		Point nextEmptyIndex = nowNode.getEmptyIndex();
		
		//Keep tracks
		Direction dir;//Suitable direction for the cell near by emtycell
		if(preEmptyIndex.x > nextEmptyIndex.x) {
			dir = Direction.SOUTH;
		} else if(preEmptyIndex.x < nextEmptyIndex.x) {
			dir = Direction.NORTH;
		} else if(preEmptyIndex.y > nextEmptyIndex.y) {
			dir = Direction.EAST;
		} else {
			dir = Direction.WEST;
		}
		
		TaciBoard.directions.push(new Way(dir, nextEmptyIndex));

	}

	private void stepByStepAction() throws TargetUnreachableException,
			DataNotSuitableException, OutOfMemoryError {
		while (openedBoards != null && openedBoards.size() != 0) {
			if ((MAX_HEAP_SIZE - Runtime.getRuntime().totalMemory()) <= MIN_HEAP_SPACE) {
				Runtime.getRuntime().gc();//Try to free up
				if (MAX_HEAP_SIZE - Runtime.getRuntime().totalMemory() <= MIN_HEAP_SPACE) {
					throw new OutOfMemoryError("Low heap space! less than "
							+ MIN_HEAP_SPACE + " bytes");
				}
			}
			
			TaciBoard bestBoard = getBestBoard();
			if (bestBoard.getG() > MAX_STEP) {
				throw new TargetUnreachableException(bestBoard.getG());
			}

			openedBoards.remove(bestBoard);
			closedBoards.add(bestBoard);

			if (bestBoard.getH() == 0) {
				while (bestBoard.getPre() != null) {
					screenBoards.add(bestBoard);
					bestBoard = bestBoard.getPre();
				}
				viewToScreen();
				break;
			}

			move(bestBoard);
		}
	}

	private void viewToScreen() {
		//High light the best board for each step
		for(TaciBoard tc : screenBoards) {
			for(TaciBoard t : tc.getTaciNextAvaiableBoards()) {
				if(TaciUtils.isIn(screenBoards, t)) {
					t.setInScreen(true); //Verify it is in screen, use later in order to distinguish between 
										//a board has next available steps with a board also has next available steps
										//but is in screen. --> See TaciPopup
					t.setBackgroundJ(TACI_CELL_BEST_BOARD_BACK_COLOR);
				}
			}
			//Add to screen
			add(tc);
			tc.updateUI();
		}
		
	}
	
	/**
	 * This is used for step by step mode.
	 * */
	private void move(TaciBoard bestBoard) throws DataNotSuitableException {
		for (TaciBoard tb : bestBoard.moveNext()) {
			if (!TaciUtils.isIn(openedBoards, tb)
					&& !TaciUtils.isIn(closedBoards, tb)) {
				open(tb);
			}
		}
		
		// Add to keep all opened board
		for (TaciBoard tb : openedBoards) {
			TaciBoard copy = TaciUtils.copyOfBoard(tb);
			bestBoard.addAvailableMovements(copy);
		}
	}
	
	/**
	 * This is used for automatic mode only.
	 * @throws DataNotSuitableException 
	 * */
	private void move(TaciNode bestNode) {
		for(TaciNode tn : bestNode.moveNext()) {
			if(!TaciUtils.isContain(openedNodes, tn)&&
					!TaciUtils.isContain(closedNodes, tn)) {
				open(tn);
			}
		}
	}
	
	private void open(TaciBoard tb) {
		openedBoards.add(tb);
		opens++;
	}

	//Just use for automatic mode
	private void open(TaciNode node) {
		openedNodes.add(node);
		opens++;
	}
	
	private TaciBoard getBestBoard() {
		TaciBoard bestBoard = openedBoards.get(0);
		//if (jmSwapWhenEqual.isSelected()) {
			for (TaciBoard tb : openedBoards) {
				if (tb.getG() + tb.getH() - bestBoard.getG() - bestBoard.getH() <= 0) {
					bestBoard = tb;
				}
			}
		/*} else {
			for (TaciBoard tb : openedBoards) {
				if (tb.getG() + tb.getH() - bestBoard.getG() - bestBoard.getH() < 0) {
					bestBoard = tb;
				}
			}
		}*/
		
		if(!bestBoard.getTitle().equals(GOAL_TITLE) && !bestBoard.getTitle().equals(INITIAL_TITLE)) {
			bestBoard.setBackgroundJ(TACI_CELL_BEST_BOARD_BACK_COLOR);
		}
		
		return bestBoard; 
	}
	
	private TaciNode getBestNode() {
		TaciNode bestNode = openedNodes.get(0);
		//if (jmSwapWhenEqual.isSelected()) {
			for (TaciNode tc : openedNodes) {
				if (tc.getG() + tc.getH() - bestNode.getG() - bestNode.getH() <= 0) {
					bestNode = tc;
				}
			}
		/*} else {
			for (TaciNode tc : openedNodes) {
				if (tc.getG() + tc.getH() - bestNode.getG() - bestNode.getH() < 0) {
					bestNode = tc;
				}
			}
		}*/
		
		return bestNode;
	}
	/**
	 * Customize the radio menu item.
	 * */
	/*private class MyRadioMenuItem extends JRadioButtonMenuItem {
		private static final long serialVersionUID = 1L;
		
		private String styleClass = null;
		public MyRadioMenuItem(String text, boolean checked, String styleClzss) {
			super(text, checked);
			setStyleClass(styleClzss); 
		}
		
		public MyRadioMenuItem(String text, String styleClzss) {
			super(text);
			setStyleClass(styleClzss); 
		}

		public void setStyleClass(String styleClass) {
			this.styleClass = styleClass;
		}

		public String getStyleClass() {
			return styleClass;
		}
	}*/
}
