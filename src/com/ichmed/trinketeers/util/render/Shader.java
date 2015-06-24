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
import static com.ichmed.trinketeers.util.DataRef.shaderspecs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector;

import com.ichmed.trinketeers.Game;
import com.ichmed.trinketeers.savefile.DataLoader;
import com.ichmed.trinketeers.util.DataRef;
import com.ichmed.trinketeers.util.JSONUtil;

public class Shader {

    private static HashMap<String, Program> programs = new HashMap<>();
    private static HashMap<String, Shader> verts = new HashMap<>();
    private static HashMap<String, Shader> frags = new HashMap<>();

    private int shaderid;
    private int shadertype;

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
    private Shader(String path, int shadertype, Object[] ins, Object[] uniforms, Object[] outs){
        Game.logger.log(Level.FINE, "creating " + 
                (shadertype == GL_FRAGMENT_SHADER ? "fragment shader" :
                shadertype == GL_VERTEX_SHADER ? "vertex shader" :
                shadertype == GL_GEOMETRY_SHADER ? "geometry shader" :
                new String("unknown shader")));
        while(!RenderUtil.checkerror("clearing errors2")){};
        int shaderid = glCreateShader(shadertype);
        RenderUtil.checkerror("glCreateShader");
        this.shadertype = shadertype;
        StringBuilder source = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line;
            while((line = reader.readLine()) != null){
                source.append(line).append('\n');
            }
        } catch (FileNotFoundException e) {
            
            Game.logger.throwing("Shader", "<init>", e);
        } catch (IOException e) {
            Game.logger.throwing("Shader", "<init>", e);
        }
        glShaderSource(shaderid, source);
        RenderUtil.checkerror("glShaderSource");

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

    public int getShaderID(){
        return shaderid;
    }

}
