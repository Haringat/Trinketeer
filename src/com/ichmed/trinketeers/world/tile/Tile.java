package com.ichmed.trinketeers.world.tile;

import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.util.render.RenderUtil;
import com.ichmed.trinketeers.world.Chunk;
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
		tiles[2] = new TileWall("wallStone", true, true);
		tiles[3] = new TileWallTop("wallTop", true, false).setRenderInFront(true);
		tiles[4] = new Tile("floorGrass", false, false);
		tiles[5] = new Tile("floorStone", false, false);
	}

	public Tile(String texture, boolean breakable, boolean massive)
	{
		super();
		this.texture = texture;
		this.breakable = breakable;
		this.massive = massive;
	}

	public boolean shouldRenderInFront(World w, int x, int y)
	{
		return this.renderInFront;
	}

	public Tile setRenderInFront(boolean b)
	{
		this.renderInFront = b;
		return this;
	}

	public String getTexture(World w, int x, int y, int z)
	{
		return texture;
	}
	
	public void renderBeneath(World w, int x, int y)
	{
		if(!this.shouldRenderInFront(w, x, y)) render(w, x, y);
	}
	
	public void renderOnTop(World w, int x, int y)
	{
		if(this.shouldRenderInFront(w, x, y))render(w, x, y);
	}

	protected void render(World w, int x, int y)
	{
		float f = 1f / (float) Chunk.chunkSize;
		if (this.getTexture(w, x, y, (int) w.player.position.z) != null)
		{
			glTranslated(f * x, f * y, 0);
			RenderUtil.renderTexturedQuad(0, 0, f, f, this.getTexture(w, x, y, (int) w.player.position.z));
			glTranslated(-f * x, -f * y, 0);
		} else
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(0, 0, 0);
			RenderUtil.drawRect((f * x), (f * y), f, f);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
		}
	}
}
