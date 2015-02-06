package com.ichmed.trinketeers.world;

import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Tile
{
	public static Tile[] tiles = new Tile[128];
	
	static
	{
		tiles[0] = new Tile();
	}
	
	public void render(int x, int y)
	{
		TextureLibrary.bindTexture("resc/textures/floorMud.png");
		GLHelper.drawRect((.125f * x) - (1), (.125f * y) - 1, .125f, .125f);
	}
}
