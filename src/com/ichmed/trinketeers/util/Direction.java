package com.ichmed.trinketeers.util;

public enum Direction
{
	NORTH(0, 1),
	EAST(1, 0),
	SOUTH(0, -1),
	WEST(-1, 0);
	
	int x;
	int y;
	
	private Direction(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean isOposite(Direction d)
	{
		return d.x == -this.x && d.y == -this.y;
	}
}
