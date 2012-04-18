package taci.usercontrol;

import java.awt.Point;

import taci.commons.Direction;

/**
 * Show the way: Where to go and direction.
 * @author BINH
 * @version 1.0
 * */
public class Way {
	private Direction dir;
	private Point index;
	
	public Way(Direction dir, Point index) {
		this.dir = dir;
		this.index = index;
	}
	
	public void setDir(Direction dir) {
		this.dir = dir;
	}
	
	/**
	 * Get direction to move.
	 * */
	public Direction getDir() {
		return dir;
	}

	public void setIndex(Point index) {
		this.index = index;
	}

	/**
	 * Get where is the index of cell need to be moved.
	 * */
	public Point getIndex() {
		return index;
	}
}
