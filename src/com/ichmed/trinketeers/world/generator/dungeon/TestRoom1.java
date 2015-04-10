package com.ichmed.trinketeers.world.generator.dungeon;

import static com.ichmed.trinketeers.util.Direction.NORTH;
import static com.ichmed.trinketeers.util.Direction.SOUTH;

import org.lwjgl.util.vector.Vector2f;


public class TestRoom1 extends DungeonRoom
{
	public TestRoom1()
	{
		tiles = new int[] { 2, 5, 5, 2,
							2, 5, 5, 2,
							2, 5, 5, 2,
							2, 5, 5, 2 };
		size = new Vector2f(4, 4);
		doors.add(new Door(1, 0, 2, 1, NORTH));
		doors.add(new Door(1, 3, 2, 1, SOUTH));
	}
}
