package com.ichmed.trinketeers.world.generator.dungeon;

import static com.ichmed.trinketeers.util.Direction.*;

import org.lwjgl.util.vector.Vector2f;

public class TestRoom3 extends DungeonRoom
{

	public TestRoom3()
	{
		tiles = new int[]{2, 5, 5, 2,
						  2, 5, 5, 2,
						  2, 5, 5, 2,
						  2, 2, 2, 2};

		size = new Vector2f(4, 4);
		doors.add(new Door(1, 0, 2, 1, NORTH));
//		doors.add(new Door(2, 4, 2, 1, SOUTH));
	}
}