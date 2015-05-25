package com.ichmed.trinketeers.util.texturesystem.ktxplugin;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.opengl.GL11;

import com.ichmed.trinketeers.util.render.RenderUtil;
import com.ichmed.trinketeers.util.texturesystem.Texture;
import com.ichmed.trinketeers.util.texturesystem.TextureCodecParameters;

@TextureCodecParameters(extension="ktx", candecode = true, canencode = false)
public abstract class KTXTextureCodec {	

	public static Texture decode(String path) {

		Texture t = load(path);
		RenderUtil.checkerror("textureload");
		glActiveTexture(GL_TEXTURE0);
		RenderUtil.checkerror("glActiveTexture");
		t.bind();
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		RenderUtil.checkerror("glTexParameteri");
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		RenderUtil.checkerror("glTexParameteri");
		return t;
/*		ByteBuffer image;
		
		image = ByteBuffer.wrap(pixeldata);
		if( image.array().length == 0){
			throw new IOException("no image data could be loaded.");
		}
		GL11.glBindTexture(GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		float color[] = { 1.0f, 0.0f, 1.0f, 1.0f };
		// glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, FloatBuffer.wrap(color));
		*/
	}

	public native static Texture load(String path);

}
