package com.ichmed.trinketeers.spell;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.entity.Entity;
import com.ichmed.trinketeers.spell.element.Element;
import com.ichmed.trinketeers.spell.formation.Formation;
import com.ichmed.trinketeers.spell.formation.Arc;
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
	public int cooldown = 25;
	public int travelBeforeStandStill = -1;
	public boolean destroyOnImpact = true;

	public void cast(World world, Entity controller, float x, float y, Vector2f direction)
	{
		formation.spawn(world, controller, x, y, direction, this);
	}

	public int getLifespan()
	{
		return Math.max(0, lifespan + (int) ((Math.random() - 0.5) * lifespanVar));
	}

	public void calculateManaCost()
	{
		float sizeMod = this.size.x;
		float lifespanMod = (float) Math.sqrt(this.lifespan);
//		float cooldownMod = (float) 50 / cooldown;
		float despawnMod = destroyOnImpact ? 1 : 3.5f;

		this.manaCost = sizeMod * lifespanMod  * amount * despawnMod * Element.elements.get(element).getManaMod() * 4;

//		System.out.printf("Size: %1$f Lifespan: %2$f Cooldown: %3$f Amount: %4$d Despawn: %5$f Manamod: %6$f \n", sizeMod, lifespanMod, cooldownMod, amount, despawnMod, Element.elements.get(element)
//				.getManaMod());

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
		return this.cooldown <= 8;
	}

	public String getName()
	{
		String shape = "Thingemagigure";
		if (this.formation instanceof Single) shape = this.isConstant() ? " Ray" : " Ball";
		if (this.formation instanceof Arc) shape = this.isConstant() ? " Wave" : " Blast";
		String size = "";
		if (this.size.x < 0.03f) size = "Tiny ";
		else if (this.size.x < 0.05f) size = "Small ";
		else if (this.size.x < 0.1f) size = "Medium ";
		else if (this.size.x < .5f) size = "Big ";
		else if (this.size.x < 1f) size = "Giant ";
		else size = "Humongous ";
		return size + this.element + shape;
	}

	public Spell(){
		element = (String) Element.elements.keySet().toArray()[(int) (Element.elements.keySet().size() * Math.random())];
		float manaMod = Element.elements.get(element).getManaMod();
		float sizeMod = Element.elements.get(element).getSizeMod();
		boolean multi = Math.random() >= 0.5f;
		boolean constant = Math.random() >= 0.5f;
		if (!multi)
		{
			formation = new Single();
			amount = 1;
		} else {
			formation = new Arc();
			amount = (int) (Math.random() / manaMod) + 3;
			args.put("Spray", (float) Math.random() * 0.2f + 0.2f);
		}

		if (constant) cooldown = 1 + (int) (2 * Math.random());
		else cooldown = (int) (27 * Math.random()) + 3;

		float f = 0.01f + (float) (0.1 * Math.random()) * sizeMod;
		size = new Vector2f(f, f);
		speed = 0.02f - (float) Math.random() * 0.015f;
		lifespan = 100;
		texture = Element.elements.get(element).getTexture();
		destroyOnImpact = Element.elements.get(element).shouldBreakOnImpact();
	}
	
}
