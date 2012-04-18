package taci.usercontrol;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;

/**
 * Contains all constants that are used in whole taci program.
 * Constants may be a tool tip text or the size of {@link TaciCell} and all things.
 * @author BINH
 * @version 1.0
 * */
public class ReferenceConstants {
	//TACI cell constants
	public static final int CELL_SIZE = 20;
	public static final int PADING = 17;
	public static final int BOARD_MAXTRIX_SIZE = 3;//3x3
	public static final Color TACI_CELL_FORE_COLOR = Color.BLACK;
	public static final Color TACI_CELL_OWN_BOARD_FORE_COLOR = Color.BLACK;
	public static final Color TACI_CELL_BACK_COLOR = Color.LIGHT_GRAY;
	public static final Color TACI_CELL_BORDER_COLOR = Color.BLUE;
	public static final Color TACI_CELL_BEST_BOARD_BACK_COLOR = Color.MAGENTA;
	public static final Color TACI_EMPTY_CELL_BACK_COLOR = Color.lightGray;
	public static final Color TACI_CELL_BOARD_HAS_NEXT_BACK_COLOR = Color.ORANGE;
	public static final String SPACE = "  ";
	public static final Integer EMPTY_VALUE = 0;
	public static final int MAX_BOARD_IN_POPUP = 4;
	
	
	public static final int MOVE_DISTANCE = CELL_SIZE * BOARD_MAXTRIX_SIZE + 30;
	
	//TOOL TIP
	public static final String TOOL_TIP_CELL_TEMPLATE = "<html><b>%d</b>" +
														"<br/>" +
														"h = %d<br/>" +
														"</html>";
	public static final String TOOL_TIP_BOARD_TEMPLATE = "<html>"+
														"g = %d<br/>" +
														"h = %d<br/>" +
														"f = %d<br/>" +
														"<u><strong>Note: </strong></u><br/>" +
														"<li><i>f(n) = g(n) + h(n).</i><br/>"+
														"<i>g(n)</i>: sá»‘ bÆ°á»›c Ä‘i.<br/>"+
														"<i>h(n)</i>: tá»•ng sá»‘ sai lá»‡ch cá»§a táº¥t cáº£ cÃ¡c Ã´ so vá»›i vá»‹ trÃ­ Ä‘Ã­ch."+
														"</html>";
	public static final Color TOOLTIP_BACKGROUND_COLOR = Color.getHSBColor(0.3f, 0.1f, 0.9f);
	public static final Color TOOLTIP_BORDER_COLOR = Color.BLUE;
	public static final Color TOOLTIP_FORE_COLOR = Color.BLACK;
	
	//GOAL STATE
	public static final int MAX_STEP = 111;
	public static final long MIN_HEAP_SPACE = 1024 * 2;
	public static final Integer[][] GOAL_DATA_A = new Integer[][] {
			new Integer[] { 1, 2, 3 }, new Integer[] { 8, 0, 4 },
			new Integer[] { 7, 6, 5 }};
	public static final List<TaciCell> GOAL_A = new ArrayList<TaciCell>();
	static {
		int x = PADING - 5, y = PADING;
		for (int i = 0; i < GOAL_DATA_A.length; i++) {
			for (int j = 0; j < GOAL_DATA_A.length; j++) {
				Point location = new Point(x, y);
				Point index = new Point(i, j); 
				TaciCell tc = new TaciCell(CELL_SIZE, TACI_CELL_FORE_COLOR,
						location, index, GOAL_DATA_A[i][j]);
				tc.setBackground(TACI_CELL_BACK_COLOR);
				tc.setOpaque(true);
				tc.setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));				
				tc.setGoalIndex(index);
				
				GOAL_A.add(tc);
				
				x += CELL_SIZE;
			}
			
			y += CELL_SIZE;
			x = PADING - 5;
		}
		//Order the GOAL in order to suitable for later use at TaciBoard.java
		Collections.sort(GOAL_A, new Comparator<TaciCell>() {
			@Override
			public int compare(TaciCell o1, TaciCell o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
	}
	
	public static final Integer[][] GOAL_DATA_B = new Integer[][] {
		new Integer[] { 0, 1, 2 }, new Integer[] { 3, 4, 5 },
		new Integer[] { 6, 7, 8 }};
	public static final List<TaciCell> GOAL_B = new ArrayList<TaciCell>();
	static {
		int x = PADING - 5, y = PADING;
		for (int i = 0; i < GOAL_DATA_B.length; i++) {
			for (int j = 0; j < GOAL_DATA_B.length; j++) {
				Point location = new Point(x, y);
				Point index = new Point(i, j); 
				TaciCell tc = new TaciCell(CELL_SIZE, TACI_CELL_FORE_COLOR,
						location, index, GOAL_DATA_B[i][j]);
				tc.setBackground(TACI_CELL_BACK_COLOR);
				tc.setOpaque(true);
				tc.setBorder(BorderFactory.createLineBorder(TACI_CELL_BORDER_COLOR));				
				tc.setGoalIndex(index);
				
				GOAL_B.add(tc);
				
				x += CELL_SIZE;
			}
			
			y += CELL_SIZE;
			x = PADING - 5;
		}
		//Order the GOAL in order to suitable for later use at TaciBoard.java
		Collections.sort(GOAL_B, new Comparator<TaciCell>() {
			@Override
			public int compare(TaciCell o1, TaciCell o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
	} 
	//TACI JFRAME
	public static final int J_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int J_HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	public static final int HEIGHT_SCREEN = 300;
	public static final int WIDTH_SCREEN = 300;
	public static final boolean RESIZABLE = true;
	public static final int INITIAL_LEFT_BOARD = 0;
	public static final int INITIAL_TOP_BOARD = 0;
	public static final int RIGHT_BOARD_PADING = CELL_SIZE * BOARD_MAXTRIX_SIZE;
	public static final String GOAL_TITLE = "Goal";
	public static final String NEXT_TITLE = "Step ";
	public static final String INITIAL_TITLE = "Initial";
	public static final String LAF_WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	public static final String LAF_METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public static final String LAF_NIMBUS = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	public static final String LAF_NOTIFY = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	public static final String LAF_GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	
	//TACI POPUP
	public static final int MAX_POPUP_WIDTH = 1150;
	public static final int MAX_POPUP_HEIGHT = 130;
	public static final int NEW_X_LOC_WHEN_FULL = 40;
	public static final int NEW_Y_LOC_WHEN_FULL = 20;
	
	//TACI new window
	public static final int NEW_WINDOW_HEIGHT = 200;
	public static final int NEW_WINDOW_WIDTH = 475;
	public static final int NEW_WINDOW_PADING = 30;
	public static final String NEW_TITLE = "New taci";
	
	
	//TACI STAUS bar
	public static final String STATUS_BAR_TITLE = "Status";
	public static final String STATUS_BAR_OPENED_NUMBER = "<html>Open: <font color=blue><i><b>%d</b></i></font></html>";
	public static final String STATUS_BAR_CLOSED_NUMBER = "<html>Close: <font color=blue><i><b>%d</b></i></font></html>";
	public static final String STATUS_BAR_TIME_COST = "<html>Time: <font color=blue><i><b>%f</b></i></font> s</html>";
	public static final String STATUS_BAR_STEPS_NUMBER = "<html>Steps: <font color=blue><i><b>%d</b></i></font></html>";
	
	//TACI MESSAGE
	public static final String STEP_BY_STEP_MAY_USE_A_LOT_MEMORY = "<html>Step by step mode may use <b>a lot of memmory</b> to track all detail steps.</html>";
	public static final String DND_LABEL_TOOL_TIP = "Drag and drop the cell to modify the taci board";
	public static final String SWAP_WHEN_EQUAL_TOOL_TIP = "<html>When comparing the f, if it is checked, <b>the last node</b> will be chosen among equaled-f nodes </html>";
	public static final String STATUS = "<html>Status: <font color=blue><i><b>%s</b></i></font></html>";
	public static final String STATUS_SWAPING_SPEED = "<html>Swapping speed: <font color=blue><i><b>%s</b></i></font></html>";
	
	public static final String LEGEND_TC_HAVE_NEXT_AVAILABLE_MOVEMENT = "<html>Refer a board which has <font color=blue><i><b>next available movement</b></i></font> for next step.</html>";
	public static final String LEGEND_TC_DIED_BOARD = "<html>Refer a board which has <font color=blue><i><b>no next available movement</b></i></font> for next step.</html>";
	public static final String LEGEND_TC_BEST_BOARD = "<html>Refer a board which is <font color=blue><i><b>the best</b></i></font> of each step.</html>";
	
	public static final String INFORMATION_MESSAGE = "<html>Trường Đại học công nghệ thông tin<br/>"+
														"Khoa công nghệ phần mềm<br>"+
														"Môn : Một số thuật toán thông minh<br>"+
														"<br>Giảng viên : <font color=red><b>PGS/TS. Vũ Thanh Nguyên</b></font><br>"+
														"<br>Sinh viên : <font color=red><b>Nguyễn Công Huy</b></font> <br/>" +
														"MSSV: <font color=red><b>08520148</b></font><br/>" +
														"Email: conghuy2012@gmail.com</html>";
	
	//RUNTIME
	public static final long MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory();
	public static int SWAP_DELAY_TIME = 100; //0.01s
}
