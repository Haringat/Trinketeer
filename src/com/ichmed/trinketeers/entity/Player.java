package com.ichmed.trinketeers.entity;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.spell.Spell;
import com.ichmed.trinketeers.util.AxisAllignedBoundingBox;
import com.ichmed.trinketeers.util.render.light.ILight;
import com.ichmed.trinketeers.util.render.light.SimpleLight;
import com.ichmed.trinketeers.world.World;

public class Player extends Entity
{
	public int shotCooldownLeft = 0, shotCooldownRight = 0;
	public float mana = 150f, maxMana = 150.0f;
	public int coins = 0;
	public int fuel = 0;

	public Spell currentSpellLeft;
	public Spell currentSpellRight;

	public Player()
	{
		super();
		this.size = new Vector2f(.075f, .075f);
		this.position = new Vector3f(-0.25f, 0, 0);
		this.speed = 0.01f;
		this.preferredSpeed = 0.01f;
		this.name = "player";
		this.maxDamageCooldown = 30;
		this.maxHealth = this.health = 25;
	}

	@Override
	public ILight createLight()
	{
		SimpleLight lightSource = new SimpleLight();
		lightSource.setActive(false);
		lightSource.setColor(new Vector4f(20f, 20f, 4f, 0f));
		return lightSource;
	}

	public float getManaRegen()
	{
		return 0.05f;
	}

	@Override
	public void tick(World world)
	{
		if (this.mana < this.maxMana) this.mana += (float) Math.min(this.getManaRegen(), this.maxMana - this.mana);

		if (fuel > 0)
		{
			this.fuel--;
			((SimpleLight) this.light).setActive(true);
		} else ((SimpleLight) this.light).setActive(false);

		if (Game.isKeyDown(GLFW_KEY_W)) this.direction.y = this.preferredDirection.y = 1;
		else if (Game.isKeyDown(GLFW_KEY_S)) this.direction.y = this.preferredDirection.y = -1f;
		else this.direction.y = this.preferredDirection.y = 0;

		if (Game.isKeyDown(GLFW_KEY_D)) this.direction.x = this.preferredDirection.x = 1f;
		else if (Game.isKeyDown(GLFW_KEY_A)) this.direction.x = this.preferredDirection.x = -1f;
		else this.direction.x = this.preferredDirection.x = 0;
		shotCooldownLeft--;
		shotCooldownRight--;

		if (!this.isDead && world.getListOfIntersectingEntities(this, true).size() > 0) System.out.println("Yay, I intersect! Wait... what? I totally should not be doing that!");

		if (this.currentSpellLeft != null && glfwGetMouseButton(Game.window, GLFW_MOUSE_BUTTON_1) == 1 && shotCooldownLeft <= 0 && this.mana >= currentSpellLeft.getManaCost())
		{
			DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(Game.window, b1, b2);

			Vector2f v = (Vector2f) new Vector2f((float) b1.get(0) - Game.WIDTH / 2, Game.HEIGHT / 2 - (float) b2.get(0)).normalise(null);
			Vector2f v2 = this.getCenter();
			currentSpellLeft.cast(world, this, v2.x, v2.y, v);
			shotCooldownLeft = currentSpellLeft.cooldown;
			mana -= currentSpellLeft.getManaCost();
		}

		if (this.currentSpellRight != null && glfwGetMouseButton(Game.window, GLFW_MOUSE_BUTTON_2) == 1 && shotCooldownRight <= 0 && this.mana >= currentSpellRight.getManaCost())
		{
			DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(Game.window, b1, b2);

			Vector2f v = (Vector2f) new Vector2f((float) b1.get(0) - Game.WIDTH / 2, Game.HEIGHT / 2 - (float) b2.get(0)).normalise(null);
			Vector2f v2 = this.getCenter();
			currentSpellRight.cast(world, this, v2.x, v2.y, v);
			shotCooldownRight = currentSpellRight.cooldown;
			mana -= currentSpellRight.getManaCost();
		}

		super.tick(world);
	}
	
	public void rerollSpells()
	{
		//this.currentSpellLeft = Spell.generateSpell(1000);
		//this.currentSpellRight = Spell.generateSpell(1000);
		this.currentSpellLeft = new Spell();
		this.currentSpellRight = new Spell();
	}

	@Override
	public AxisAllignedBoundingBox getRenderArea()
	{
		return new AxisAllignedBoundingBox(this.position.x - 0.0375f, this.position.y, this.size.x + 0.075f, this.size.y + 0.075f);
	}

}
