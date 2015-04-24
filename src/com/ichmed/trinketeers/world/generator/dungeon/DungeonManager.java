package com.ichmed.trinketeers.world.generator.dungeon;

import java.util.ArrayList;
import java.util.List;

import com.ichmed.trinketeers.util.Direction;
import com.ichmed.trinketeers.world.World;

public class DungeonManager
{
	boolean[][][] occupiedTiles;
	int sizeX, sizeY, sizeZ;
	int posX, posY, startingHeight;

	public DungeonManager(int sizeX, int sizeY, int height, int posX, int posY, int startingHeight)
	{
		super();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = height;
		this.posX = posX;
		this.posY = posY;
		this.startingHeight = startingHeight;
		this.occupiedTiles = new boolean[sizeX][sizeY][height];
	}

	public void populate(World w)
	{
		DungeonRoom start = new TestRoomJunction();
		List<DungeonRoom> roomsLeftToConnect = new ArrayList<DungeonRoom>();
		roomsLeftToConnect.add(start);
		System.out.println(start.createRoom(w, this.posX + this.sizeX / 2, this.posY + this.sizeY / 2, startingHeight, this));

		while (roomsLeftToConnect.size() > 0)
		{
			List<DungeonRoom> tmp = new ArrayList<DungeonRoom>(roomsLeftToConnect);
			for (DungeonRoom room : roomsLeftToConnect)
			{
				for (DungeonRoom.Door door : room.doors)
				{
					if (door.connected) continue;
					DungeonRoom j = null;

					if (door.dir == Direction.NORTH || door.dir == Direction.SOUTH)
					{
						int r = (int) (Math.random() * 5);
						if (r < 4) j = new TestRoomTunnelVertical();
						else j = new TestRoomJunction();
					} else
					{

						int r = (int) (Math.random() * 5);
						if (r < 4) j = new TestRoomTunnelHorizontal();
						else j = new TestRoomJunction();
					}
					if (room.attachRoom(w, j, door, this)) tmp.add(j);
				}
				tmp.remove(room);
			}
			roomsLeftToConnect = tmp;
		}
	}

	public boolean occupieSpaceRelativToDungeon(int x, int y, int z, int width, int height)
	{
		if (x >= 0 && y >= 0 && z >= 0 && z < this.sizeZ && x + width < this.sizeX && y + height < this.sizeY)
		{
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					occupiedTiles[x + i][y + j][z] = true;
			return true;
		}
		return false;
	}

	public void registerRoom(DungeonRoom room)
	{

	}

	public boolean isSpaceOccupiedRelativToDungeon(int x, int y, int z, int width, int height)
	{
		if (x >= 0 && y >= 0 && z >= 0 && z < this.sizeZ && x + width < this.sizeX && y + height < this.sizeY)
		{
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					if (occupiedTiles[x + i][y + j][z] == true) return true;
			return false;
		}
		System.out.println(x + " " + y);
		return true;
	}
}
