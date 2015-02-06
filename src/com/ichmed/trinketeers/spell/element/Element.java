package com.ichmed.trinketeers.spell.element;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

public class Element
{
	public static HashMap<String, Element> elements = new HashMap<>();
	
	private float red;
	private float green;
	private float blue;
	private float brightness;
	private float density;
	private boolean breakonimpact;
	private float damage;
	private String texture;
	@SuppressWarnings("unused")
	private float manamod;
	@SuppressWarnings("unused")
	private float sizemod;
	
	public Element(float r, float g, float b, float brightness, float density,
			boolean boi, float dmg, String tex, float manamod, float sizemod){
		red = r;
		green = g;
		blue = b;
		this.density = density;
		this.brightness = brightness;
		breakonimpact = boi;
		texture = tex;
		this.manamod = manamod;
		this.sizemod = sizemod;
	}
	
	public void setColor(float r, float g, float b){
		red = r;
		green = g;
		blue = b;
	}
	
	public void setColor(Vector3f c){
		red = c.x;
		green = c.y;
		blue = c.z;
	}
	
	public void setBrightness(float b){
		brightness = b;
	}
	
	public void setDensity(float d){
		density = d;
	}
	
	public void setBreakOnImpact(boolean b){
		breakonimpact = b;
	}
	
	public void setDamage(float dmg){
		damage = dmg;
	}
	
	public void setTexture(String tex){
		texture = tex;
	}
	
	public void setManaMod(float f){
		manamod = f;
	}
	
	public void setSizeMod(float f){
		sizemod = f;
	}
	
	public Vector3f getColor()
	{
		return new Vector3f(red, green, blue);
	}
	
	public float getDensity()
	{
		return density;
	}
	
	public boolean shouldBreakOnImpact()
	{
		return breakonimpact;
	}
	
	public float getBaseDamage()
	{
		return damage;
	}
	
	public float getBrightness()
	{
		return brightness;
	}
	
	public String getTexture()
	{
		return texture;
	}
	
	public float getManaMod()
	{
		return getBaseDamage() / 30f;
	}
	
	public float getSizeMod()
	{
		return 1.0f;
	}
}
