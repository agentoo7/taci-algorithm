package taci.usercontrol.exception;

import static taci.usercontrol.ReferenceConstants.*;

public class TargetUnreachableException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public TargetUnreachableException(int g) {
		super(
				String.format(
						"The target is unreachable! The maximun allowed step is %d while yours is %d.",
						MAX_STEP, g));
	}
}
