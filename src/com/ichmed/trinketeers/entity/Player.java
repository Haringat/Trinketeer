package com.ichmed.trinketeers.entity;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.ai.BehaviourLight;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.world.World;

public class Player extends Entity
{
	public int coins = 0;
	public int fuel = 0;

	public Spell currentSpellLeft;
	public Spell currentSpellRight;

	BehaviourLight light;

	public Player(World w)
	{
		super(w);
		this.size = new Vector2f(.075f, .075f);
		this.speed = 0.01f;
		this.preferredSpeed = 0.01f;
		this.entityType = "player";
		this.maxDamageCooldown = 30;
		this.maxHealth = this.health = 25;
		light = new BehaviourLight(w, "20", "20", "4", "0", "1.0", "1");
		this.behaviours.add(light);
		this.isSolid = true;
		mana = 150f;
		maxMana = 150.0f;
	}

	public float getManaRegen()
	{
		return 0.15f;
	}

	@Override
	public void tick(World world)
	{
		if (this.mana < this.maxMana) this.mana += (float) Math.min(this.getManaRegen(), this.maxMana - this.mana);

		if (fuel > 0)
		{
			this.fuel--;
			this.light.light.setActive(true);
		} else this.light.light.setActive(false);

		if (Game.isKeyDown(GLFW_KEY_W)) this.direction.y = this.preferredDirection.y = 1;
		else if (Game.isKeyDown(GLFW_KEY_S)) this.direction.y = this.preferredDirection.y = -1f;
		else this.direction.y = this.preferredDirection.y = 0;

		if (Game.isKeyDown(GLFW_KEY_D)) this.direction.x = this.preferredDirection.x = 1f;
		else if (Game.isKeyDown(GLFW_KEY_A)) this.direction.x = this.preferredDirection.x = -1f;
		else this.direction.x = this.preferredDirection.x = 0;

		DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(Game.window, b1, b2);

		float min = Math.min(Game.WIDTH, Game.HEIGHT);

		float x = ((float) b1.get(0) - Game.WIDTH / 2) * (min / (float) Game.WIDTH) + this.getCenter().x;
		float y = (Game.HEIGHT / 2 - (float) b2.get(0)) * (min / (float) Game.HEIGHT) + this.getCenter().y;
		Vector2f v = (Vector2f) new Vector2f(x, y);
		Vector3f v2 = this.getCenter();

		if (this.currentSpellLeft != null)
		{
			this.currentSpellLeft.tick(world, this);

			if (glfwGetMouseButton(Game.window, GLFW_MOUSE_BUTTON_1) == 1) mana -= currentSpellLeft.cast(world, this, v2.x, v2.y, v);
			else mana -= this.currentSpellLeft.release(world, this, v2.x, v2.y, v);

		}

		if (this.currentSpellRight != null)
		{
			this.currentSpellRight.tick(world, this);
			if (glfwGetMouseButton(Game.window, GLFW_MOUSE_BUTTON_2) == 1) mana -= currentSpellRight.cast(world, this, v2.x, v2.y, v);
			else mana -= this.currentSpellRight.release(world, this, v2.x, v2.y, v);
		}

		super.tick(world);
	}

	public void rerollSpells()
	{
		// this.currentSpellLeft = Spell.generateSpell(1000);
		// this.currentSpellRight = Spell.generateSpell(1000);
		this.currentSpellLeft = new Spell();
		this.currentSpellRight = new Spell();
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(this.position.x - 0.0375f, this.position.y, this.size.x + 0.075f, this.size.y + 0.075f);
	}
}
