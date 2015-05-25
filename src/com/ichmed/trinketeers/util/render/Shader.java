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
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.lwjgl.BufferUtils;

import com.ichmed.trinketeers.Game;

public class Shader {

	private static HashMap<String, Integer> programs = new HashMap<>();

	private int shaderid;
	private int shadertype;

	static{
		File shaderfolder = new File("./resc/shaders");
		FilenameFilter shaderfilter = new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if( /*name.toLowerCase().endsWith(".frag")
					||*/ name.toLowerCase().endsWith(".glslf")
					|| name.toLowerCase().endsWith(".vert")
					|| name.toLowerCase().endsWith(".glslv")
					|| name.toLowerCase().endsWith(".geom")
					|| name.toLowerCase().endsWith(".glslg")){
					return true;
				}
				return false;
			}	
		};
		while(!RenderUtil.checkerror("clearing errors1")){}
		File[] shaderfiles = shaderfolder.listFiles(shaderfilter);
		Shader[] shaders = new Shader[shaderfiles.length];
		for(int i = 0; i < shaderfiles.length; i++){
			File f = shaderfiles[i];
			if(f.getAbsolutePath().toLowerCase().endsWith(".frag")
					|| f.getAbsolutePath().toLowerCase().endsWith(".glslf")){
				shaders[i] = new Shader(f.getAbsolutePath(), GL_FRAGMENT_SHADER);
				continue;
			}
			if(f.getAbsolutePath().toLowerCase().endsWith(".vert")
					|| f.getAbsolutePath().toLowerCase().endsWith(".glslv")){
				shaders[i] = new Shader(f.getAbsolutePath(), GL_VERTEX_SHADER);
				continue;
			}
			if(f.getAbsolutePath().toLowerCase().endsWith(".geom")
					|| f.getAbsolutePath().toLowerCase().endsWith(".glslg")){
				shaders[i] = new Shader(f.getAbsolutePath(), GL_GEOMETRY_SHADER);
				continue;
			}
			Game.logger.log(Level.WARNING, "found unknown file in shader folder.");
			Game.logger.log(Level.WARNING, "Name:%s");
			RenderUtil.checkerror("shader");
		}
		linkShaderProgram(shaders, "default");
		RenderUtil.checkerror("shaderlink");
	}

	/**
	 * creates a new shader in the graphics card and compiles it.
	 * @param path the path to the source
	 * @param shadertype one of
	 * {@link org.lwjgl.opengl.GL20#GL_VERTEX_SHADER VERTEX_SHADER},
	 * {@link org.lwjgl.opengl.GL20#GL_FRAGMENT_SHADER FRAGMENT_SHADER},
	 * {@link org.lwjgl.opengl.GL32#GL_GEOMETRY_SHADER GEOMETRY_SHADER},
	 * {@link org.lwjgl.opengl.GL40#GL_TESS_CONTROL_SHADER TESS_CONTROL_SHADER} or
	 * {@link org.lwjgl.opengl.GL40#GL_TESS_EVALUATION_SHADER TESS_EVALUATION_SHADER}
	 */
	private Shader(String path, int shadertype){
		Game.logger.log(Level.FINE, "creating " + 
				(shadertype == GL_FRAGMENT_SHADER ? "fragment shader" :
				shadertype == GL_VERTEX_SHADER ? "vertex shader" :
				shadertype == GL_GEOMETRY_SHADER ? "geometry shader" :
				new String("unknown shader")));
		while(!RenderUtil.checkerror("clearing errors2")){};
		
		int shaderid = glCreateShader(shadertype);
		RenderUtil.checkerror("glCreateShader");
		this.shadertype = shadertype;
		ByteBuffer shadersource;
		try {
			shadersource = ByteBuffer.wrap(Files.readAllBytes(Paths.get(path)));
		} catch ( IOException e) {
			Game.logger.log(Level.WARNING, "the shader file " + path
					+ " was not found.", e);
			return;
		}
		//glShaderSource(shaderid, shadersource.asCharBuffer());
		glShaderSource(shaderid, "#version 330 core\nin vec2 position;\nin vec2 texturecoords;\nout vec2 TextureCoords;\nvoid main(){\nTextureCoords = texturecoords;\ngl_Position = vec4(position, 0.0, 1.0);\n}");
		//glShaderSource(shaderid, 0, shadersource, ByteBuffer.wrap(new byte[]{(byte)shadersource.limit()}));
		//glShaderSource(shaderid, 0, shadersource, null);
		RenderUtil.checkerror("glShaderSource");
		byte[] b1 = shadersource.array();
		System.out.write(b1, 0, b1.length - 1);
		System.out.append('\n');
		
		glCompileShader(shaderid);
		RenderUtil.checkerror("glCompileShader");
		if(glGetShaderi(shaderid, shadertype) != GL_TRUE){
			
			ByteBuffer errbuf = ByteBuffer.allocateDirect(512);
			//ByteBuffer errbuf = BufferUtils.createByteBuffer(512);
			if(!errbuf.isDirect()){
				Game.logger.log(Level.SEVERE, "indirect buffer!!!");
			}
			glGetShaderInfoLog(shaderid, 512, null, errbuf);
			//Game.logger.log(Level.SEVERE, errbuf.asCharBuffer());
			byte[] b = new byte[errbuf.limit() -1 ];
			errbuf.get(b);
			System.out.write(b, 0, b.length - 1);
			RenderUtil.checkerror("glGetShaderInfoLog");
		}
	}
	
	private static void linkShaderProgram(Shader[] shaders, String name){
		while(!RenderUtil.checkerror("clearing errors3")){};
		int program = glCreateProgram();
		RenderUtil.checkerror("glCreateProgram");
		programs.put(name, program);
		for(Shader s: shaders){
			glAttachShader(program, s.shaderid);
			RenderUtil.checkerror("glAttachShader");
		}
		glLinkProgram(program);
		RenderUtil.checkerror("glLinkProgram");
		//TODO: glBindFragDataLocation
		//glBindFragDataLocation();
		glUseProgram(program);
		RenderUtil.checkerror("glUseProgram");
		glValidateProgram(program);
		RenderUtil.checkerror("glValidateProgram");
		
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
