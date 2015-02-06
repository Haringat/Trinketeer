package com.ichmed.trinketeers.util.render;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureLibrary
{
	private static HashMap<String, Texture> lib = new HashMap<>();
	
	private static String currentTexture = "";
	
	public static boolean bindTexture(String path)
	{
		if(currentTexture.equals(path)) return true;
		if(!lib.containsKey(path))
		{
			try
			{
				lib.put(path, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path)));
			} catch (IOException e)
			{
				System.out.println("Could not load \"" + path + "\"");
				e.printStackTrace();
				return false;
			}
		}
		lib.get(path).bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		return true;
	}
}
