package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.Game;

import static org.lwjgl.glfw.GLFW.*;

import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TrueTypeFont;
import com.ichmed.trinketeers.world.World;

public class LevelExit extends Pickup
{

	@Override
	public boolean pickUp(World w, Player p)
	{
		if(Game.isKeyDown(GLFW_KEY_SPACE))w.nextLevel();
		return false;
	}

	@Override
	public boolean canBePickedUp(World w)
	{
		return super.canBePickedUp(w) && w.currentLevel.getNumberOfEnemies() <= 0;
	}

	@Override
	public String getTextureForState(World w)
	{
		return this.canBePickedUp(w) ? "hatch_open" : "hatch_closed";
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
		if(w.currentLevel.getNumberOfEnemies() == 0)
			GLHelper.renderText(v.x, v.y + 0.05f, "Prees SPACE to enter", 0.001f, 0.001f, TrueTypeFont.ALIGN_CENTER);
		else if(Game.isKeyDown(GLFW_KEY_SPACE) && isPlayerInPickupRange(w.player) && w.currentLevel.getNumberOfEnemies() > 0) 
			GLHelper.renderText(v.x, v.y + 0.05f, "CLOSED!", 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);		
	}

	@Override
	public float getY()
	{
		return 10000000;
	}
	
	
	
	
}
