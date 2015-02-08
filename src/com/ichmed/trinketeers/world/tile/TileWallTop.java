package com.ichmed.trinketeers.world.tile;

import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class TileWallTop extends Tile
{

	public TileWallTop(String texture, boolean breakable, boolean massive)
	{
		super(texture, breakable, massive);
	}
	
	@Override
	public String getTexture(World w, int x, int y)
	{
		if(!tiles[Chunk.getTile(x, y + 1, w.currentHeight)].massive) return this.texture;
		return null;
	}
}
