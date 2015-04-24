package com.ichmed.trinketeers.world.generator.dungeon;

import static com.ichmed.trinketeers.util.Direction.*;

import org.lwjgl.util.vector.Vector2f;


public class TestRoomTunnelHorizontal extends DungeonRoom
{
	public TestRoomTunnelHorizontal()
	{
		tiles = new int[] { 2, 2, 2, 2,
							5, 5, 5, 5,
							5, 5, 5, 5,
							2, 2, 2, 2 };
		size = new Vector2f(4, 4);
		doors.add(new Door(0, 1, 1, 2, EAST));
		doors.add(new Door(4, 1, 1, 2, WEST));
	}
}
