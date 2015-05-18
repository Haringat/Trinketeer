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
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.ichmed.trinketeers.Game;

public class Shader {

	public static HashMap<String, Integer> programs = new HashMap<>();

	private int shaderid;
	private int shadertype;

	static{
		File shaderfolder = new File("./resc/shaders");
		List<Shader> shaders = new ArrayList<>();
		for(File f: shaderfolder.listFiles()){
			if(f.getAbsolutePath().toLowerCase().endsWith(".frag") || f.getAbsolutePath().toLowerCase().endsWith(".glslf")){
				shaders.add(new Shader(f.getAbsolutePath(), GL_FRAGMENT_SHADER));
				continue;
			}
			if(f.getAbsolutePath().toLowerCase().endsWith(".vert") || f.getAbsolutePath().toLowerCase().endsWith(".glslv")){
				shaders.add(new Shader(f.getAbsolutePath(), GL_VERTEX_SHADER));
				continue;
			}
			if(f.getAbsolutePath().toLowerCase().endsWith(".geom") || f.getAbsolutePath().toLowerCase().endsWith(".glslg")){
				shaders.add(new Shader(f.getAbsolutePath(), GL_GEOMETRY_SHADER));
				continue;
			}
			Game.logger.log(Level.WARNING, "found unknown file in shader folder.");
			Game.logger.log(Level.WARNING, "Name:%s");
		}
		int[] shaderarray = new int[shaders.size()];
		linkShaderProgram(shaderarray, "default");
	}

	/**
	 * creates a new shader in the graphics card and compiles it.
	 * @param path the path to the source
	 * @param shadertype one of
	 * {@link org.lwjgl.opengl.GL20#GL_VERTEX_SHADER VERTEX_SHADER},
	 * {@link org.lwjgl.opengl.GL20#GL_FRAGMENT_SHADER FRAGMENT_SHADER} or
	 * {@link org.lwjgl.opengl.GL32#GL_GEOMETRY_SHADER GEOMETRY_SHADER} 
	 */
	private Shader(String path, int shadertype){
		int shaderid = glCreateShader(shadertype);
		this.shadertype = shadertype;
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
		if(glGetShaderi(shaderid, shadertype) != GL_TRUE){
			ByteBuffer errbuf = ByteBuffer.allocateDirect(512);
			glGetShaderInfoLog(shaderid, 512, null, errbuf);
		}
	}
	
	private static void linkShaderProgram(int[] shaders, String name){
		int program = glCreateProgram();
		programs.put(name, program);
		for(int i: shaders){
			glAttachShader(program, i);
		}
		glLinkProgram(program);
		//TODO: glBindFragDataLocation
		//glBindFragDataLocation();
		glUseProgram(program);
	}
	
	public static int getShaderProgramId(String name){
		if(programs.containsKey(name)){
			return programs.get(name);
		} else {
			throw new IllegalArgumentException("The aquired program dows not "
					+ "exist.");
		}
	}
	
	public static void deleteShaderProgram(String name){
		IntBuffer shaders = glGetAttachedShaders(programs.get(name));
		for(int i: shaders.array()){
			glDetachShader(programs.get(name), i);
		}
		glDeleteProgram(programs.get(name));
	}
	
	public int getShaderID(){
		return shaderid;
	}
	
	public static void useShaderProgram(String name){
		if(programs.containsKey(name)){
			glUseProgram(programs.get(name));
		} else {
			throw new IllegalArgumentException("The aquired program dows not "
					+ "exist.");
		}
	}

}
