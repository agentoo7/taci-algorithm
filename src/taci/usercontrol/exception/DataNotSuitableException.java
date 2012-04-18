package taci.usercontrol.exception;
import static taci.usercontrol.ReferenceConstants.*;

public class DataNotSuitableException extends Exception {
	private static final long serialVersionUID = 1L;

	public DataNotSuitableException(int sizeX, int sizeY) {
		super(String.format("YOUR DATA SIZE IS %dx%d, WHILE THE SUITABLE SIZE IS %dx%d.",
				sizeX, sizeY, BOARD_MAXTRIX_SIZE, BOARD_MAXTRIX_SIZE));
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
}
