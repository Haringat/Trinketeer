package com.ichmed.trinketeers.util.texturesystem.ktxplugin;

import com.ichmed.trinketeers.util.texturesystem.Texture;

import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class KTXTexture extends Texture {

	KTXTexture(int id, int textype){
		super(id, textype);
	}

	@Override
	protected void generateMipmap(){
		glGenerateMipmap(getTextype());
	}

}
