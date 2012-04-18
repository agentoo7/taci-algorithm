package taci.program;

import static taci.usercontrol.ReferenceConstants.BOARD_MAXTRIX_SIZE;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import taci.usercontrol.exception.DataNotSuitableException;

/**
 * Executable class. Call the JVM - Java Virtual Machine to invoke the program.
 * @author HUY
 * @version 1.0
 * */
public class Program {
	/**
	 * No use, just be a example.
	 * */
	public Program() {
		System.out.println(getClass().getCanonicalName());
		String template = "HEY %s";
		System.out.println(String.format(template, "abc"));
	}
	public static void main(String... strings) {
		//new Program();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					new TaciProgram().setVisible(true);
				} catch (DataNotSuitableException e1) {
					JOptionPane.showMessageDialog(
							null,
							String.format(
									"Please check your input data, be sure that the size is %d!",
									BOARD_MAXTRIX_SIZE));
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
}
