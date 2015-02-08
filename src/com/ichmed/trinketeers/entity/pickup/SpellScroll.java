package com.ichmed.trinketeers.entity.pickup;

import org.lwjgl.glfw.GLFW;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.entity.Player;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.util.render.GLHelper;
import com.ichmed.trinketeers.util.render.TrueTypeFont;
import com.ichmed.trinketeers.world.World;

public class SpellScroll extends Pickup
{
	public Spell spell;

	public SpellScroll(Spell s)
	{
		this.spell = s;
		this.pickupRange = 0.15f;
	}

	public SpellScroll()
	{
		//this(Spell.generateSpell(1000));
		this(new Spell());
	}

	@Override
	public boolean canBePickedUp(World w)
	{
		return super.canBePickedUp(w) && this.isClosestSpecificPickupToPlayer(w);
	}

	@Override
	public boolean pickUp(World world, Player player)
	{
		if (player.currentSpellLeft == null || Game.isKeyDown(GLFW.GLFW_KEY_Q))
		{
			if (player.currentSpellLeft != null)
			{
				SpellScroll scroll = new SpellScroll(player.currentSpellLeft);
				scroll.setCenter(this.getCenter());
				world.spawn(scroll, false);
			}
			player.currentSpellLeft = this.spell;
			return true;
		} else if (player.currentSpellRight == null || Game.isKeyDown(GLFW.GLFW_KEY_E))
		{
			if (player.currentSpellRight != null)
			{
				SpellScroll scroll = new SpellScroll(player.currentSpellRight);
				scroll.setCenter(this.getCenter());
				world.spawn(scroll, false);
			}
			player.currentSpellRight = this.spell;
			return true;
		}
		return false;
	}

	@Override
	public boolean movesTowardPlayer()
	{
		return false;
	}

	@Override
	public void actualRender(World w)
	{
		GLHelper.renderTexturedQuad(this.position.x, this.position.y - 0.03f, this.size.x, this.size.y, "shadow");
		float f = (float) (Math.sin(this.ticksExisted / 100d) * 0.02f);
		GLHelper.renderTexturedQuad(this.position.x, this.position.y + f, this.size.x, this.size.y, "scroll");
		GLHelper.renderTexturedQuad(this.position.x, this.position.y + f, this.size.x, this.size.y, this.spell.element.toLowerCase() + "Projectile");
		if (this.canBePickedUp(w))
		{
			float fx = this.getCenter().x - 0.01f;
			float fy = this.position.y + 0.11f + f;
			GLHelper.renderText(fx, fy + 0.08f, this.spell.getName(), 0.001f, 0.001f, TrueTypeFont.ALIGN_CENTER);
			GLHelper.renderText(fx, fy, "Q  or  E", 0.002f, 0.002f, TrueTypeFont.ALIGN_CENTER);
		}
	}
}
