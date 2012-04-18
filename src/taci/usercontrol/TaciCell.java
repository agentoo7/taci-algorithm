package taci.usercontrol;

import static taci.usercontrol.ReferenceConstants.EMPTY_VALUE;
import static taci.usercontrol.ReferenceConstants.TOOL_TIP_CELL_TEMPLATE;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JLabel;

/**
 * It represents a cell in board - {@link TaciBoard}. It contains the number
 * value, the index on board, the goal index and many other decoration
 * attributes such as background color, fore color, location...
 * 
 * @author BINH
 * @version 1.0
 * */
public class TaciCell extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private Point index;//The current index of this cell
	private Point goalIndex; //The target index of this cell
	private Integer value;//The value of this cell
	private int h; //The remaining step need to go to get the goal
	
	/**
	 * Create a new cell
	 * @param _size size of the cell
	 * @param _foreColor fore color for this cell
	 * @param _location the location of cell
	 * @param _index the index of the cell on board, index calculation is dependent on data of board - see more {@link TaciBoard}
	 * @param _value the value of cell, if value is EMPTY_VALUE in {@link ReferenceConstants}, no text is shown
	 * */
	public TaciCell(int _size, Color _foreColor, Point _location, Point _index, Integer _value) {
		setSize(new Dimension(_size, _size));
		setForeground(_foreColor);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setLocation(_location);
		setIndex(_index);
		setValue(_value);
		setText(value == EMPTY_VALUE ? "" : value.toString());
		
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}
	
	public void setIndex(Point index) {
		this.index = index;
	}

	public Point getIndex() {
		return index;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setGoalIndex(Point goalIndex) {
		this.goalIndex = goalIndex;
	}

	public Point getGoalIndex() {
		return goalIndex;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getH() {
		return h;
	}
	
	@Override
	public boolean contains(int x, int y) {
		//Set tool tip
		if(getValue() == EMPTY_VALUE) {
			return false;
		}
		
		setToolTipText(String.format(TOOL_TIP_CELL_TEMPLATE, getValue(), getH()));
		return super.contains(x, y);
	}

	public void dispose() {
		index = null;
		goalIndex = null;
		value = null;
	}
}
