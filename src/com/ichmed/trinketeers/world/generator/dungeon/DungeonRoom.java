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
	List<Door> doors = new ArrayList<Door>();
	Vector2f size;
	int[] tiles;
	Vector2f pos;
	int height;
	int defaultWall = 2;
	int defaultFloor = 5;
	boolean isRotatable = true;
	
	public static DungeonRoom testRoom1 = new DungeonRoom();
	public static DungeonRoom testRoom2 = new DungeonRoom();
	static
	{
		testRoom1.tiles = new int[]{2, 5, 5, 2,
								    2, 5, 5, 2,
								    2, 5, 5, 2,
								    2, 5, 5, 2};
		testRoom1.size = new Vector2f(4, 4);
		testRoom1.doors.add(new Door(1, 0, 2, 1, NORTH));
		testRoom1.doors.add(new Door(1, 4, 2, 1, SOUTH));
		
		testRoom2.tiles = new int[]{2, 5, 5, 2,
								    5, 5, 5, 5,
								    5, 5, 5, 5,
								    2, 5, 5, 2};
		
		testRoom2.size = new Vector2f(4, 4);
		testRoom2.doors.add(new Door(1, 0, 2, 1, NORTH));
		testRoom2.doors.add(new Door(0, 1, 1, 2, EAST));
		testRoom2.doors.add(new Door(2, 4, 2, 1, SOUTH));
		testRoom2.doors.add(new Door(4, 2, 1, 2, WEST));
	}
	
	public DungeonRoom rotate()
	{
		if(!isRotatable) return this;
		int[][] temp2dim = new int[(int)this.size.x][(int)this.size.y];
		
		for(int i = 0; i < this.size.x * this.size.y; i++)
				temp2dim[(int)(i % size.x)][(int)(i / this.size.x)] = this.tiles[i];
		
		temp2dim = MathUtil.rotateCW(temp2dim); 
		
		float oldSizeX = this.size.x;
		float oldSizeY = this.size.y;
		
		this.size.y = oldSizeX;
		this.size.x = oldSizeY;
		
		for(Door d : this.doors)
		{
			d.dir = d.dir == NORTH ? WEST : d.dir == WEST ? SOUTH : d.dir == SOUTH ? EAST : NORTH;
			d.pos = new Vector2f(this.size.x - d.pos.y, d.pos.x);
			d.size = new Vector2f(d.size.y, d.size.x);
		}
		
		for(int i = 0; i < tiles.length; i++)
			this.tiles[i] = temp2dim[(int)(i % size.x)][(int)(i / this.size.x)];
		return this;
	}
	
	public boolean attachRoom(World w, DungeonRoom room, Direction d)
	{
		for(Door doorA : this.doors)
		{
			if(doorA.dir != d) continue;
			for(Door doorB : room.doors)
			{
				if(doorB.dir.isOposite(doorA.dir) 
						&& doorB.size.y == doorA.size.y 
						&& doorB.size.x == doorA.size.x)
				{
					doorA.connected = true;
					doorB.connected = true;
					int shiftX = (int)((this.pos.x + doorA.pos.x) - doorB.pos.x);
					int shiftY = (int)(this.pos.y + doorA.pos.y - doorB.pos.y);
					if(doorB.dir == NORTH) shiftX += (doorB.size.x - 1);
					if(doorB.dir == EAST) shiftY += (doorB.size.y - 1);
					if(room.createRoom(w, shiftX, shiftY, this.height))
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean createRoom(World world, int x, int y, int z)
	{
		this.pos = new Vector2f(x, y);
		this.height = z;
		for(int i = 0; i < size.x * size.y; i++)
		{
			Chunk.setTile(world, (int)(x + (i % size.x)), (int)y + (int)(i / size.x), z, tiles[i]);
		}
//		for(Door d : this.doors) if(!d.connected) Chunk.setTile(world, (int)(this.pos.x + d.pos.x), (int)(this.pos.y + d.pos.y), this.height, this.defaultWall);
		return true;
	}
	
	private static class Door
	{
		Vector2f pos;
		Vector2f size;
		Direction dir;
		boolean connected = false;
		
		public Door(int x, int y, int width, int height,  Direction d)
		{
			this.pos = new Vector2f(x, y);
			this.size = new Vector2f(width, height);
			dir = d;
		}
	}
}
