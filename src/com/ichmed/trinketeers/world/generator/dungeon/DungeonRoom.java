package com.ichmed.trinketeers.world.generator.dungeon;

import static com.ichmed.trinketeers.util.Direction.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.util.Direction;
import com.ichmed.trinketeers.util.MathUtil;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class DungeonRoom
{
	public List<Door> doors = new ArrayList<Door>();
	Vector2f size;
	int[] tiles;
	Vector2f pos;
	int height;
	int defaultWall = 2;
	int defaultFloor = 5;
	boolean isRotatable = true;

	public DungeonRoom rotate()
	{
		if (!isRotatable) return this;
		int[][] temp2dim = new int[(int) this.size.x][(int) this.size.y];

		for (int i = 0; i < this.size.x * this.size.y; i++)
			temp2dim[(int) (i % size.x)][(int) (i / this.size.x)] = this.tiles[i];

		temp2dim = MathUtil.rotateCW(temp2dim);

		float oldSizeX = this.size.x;
		float oldSizeY = this.size.y;

		this.size.y = oldSizeX;
		this.size.x = oldSizeY;

		for (Door d : this.doors)
		{
			d.dir = d.dir == NORTH ? EAST : d.dir == EAST ? SOUTH : d.dir == SOUTH ? WEST : NORTH;
			d.pos = new Vector2f(d.pos.y, d.pos.x);
			d.size = new Vector2f(d.size.y, d.size.x);
		}

		for (int i = 0; i < tiles.length; i++)
			this.tiles[i] = temp2dim[(int) (i % size.x)][(int) (i / this.size.x)];
		return this;
	}

	public boolean attachRoom(World w, DungeonRoom room, Direction d, DungeonManager manager)
	{
		for (Door doorA : this.doors)
		{
			if (doorA.dir != d) continue;
			if (attachRoom(w, room, doorA, manager)) return true;
		}
		return false;
	}

	public boolean attachRoom(World w, DungeonRoom room, Door doorA, DungeonManager manager)
	{
		for (Door doorB : room.doors)
		{
			if (doorB.dir.isOposite(doorA.dir) && doorB.size.y == doorA.size.y && doorB.size.x == doorA.size.x)
			{
				int shiftX = (int) (this.pos.x + doorA.pos.x - doorB.pos.x);
				int shiftY = (int) (this.pos.y + doorA.pos.y - doorB.pos.y);
				if (room.createRoom(w, shiftX, shiftY, this.height, manager))
				{
					doorA.connected = true;
					doorB.connected = true;
					return true;
				}
			}
		}

		return false;
	}

	public boolean createRoom(World world, int x, int y, int z, DungeonManager manager)
	{
		if (manager != null)
		{
			if (manager.isSpaceOccupiedRelativToDungeon(x - manager.posX, y - manager.posY, z - manager.startingHeight, (int) this.size.x, (int) this.size.y)) return false;
			manager.occupieSpaceRelativToDungeon(x - manager.posX, y - manager.posY, z - manager.startingHeight, (int) this.size.x, (int) this.size.y);
		}
		this.pos = new Vector2f(x, y);
		this.height = z;
		for (int i = 0; i < size.x * size.y; i++)
			Chunk.setTile(world, (int) (x + (i % size.x)), (int) y - (int) (i / size.x), z, tiles[i]);
		// for(Door d : this.doors) if(!d.connected) Chunk.setTile(world,
		// (int)(this.pos.x + d.pos.x), (int)(this.pos.y + d.pos.y),
		// this.height, this.defaultWall);

		return true;
	}

	public static class Door
	{
		Vector2f pos;
		Vector2f size;
		public Direction dir;
		public boolean connected = false;

		public Door(int x, int y, int width, int height, Direction d)
		{
			this.pos = new Vector2f(x, y);
			this.size = new Vector2f(width, height);
			dir = d;
		}
	}
}
