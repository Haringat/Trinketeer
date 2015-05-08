package com.ichmed.trinketeers.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

public class KTXTexture implements ITexture {

	private int textype = 0;
	private int id = 0;
	
	public KTXTexture(String path) throws IOException {
		id = GL11.glGenTextures();

		System.out.printf("id: %d\n", id);
		load(path);
		System.out.printf("id: %d\n", id);
		glTexParameteri(textype, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(textype, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		System.out.printf("texture linear filtering enabled\n");
		glGenerateMipmap(textype);
		System.out.printf("texture mipmap created\n");
		bind();
/*		ByteBuffer image;
		
		System.out.printf("texture created");
		image = ByteBuffer.wrap(pixeldata);
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
		GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1024, 1024, 0, GL_RGBA, GL_BYTE, image);
		System.out.printf("texture loaded into card\n");*/
	}

	private native void load(String path);

	@Override
	public void bind() {
		GL11.glBindTexture(textype, id);
	}

	@Override
	public void destruct() {
		GL11.glDeleteTextures(id);
		
	}
}
