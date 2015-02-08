package com.ichmed.trinketeers.world;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TextureLibrary;

public class Tile
{
	public final String texture;
	public final boolean breakable, massive;
	public boolean renderInFront = false;
	public static Tile[] tiles = new Tile[128];

	static
	{
		tiles[0] = new Tile(null, false, false);
		tiles[1] = new Tile("floor_Mud", false, false);
		tiles[2] = new Tile("floor_Mud", false, false).setRenderInFront(true);
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

	public void render(int x, int y)
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
