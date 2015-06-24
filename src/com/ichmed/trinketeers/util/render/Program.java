package com.ichmed.trinketeers.util.render;

import static com.ichmed.trinketeers.util.DataRef.shaderspecs;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL44.*;
import static org.lwjgl.opengl.GL45.*;

import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.util.JSONUtil;

public class Program {

	static void loadShaders(){
		JSONObject shaderspec = JSONUtil.getJSONObjectFromFile(shaderspecs);
		JSONArray verts = shaderspec.getJSONArray("vertexshaders");
		Game.logger.log(Level.FINEST, "loading " + verts.length() + "vertex shaders.");
		for(int i = 0; i < verts.length(); i++){
			JSONObject vert = verts.getJSONObject(i);
			String name = vert.getString("name");
			String loc = vert.getString("loc");
		}
		JSONArray frags = shaderspec.getJSONArray("vertexshaders");
		JSONArray progs = shaderspec.getJSONArray("programs");
		
	}

	int id;
	
	Shader[] objects;

	public Program(Shader[] shaders) {
		id = glCreateProgram();
		RenderUtil.checkerror("Program.<init>:glCreateProgram");
		for(Shader s: shaders){
			glAttachShader(id, s.getShaderID());
			RenderUtil.checkerror("Program.<init>:glAttachShader");
		}
		glLinkProgram(id);
		RenderUtil.checkerror("Program.<init>:glLinkProgram");
	}

	/**
	 * deletes the program from the graphics card.
	 * Beware: references to this program should *NOT* be used anymore after
	 * this method was called as they are invalid.
	 */
	public void delete(){
		for(Shader s: objects){
			glDetachShader(id, s.getShaderID());
			RenderUtil.checkerror("Program.delete:glDetachShader");
		}
		glDeleteProgram(id);
		RenderUtil.checkerror("Program.delete:glDeleteProgram");
	}

	public void use(){
		glUseProgram(id);
		RenderUtil.checkerror("Program.use:glUseProgram");
	}

}
