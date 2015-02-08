package com.ichmed.trinketeers.world.tile;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TextureLibrary;
import com.ichmed.trinketeers.world.World;

public class Tile
{
	public final String texture;
	public final boolean breakable, massive;
	public boolean renderInFront = false;
	public static Tile[] tiles = new Tile[128];

	static
	{
		tiles[0] = new Tile(null, false, false);
		tiles[1] = new Tile("floorMud", false, false);
		tiles[2] = new Tile("wallBrick", true, true);
		tiles[2] = new Tile("wallTop", true, false).setRenderInFront(true);
	}

	public Tile(String texture, boolean breakable, boolean massive)
	{
		super();
		this.texture = texture;
		this.breakable = breakable;
		this.massive = massive;
	}

	public Tile setRenderInFront(boolean b)
	{
		this.renderInFront = b;
		return this;
	}
	
	public String getTexture(World w, int x, int y)
	{
		return texture;
	}

	public void render(World w, int x, int y)
	{
		if (this.texture != null) GLHelper.renderTexturedQuad((.125f * x), (.125f * y), .125f, .125f, TextureLibrary.getTextureVector(texture));
		else
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(0, 0, 0);
			GLHelper.drawRect((.125f * x), (.125f * y), .125f, .125f);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
		}
	}
}
