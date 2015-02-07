package com.ichmed.trinketeers.world;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Tile
{
	public final String texturePath;
	public final boolean breakable, massive;
	public static Tile[] tiles = new Tile[128];

	static
	{
		tiles[0] = new Tile(null, false, false);
		tiles[1] = new Tile("resc/textures/floorMud.png", false, false);
	}

	public Tile(String texturePath, boolean breakable, boolean massive)
	{
		super();
		this.texturePath = texturePath;
		this.breakable = breakable;
		this.massive = massive;
	}

	public void render(int x, int y)
	{
		if (this.texturePath != null) TextureLibrary.bindTexture(this.texturePath);
		else
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(0, 0, 0);
		}
		GLHelper.drawRect((.125f * x), (.125f * y), .125f, .125f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
	}
}
