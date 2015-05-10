package com.ichmed.trinketeers.util.texturesystem.ktxplugin;

import com.ichmed.trinketeers.util.texturesystem.Texture;
import com.ichmed.trinketeers.util.texturesystem.TextureCodecParameters;

@TextureCodecParameters(extension="ktx", candecode = true, canencode = false)
public abstract class KTXTextureCodec {	

	public static Texture decode(String path) {

		Texture t = load(path);
		//glGenerateMipmap(textype);
		//System.out.printf("texture mipmap created\n");
		t.bind();
		return t;
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

	public native static Texture load(String path);

}
