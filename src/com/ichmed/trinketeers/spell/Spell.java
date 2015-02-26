package com.ichmed.trinketeers.spell;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.ichmed.trinketeers.ai.BehaviourLight;
import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.entity.Projectile;
import com.ichmed.trinketeers.savefile.data.ElementData;
import com.ichmed.trinketeers.spell.formation.Arc;
import com.ichmed.trinketeers.spell.formation.Formation;
import com.ichmed.trinketeers.spell.formation.Single;
import com.ichmed.trinketeers.world.World;

public class Spell
{
	public Spell childSpell, trailSpell;

	public String element;
	public int trailDelay = -1;
	public int amount;
	public Formation formation;
	public Vector2f size;
	public float speed = 0.001f;
	public int lifespan;
	public int lifespanVar = 0;
	public HashMap<String, Object> args = new HashMap<>();
	public float wobble = 0;
	public String texture = "defaultSpell.png";
	public float manaCost = -1;
	public int maxCooldown = 1000;
	public int timeBetweenShots;
	public int travelBeforeStandStill = -1;
	public boolean destroyOnImpact = true;
	public int cooldown;
	public float charge = 0, chargeRate = 5f;
	public SpellMode mode = SpellMode.PROJECTILE;
	public int durationLeft = -1;
	public Vector2f lockedTarget;

	public int tick(World world, Entity controller)
	{
		this.cooldown--;
		return 0;
	}

	protected Projectile[] createProjectiles(World world, Entity controller, float x, float y, Vector2f direction, int amount)
	{
		Projectile[] p = new Projectile[amount];
		for (int i = 0; i < amount; i++)
			p[i] = createProjectile(world, controller, x, y, direction);
		return p;
	}

	protected final Projectile createProjectile(World world, Entity controller, float x, float y, Vector2f direction)
	{
		Projectile p = new Projectile(world, this.size, this.element, this.childSpell, this.wobble, this.trailSpell);
		p.speed = p.preferredSpeed = this.speed;
		p.direction = p.preferredDirection = new Vector2f(direction);
		
		float f = ElementData.elements.get(this.element).getBrightness();
		if(f > 0)
		{
			Vector3f c = ElementData.elements.get(this.element).getColor();
			p.behaviours.add(new BehaviourLight(world, new Float(c.x * f).toString(), new Float(c.y * f).toString(), new Float(c.z * f).toString(), "0", new Float(f).toString(), "1"));
		}

		p.setController(controller);
		p.setCenter(new Vector3f(x + p.direction.x * (controller.size.x / 2), y + p.direction.y * (controller.size.y / 2), controller.position.z));

		p.lifespan = this.getLifespan();
		p.trailDelay = p.maxTrailDelay = this.trailDelay;
		p.travelBeforeStandStill = this.travelBeforeStandStill;
		p.destroyOnImpact = this.destroyOnImpact;

		return p;
	}

	public float cast(World world, Entity controller, float x, float y, Vector2f target)
	{
		return this.cast(world, controller, x, y, target, this.amount, true);
	}

	public float cast(World world, Entity controller, float x, float y, Vector2f target, int amount, boolean shouldCharge)
	{
		if (this.cooldown > 0) return 0;
		if (SpellMode.isCharged(this.mode) && shouldCharge)
		{
			if (this.charge < this.maxCooldown) this.charge += chargeRate;
			return 0;

		} else
		{
			if (controller.mana > 0 && controller.mana <= this.getManaCost()) return 0;
			Vector2f direction = new Vector2f();
			Vector2f.sub(new Vector2f(target), new Vector2f(controller.getCenter()), direction);
			direction.normalise();
			for (Projectile p : this.formation.apply(this.createProjectiles(world, controller, x, y, direction, amount), world, controller, x, y, direction, this))
				world.spawn(p, false);
			this.cooldown = this.maxCooldown;
			return this.getManaCost();
		}
	}

	public float release(World world, Entity controller, float x, float y, Vector2f target)
	{
		if (this.durationLeft >= 0)
		{
			if (this.durationLeft % timeBetweenShots == 0) this.cast(world, controller, x, y, lockedTarget, this.amount, false);
			this.durationLeft--;
			return 0;
		}
		if (this.mode == SpellMode.PROJECTILE_CHARGE_SIZE && this.charge > 0)
		{
			Vector2f direction = new Vector2f();
			Vector2f.sub(new Vector2f(target), new Vector2f(controller.getCenter()), direction);
			direction.normalise();
			double mod = Math.sqrt(this.charge);
			float ret = (this.charge / 2) * this.getManaCost();
			if (ret > controller.mana)
			{
				this.charge = 0;
				return 0;
			}
			for (Projectile p : this.formation.apply(this.createProjectiles(world, controller, controller.position.x, controller.position.y, direction, this.amount), world, controller,
					controller.position.x, controller.position.y, direction, this))
			{
				p.size.y *= mod;
				p.size.x *= mod;
				world.spawn(p, false);
			}
			this.charge = 0;
			return ret;
		}
		if (this.mode == SpellMode.PROJECTILE_CHARGE_AMOUNT && this.charge > 0)
		{
			Vector2f direction = new Vector2f();
			Vector2f.sub(new Vector2f(target), new Vector2f(controller.getCenter()), direction);
			direction.normalise();
			float ret = (this.charge / 2) * this.getManaCost();
			if (ret > controller.mana)
			{
				this.charge = 0;
				return 0;
			}
			for (Projectile p : this.formation.apply(this.createProjectiles(world, controller, controller.position.x, controller.position.y, direction, (int) (this.amount * (this.charge / 20)) + 1),
					world, controller, controller.position.x, controller.position.y, direction, this))
			{
				world.spawn(p, false);
			}
			this.cooldown = (int) this.charge;
			this.charge = 0;
			return ret;
		}
		if (this.mode == SpellMode.PROJECTILE_CHARGE_SPRAY && this.charge > 0)
		{
			Vector2f direction = new Vector2f();
			Vector2f.sub(new Vector2f(target), new Vector2f(controller.getCenter()), direction);
			direction.normalise();
			float ret = this.getManaCost();
			if (ret > controller.mana)
			{
				this.charge = 0;
				return 0;
			}
			this.args.put("SprayMod", this.charge * 0.05f);
			for (Projectile p : this.formation.apply(this.createProjectiles(world, controller, controller.position.x, controller.position.y, direction, this.amount), world, controller,
					controller.position.x, controller.position.y, direction, this))
			{
				world.spawn(p, false);
			}
			this.cooldown = this.timeBetweenShots;
			this.charge = 0;
			return ret;
		}
		if (this.mode == SpellMode.PROJECTILE_CHARGE_DURATION && this.charge > 0)
		{
			if (this.getManaCost() * (int) (this.charge / 10 + 1) > controller.mana)
			{
				this.charge = 0;
				return 0;
			}

			this.durationLeft = (int) (this.charge / 10 + 1);
			this.charge = 0;
			this.lockedTarget = new Vector2f(target);
			return this.getManaCost() * this.durationLeft;
		}
		return 0;
	}

	public int getLifespan()
	{
		return Math.max(0, lifespan + (int) ((Math.random() - 0.5) * lifespanVar));
	}

	public void calculateManaCost()
	{
		float sizeMod = this.size.x;
		float lifespanMod = (float) Math.sqrt(this.lifespan);
		float despawnMod = destroyOnImpact ? 1 : 3.5f;

		this.manaCost = sizeMod * lifespanMod * amount * despawnMod * ElementData.elements.get(element).getManaMod() * 4;

		if (this.childSpell != null) manaCost += this.childSpell.getManaCost();

		if (this.trailSpell != null) manaCost += this.trailSpell.getManaCost() * (this.lifespan / this.trailDelay);
	}

	public float getManaCost()
	{
		if (manaCost == -1) calculateManaCost();
		return manaCost;
	}

	public boolean isConstant()
	{
		return this.maxCooldown <= 8;
	}

	public String getName()
	{
		String shape = "Thingemagigure";
		if (this.formation instanceof Single) shape = this.isConstant() || SpellMode.isCharged(this.mode) ? " Ray" : " Ball";
		if (this.formation instanceof Arc) shape = this.isConstant() || SpellMode.isCharged(this.mode) ? " Wave" : " Blast";
		String size = "";
		if (this.size.x < 0.03f) size = "Tiny ";
		else if (this.size.x < 0.05f) size = "Small ";
		else if (this.size.x < 0.1f) size = "Medium ";
		else if (this.size.x < .5f) size = "Big ";
		else if (this.size.x < 1f) size = "Giant ";
		else size = "Humongous ";
		return size + this.element + shape;
	}

	public Spell()
	{
		element = (String) ElementData.elements.keySet().toArray()[(int) (ElementData.elements.keySet().size() * Math.random())];
		float manaMod = ElementData.elements.get(element).getManaMod();
		float sizeMod = ElementData.elements.get(element).getSizeMod();
		boolean multi = Math.random() >= 0.5f;
		boolean constant = Math.random() >= 0.5f;
		if (!multi)
		{
			formation = new Single();
			amount = 1;
		} else
		{
			formation = new Arc();
			amount = (int) (Math.random() / manaMod) + 3;
			args.put("Spray", (float) Math.random() * 0.2f + 0.2f);
		}

		if (constant) timeBetweenShots = 1 + (int) (2 * Math.random());
		else timeBetweenShots = (int) (30 * Math.random()) + 30;
		if (SpellMode.isCharged(this.mode)) this.maxCooldown = 40 + (int) (100 * Math.random());
		else this.maxCooldown = timeBetweenShots;

		float f = 0.01f + (float) (0.1 * Math.random()) * sizeMod;
		size = new Vector2f(f, f);
		speed = 0.02f - (float) Math.random() * 0.015f;
		lifespan = 100;
		texture = ElementData.elements.get(element).getTexture();
		destroyOnImpact = ElementData.elements.get(element).shouldBreakOnImpact();
	}

	public static enum SpellMode
	{
		PROJECTILE, PROJECTILE_CHARGE_SIZE, PROJECTILE_CHARGE_AMOUNT, PROJECTILE_CHARGE_DURATION, PROJECTILE_CHARGE_SPRAY, AOE_SINLGE, AOE_MULTI, MINION;

		public static boolean isCharged(SpellMode s)
		{
			return s == PROJECTILE_CHARGE_AMOUNT || s == PROJECTILE_CHARGE_SIZE || s == PROJECTILE_CHARGE_SPRAY || s == PROJECTILE_CHARGE_DURATION;
		}
	}

	public static Spell getSpellFromSaveData(String string)
	{
		//TODO
		return null;
	}
}
