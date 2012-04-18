package taci.program;

import static taci.usercontrol.ReferenceConstants.*;
import static taci.usercontrol.ReferenceConstants.GOAL_TITLE;
import static taci.usercontrol.ReferenceConstants.INITIAL_LEFT_BOARD;
import static taci.usercontrol.ReferenceConstants.INITIAL_TITLE;
import static taci.usercontrol.ReferenceConstants.INITIAL_TOP_BOARD;
import static taci.usercontrol.ReferenceConstants.J_HEIGHT;
import static taci.usercontrol.ReferenceConstants.J_WIDTH;
import static taci.usercontrol.ReferenceConstants.LAF_GTK;
import static taci.usercontrol.ReferenceConstants.LAF_METAL;
import static taci.usercontrol.ReferenceConstants.LAF_NIMBUS;
import static taci.usercontrol.ReferenceConstants.LAF_NOTIFY;
import static taci.usercontrol.ReferenceConstants.LAF_WINDOWS;
import static taci.usercontrol.ReferenceConstants.MAX_HEAP_SIZE;
import static taci.usercontrol.ReferenceConstants.MAX_STEP;
import static taci.usercontrol.ReferenceConstants.MIN_HEAP_SPACE;
import static taci.usercontrol.ReferenceConstants.NEW_WINDOW_HEIGHT;
import static taci.usercontrol.ReferenceConstants.NEW_WINDOW_WIDTH;
import static taci.usercontrol.ReferenceConstants.RESIZABLE;
import static taci.usercontrol.ReferenceConstants.STEP_BY_STEP_MAY_USE_A_LOT_MEMORY;
import static taci.usercontrol.ReferenceConstants.TACI_CELL_BEST_BOARD_BACK_COLOR;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import taci.commons.Direction;
import taci.commons.GoalKind;
import taci.commons.TaciUtils;
import taci.usercontrol.ReferenceConstants;
import taci.usercontrol.TaciBoard;
import taci.usercontrol.TaciNode;
import taci.usercontrol.TaciStatusBar;
import taci.usercontrol.Way;
import taci.usercontrol.exception.DataNotSuitableException;
import taci.usercontrol.exception.TargetUnreachableException;

/**
 * Main class for this program. It holds all functions and actions for this Taci program.<br/>
 * Why not use the {@link PriorityQueue} to save all opened board.
 * @author BINH
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
	
	private List<TaciBoard> samples;
	private TaciStatusBar pnlStatus = new TaciStatusBar();
	
	private int opens = 0;
	/*For automation mode*/
	private TaciNode initialNode;
	private List<TaciNode> openedNodes = new ArrayList<TaciNode>();
	private List<TaciNode> closedNodes = new ArrayList<TaciNode>();
	private int step = 0;
	
	/*private JMenuItem jmDefault = new MyRadioMenuItem("Default", true, UIManager.getSystemLookAndFeelClassName());
	private JMenuItem jmGTK = new MyRadioMenuItem("GTK", LAF_GTK);
	private JMenuItem jmMetal = new MyRadioMenuItem("Metal", LAF_METAL);
	private JMenuItem jmNotify = new MyRadioMenuItem("Notify", LAF_NOTIFY);
	private JMenuItem jmNimbus = new MyRadioMenuItem("Nimbus", LAF_NIMBUS);
	private JMenuItem jmWindows = new MyRadioMenuItem("Windows", LAF_WINDOWS);*/
	
	private JMenuItem fileNew = new JMenuItem("Tạo mới");
	private JMenuItem jmRun = new JMenuItem("Chạy");
	//private JMenuItem jmSwapWhenEqual = new JCheckBoxMenuItem("Swap when equal", true);
	
	public TaciProgram() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(J_WIDTH, J_HEIGHT);
		setLayout(null);
		setTitle("Chương trình mô phỏng giải bài toán Taci - Nguyễn Công Huy");
		setResizable(RESIZABLE);
		
		createNewPopupData();
	
		//Create the menu
		JMenuBar menubar = new JMenuBar();
	    JMenu jmFile = new JMenu("Tệp");
	    jmFile.setMnemonic('T');
	    jmFile.add(new JSeparator());
	    /*JMenu jmStyle = new JMenu("Styles");
	    jmStyle.setMnemonic('s');
	    jmStyle.add(new JSeparator());*/
	    
	    jmRun.setEnabled(false);
	    jmRun.setMnemonic('C');
	    jmRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*String[] choices = { "Automation", "Step by Step" };
				int response = JOptionPane.showOptionDialog(null // Center in
																	// window.
						, "Which kind of mode you want to play?" // Message
						, "Mode chosing" // Title in titlebar
						, JOptionPane.YES_NO_OPTION // Option type
						, JOptionPane.QUESTION_MESSAGE // messageType
						, null // Icon (none)
						, choices // Button text as above.
						, "Automation" // Default button's label
				);*/
				
				//if(response == 0) {
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
				/*} else if(response == 1) {//Step by step mode
					JOptionPane.showMessageDialog(null, STEP_BY_STEP_MAY_USE_A_LOT_MEMORY);
					final long start = System.currentTimeMillis();
						if(screenBoards.size() <= 1) { //Check if screenboard is empty, do the heuristic
							setCursor(new Cursor(Cursor.WAIT_CURSOR));
							updateStatus(0, 0, 0.0f, 0,
									CALCULATING, SWAP_DELAY_TIME);
							
							Thread th = new Thread() {
								@Override
								public void run() {
									try {
										//Disable the run menu item when doing and after finishing the job
										fileNew.setEnabled(false);
										jmRun.setEnabled(false);
										
										stepByStepAction();
										
										setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
										float time = (System.currentTimeMillis() - start) / 1000; 
										updateStatus(opens, closedBoards.size(), time,
												screenBoards.size() - 2,
												READY, SWAP_DELAY_TIME); //Subtract the initial and goal state
										fileNew.setEnabled(true);
									} catch (OutOfMemoryError e) {
										JOptionPane
												.showMessageDialog(
														null,
														"<html>Program will be restarted because no more java heap space.<br/>Please run the <b>automatic mode</b> instead.</html>");
										doClean();
										try {
											String path = "\""
													+ getClass()
															.getProtectionDomain()
															.getCodeSource()
															.getLocation()
															.getPath()
															.replace("%20", " ")
													+ "\"";
											Runtime.getRuntime()
													.exec("java -cp "
															+ path
															+ " "
															+ Program.class
																	.getCanonicalName());
											Runtime.getRuntime().exit(1);
										} catch (Exception e1) {
											e1.printStackTrace();
										}
									} catch (TargetUnreachableException e1) {
										JOptionPane.showMessageDialog(null,
												e1.getMessage());
										e1.printStackTrace();
										e1.printStackTrace();
									} catch (DataNotSuitableException e1) {
										JOptionPane.showMessageDialog(
												null,
												String.format(
														"Please check your input data, be sure that the size is %d!",
														BOARD_MAXTRIX_SIZE));
										e1.printStackTrace();
									}
								}
							};
							
							th.start();
						} else {
							viewToScreen(); //Just view all best board to the screen*/
						//}
					//}
				}
			});
	    
	    fileNew.setMnemonic('n');
	    fileNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doClean();
				//New Popup
				TaciNewSession newScreen = new TaciNewSession(samples);
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
	    
	    //jmSwapWhenEqual.setMnemonic('w');
	    //jmSwapWhenEqual.setToolTipText(ReferenceConstants.SWAP_WHEN_EQUAL_TOOL_TIP);
	    
	    /*JMenuItem fileSwapSpeed = new JMenuItem("Swapping speed...");
	    fileSwapSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] choices = { "Quick", "Normal" };
				int response = JOptionPane.showOptionDialog(null // Center in
						// window.
						, "How quickly do you want the program to do swapping?" // Message
						, "Swapping speed" // Title in titlebar
						, JOptionPane.YES_NO_OPTION // Option type
						, JOptionPane.QUESTION_MESSAGE // messageType
						, null // Icon (none)
						, choices // Button text as above.
						, "Normal" // Default button's label
				);
				if(response == 0) {
					ReferenceConstants.SWAP_DELAY_TIME = 10;
					pnlStatus.setCurrentSwappingSpeed("Quick");
				} else {
					ReferenceConstants.SWAP_DELAY_TIME = 100;
					pnlStatus.setCurrentSwappingSpeed(String.format("Normal"));
				}
			}
		});*/
	    
	    jmFile.add(fileNew);
	    jmFile.add(jmRun);
	    //jmFile.add(jmSwapWhenEqual);
	    jmFile.addSeparator();
	    //jmFile.add(fileSwapSpeed);
	    jmFile.add(fileExit);
	    
	    /*ButtonGroup group = new ButtonGroup();
	    group.add(jmDefault);
	    group.add(jmGTK);
	    group.add(jmMetal);
	    group.add(jmNotify);
	    group.add(jmNimbus);
	    group.add(jmWindows);
	    
	    jmStyle.add(jmDefault);
	    jmDefault.setMnemonic('e');
	    jmDefault.addActionListener(new MyActionListener());
	    jmStyle.addSeparator();
	    
	    jmStyle.add(jmGTK);
	    jmGTK.setMnemonic('t');
	    jmGTK.addActionListener(new MyActionListener());
	    
	    jmStyle.add(jmMetal);
	    jmMetal.setMnemonic('m');
	    jmMetal.addActionListener(new MyActionListener());
	    
	    jmStyle.add(jmNotify);
	    jmNotify.setMnemonic('f');
	    jmNotify.addActionListener(new MyActionListener());
	    
	    jmStyle.add(jmNimbus);
	    jmNimbus.setMnemonic('u');
	    jmNimbus.addActionListener(new MyActionListener());
	    
	    jmStyle.add(jmWindows);
	    jmWindows.setMnemonic('w');
	    jmWindows.addActionListener(new MyActionListener());*/
	    
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
		
		try {
			createNewPopupData();
		} catch (DataNotSuitableException e) {
			e.printStackTrace();
		}
	}
	
	private void updateStatus(int opens, int close, float f, int step, String status, int speed) {
		//pnlStatus.setOpens(opens);
		//pnlStatus.setCloses(close);
		pnlStatus.setTimeCost(f);
		pnlStatus.setSteps(step);
		pnlStatus.setCurrentStatus(status);
		//pnlStatus.setCurrentSwappingSpeed(speed <= 10 ? "Quick" : "Normal");
	}
	
	private void createNewPopupData() throws DataNotSuitableException {
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
	}

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
	private class MyRadioMenuItem extends JRadioButtonMenuItem {
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
	}
	
	/**
	 * Custom the action listener for the menu.
	 * @author BINH
	 * @version 1.0
	 * */
	/*private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MyRadioMenuItem object = (MyRadioMenuItem)e.getSource();
			if (object.getStyleClass().equals(null)) {
				return;
			}
			try {
				setLookAndFeel(object.getStyleClass());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "This look and feel is not supported!");
				//Set back to default
				jmDefault.setSelected(true);
				jmDefault.doClick();
			}
		}
	}*/
}
