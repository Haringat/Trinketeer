package com.ichmed.trinketeers.world.tile;

import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class TileWallBrick extends Tile
{

	public TileWallBrick(String texture, boolean breakable, boolean massive)
	{
		super(texture, breakable, massive);
	}

	@Override
	public String getTexture(World w, int x, int y)
	{
		if(!tiles[Chunk.getTile(x, y - 1, w.currentHeight)].massive && Chunk.getTile(x, y - 1, w.currentHeight) != 3) return this.texture;
		return null;
	}
	
	@Override
	public boolean renderInFront(World w, int x, int y)
	{
		return tiles[Chunk.getTile(x, y - 1, w.currentHeight)].massive || Chunk.getTile(x, y - 1, w.currentHeight) == 3;
	}
	
	

}
