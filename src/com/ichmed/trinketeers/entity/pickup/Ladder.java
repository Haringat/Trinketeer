package com.ichmed.trinketeers.entity.pickup;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TrueTypeFont;
import com.ichmed.trinketeers.world.World;

public class Ladder extends Pickup
{
	public boolean down;

	public Ladder(World w, boolean down)
	{
		super(w);
		this.down = down;
	}

	@Override
	public boolean pickUp(World w, Player p)
	{
		if (Game.isKeyDown(GLFW_KEY_SPACE))
		{
			w.removeEntityFromChunk(p);
			p.setCenter(new Vector3f(this.getCenter().x, this.getCenter().y + (down ? -0.125f : 0.125f), this.position.z + (down ? -1 : 1)));
			w.addEntityToChunk(p);
		}
		return false;
	}

	@Override
	public boolean canBePickedUp(World w)
	{
		return super.canBePickedUp(w) && w.getNumberOfEnemies((int) this.position.z) <= 0;
	}

	@Override
	public String getTextureForState(World w)
	{
		return "ladder" + (this.down ? "Down" : "Up") + (this.canBePickedUp(w) ? "Open" : "Closed");
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}

	@Override
	protected void actualRender(World w)
	{
		super.actualRender(w);
		Vector3f v = this.getCenter();
		if (w.getNumberOfEnemies((int) this.position.z) == 0) GLHelper.renderText(v.x, v.y + 0.05f, "Prees SPACE to enter", 0.001f, 0.001f, TrueTypeFont.ALIGN_CENTER);
		else if (Game.isKeyDown(GLFW_KEY_SPACE) && isPlayerInPickupRange(w.player) && w.getNumberOfEnemies((int) this.position.z) > 0)
			GLHelper.renderText(v.x, v.y + 0.05f, "CLOSED!", 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
	}

	@Override
	public float getY()
	{
		return 10000000;
	}

}
