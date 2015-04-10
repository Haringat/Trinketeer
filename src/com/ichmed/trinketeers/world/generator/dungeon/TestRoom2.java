package com.ichmed.trinketeers.world.generator.dungeon;

import static com.ichmed.trinketeers.util.Direction.EAST;
import static com.ichmed.trinketeers.util.Direction.NORTH;
import static com.ichmed.trinketeers.util.Direction.SOUTH;
import static com.ichmed.trinketeers.util.Direction.WEST;

import org.lwjgl.util.vector.Vector2f;

public class TestRoom2 extends DungeonRoom
{
	public TestRoom2()
	{
		tiles = new int[]{2, 5, 5, 2,
						  5, 5, 5, 5,
						  5, 5, 5, 5,
						  2, 5, 5, 2};

		size = new Vector2f(4, 4);
		doors.add(new Door(1, 0, 2, 1, NORTH));
		doors.add(new Door(1, 3, 2, 1, SOUTH));
		
		doors.add(new Door(0, 1, 1, 2, EAST));
		doors.add(new Door(3, 1, 1, 2, WEST));
	}
}
