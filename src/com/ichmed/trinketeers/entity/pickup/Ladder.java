package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.Game;

import static org.lwjgl.glfw.GLFW.*;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TrueTypeFont;
import com.ichmed.trinketeers.world.World;

public class Ladder extends Pickup
{
	public boolean down;

	public Ladder(boolean down)
	{
		this.down = down;
	}
	
	@Override
	public boolean pickUp(World w, Player p)
	{
		if(Game.isKeyDown(GLFW_KEY_SPACE)) p.position.z += down ? -1 : 1;
		return false;
	}

	@Override
	public boolean canBePickedUp(World w)
	{
		return super.canBePickedUp(w) && w.getNumberOfEnemies() <= 0;
	}

	@Override
	public String getTextureForState(World w)
	{
		return this.canBePickedUp(w) ? "hatchOpen" : "hatchClosed";
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
		Vector2f v = this.getCenter();
		if(w.getNumberOfEnemies() == 0)
			GLHelper.renderText(v.x, v.y + 0.05f, "Prees SPACE to enter", 0.001f, 0.001f, TrueTypeFont.ALIGN_CENTER);
		else if(Game.isKeyDown(GLFW_KEY_SPACE) && isPlayerInPickupRange(w.player) && w.getNumberOfEnemies() > 0) 
			GLHelper.renderText(v.x, v.y + 0.05f, "CLOSED!", 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);		
	}

	@Override
	public float getY()
	{
		return 10000000;
	}
	
	
	
	
}
