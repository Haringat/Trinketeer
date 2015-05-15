package com.ichmed.trinketeers.util.render;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Shader {

	private static HashMap<String, Integer> programs = new HashMap<>();

	private int shaderid;
	private int shadertype;

	/**
	 * 
	 * @param path the path to the source
	 * @param shadertype one of
	 * {@link org.lwjgl.opengl.GL20#GL_VERTEX_SHADER VERTEX_SHADER},
	 * {@link org.lwjgl.opengl.GL20#GL_FRAGMENT_SHADER FRAGMENT_SHADER} or
	 * {@link org.lwjgl.opengl.GL32#GL_GEOMETRY_SHADER GEOMETRY_SHADER} 
	 */
	public Shader(String path, int shadertype){
		int shaderid = glCreateShader(shadertype);
		List<String> shadersource;
		try {
			shadersource = Files.readAllLines(Paths.get(path));
		} catch ( IOException e) {
			throw new RuntimeException("the shader file " + path + " was not "
					+ "found.", e);
		}
		String sourcestring = new String();
		for(String line: shadersource){
			sourcestring = line;
		}
		glShaderSource(shaderid, sourcestring);
		glCompileShader(shaderid);
	}
	
	public static void createShaderProgram(int[] shaders, String name){
		int program = glCreateProgram();
		programs.put(name, program);
		for(int i: shaders){
			glAttachShader(program, i);
		}
		//TODO: glBindFragDataLocation
		glLinkProgram(program);
		glUseProgram(program);
	}
	
	public static void useShaderProgram(String name){
		glUseProgram(programs.get(name));
	}

}
