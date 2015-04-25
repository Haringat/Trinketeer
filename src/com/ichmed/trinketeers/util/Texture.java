package com.ichmed.trinketeers.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.savefile.DataLoader;

public class Texture {

	private int id;
	private ByteBuffer image;

	public Texture(String path) throws IOException {
		id = GL11.glGenTextures();

		System.out.printf("texture created");
		image = ByteBuffer.wrap(DataLoader.loadTextureFile(path));
		if( image.array().length == 0){
			throw new IOException("no image data could be loaded.");
		}
		System.out.printf("texture loaded\n");
		GL11.glBindTexture(GL_TEXTURE_2D, id);
		System.out.printf("texture bound\n");
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		System.out.printf("texture wrapped\n");
		float color[] = { 1.0f, 0.0f, 1.0f, 1.0f };
		//glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, FloatBuffer.wrap(color));
		System.out.printf("texture wrap color changed\n");
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		System.out.printf("texture linear filtering enabled\n");
		glGenerateMipmap(GL_TEXTURE_2D);
		System.out.printf("texture mipmap created\n");
		GL11.glTexImage2D(GL_TEXTURE_2D, 1024, GL_RGBA, 1024, 1024, 0, GL_RGBA, GL_BYTE, image);
		System.out.printf("texture loaded into card\n");
		DataLoader.loadTextureFile(path);
	}

	public void bind(){
		GL11.glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void destruct(){
		GL11.glDeleteTextures(id);
	}
	
}
