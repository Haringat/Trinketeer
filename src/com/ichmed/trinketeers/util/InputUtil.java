package com.ichmed.trinketeers.util;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.trinketeers.Game;

public class InputUtil
{
	public static Vector2f getMouseRelativToScreenCenter()
	{

		DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(Game.window, b1, b2);
		
		return new Vector2f(((float) b1.get(0) - Game.WIDTH / 2) * 2 / Game.WIDTH, (Game.HEIGHT / 2 - (float) b2.get(0)) * 2 / Game.HEIGHT);
	}
}
