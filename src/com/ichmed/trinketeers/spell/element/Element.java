package com.ichmed.trinketeers.spell.element;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

public class Element
{
	public static HashMap<String, Element> elements = new HashMap<>();
	public HashMap<String, String> values = new HashMap<String, String>();
	
	public Vector3f getColor()
	{
		float r = Float.valueOf(values.get("color_red"));
		float g = Float.valueOf(values.get("color_green"));
		float b = Float.valueOf(values.get("color_blue"));
		return new Vector3f(r, g, b);
	}
	
	public float getDensity()
	{
		return Float.valueOf(values.get("density"));
	}
	
	public boolean shouldBreakOnImpact()
	{
		return Boolean.valueOf(values.get("break_on_impact"));
	}
	
	public float getBaseDamage()
	{
		return Float.valueOf(values.get("damage"));
	}
	
	public float getBrightness()
	{
		return Float.valueOf(values.get("brightness"));
	}
	
	public String getTexture()
	{
		return (String)values.get("texture");
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
