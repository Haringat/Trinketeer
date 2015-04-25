package com.ichmed.trinketeers.util;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.savefile.DataLoader;

public class Texture {

	private int id;

	public Texture(String path) {
		id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		
		byte[] tmp = DataLoader.loadTextureFile(path);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(tmp.length);
		buffer.put(tmp);
		
		GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 2, 2, 0, GL_RGB, GL_FLOAT, buffer);
		
	}

	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void destruct(){
		GL11.glDeleteTextures(id);
	}
	
}
