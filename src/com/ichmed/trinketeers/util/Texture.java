package com.ichmed.trinketeers.util;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.savefile.DataLoader;

public class Texture {

	private int id;

	public Texture(String path) {
		id = GL11.glGenTextures();
		DataLoader.loadTextureFile(path);
	}

	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void destruct(){
		GL11.glDeleteTextures(id);
	}
	
}
