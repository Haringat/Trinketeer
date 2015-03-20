package com.ichmed.trinketeers.world.tile;

import static org.lwjgl.opengl.GL11.glTranslated;

import com.ichmed.trinketeers.util.render.RenderUtil;
import com.ichmed.trinketeers.world.Chunk;
import com.ichmed.trinketeers.world.World;

public class TileWall extends Tile {

	public TileWall(String texture, boolean breakable, boolean massive) {
		super(texture, breakable, massive);
	}

	@Override
	public String getTexture(World w, int x, int y, int z) {
		if (!tiles[Chunk.getTile(w, x, y - 1, z)].massive
				&& Chunk.getTile(w, x, y - 1, (int) w.player.position.z) != 3)
			return this.texture;
		return null;
	}

	@Override
	public boolean shouldRenderInFront(World w, int x, int y) {
		return false;
	}

	public void renderOnTop(World w, int x, int y) {
		if(tiles[Chunk.getTile(w, x, y + 1, (int)w.player.position.z)].massive)return;
		float f = 1f / (float) Chunk.chunkSize;

		glTranslated(f * x, f * (y + 1), 0);
		RenderUtil.renderTexturedQuad(0, 0, f, f, "wallTop");
		glTranslated(-f * x, -f * (y + 1), 0);
	}
}
