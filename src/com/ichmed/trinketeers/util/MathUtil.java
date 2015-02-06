package com.ichmed.trinketeers.util;

import org.lwjgl.util.vector.Vector2f;

public class MathUtil
{
	public static float getDistanceBetweenPoints(Vector2f p1, Vector2f p2)
	{

		return new Vector2f(p1.x - p2.x, p1.y - p2.y).length();
	}
}
