package com.ichmed.trinketeers.util.texturesystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import com.ichmed.trinketeers.Game;

/**
 * This is the registry for all format decoder classes. It is also used to instantiate new Textures.<br>
 * Note: It is NOT a texture management class.
 * @author marcel
 * @see com.ichmed.trinketeers.util.render.TextureLibrary TextureLibrary
 * @see Texture
 */
public abstract class TextureCodecRegistry {

	private static HashMap<String, Class<?>> decoders;
	private static HashMap<String, Class<?>> encoders;
	
	static{
		decoders = new HashMap<>();
		encoders = new HashMap<>();
		Reflections ref = new Reflections("com.ichmed.trinketeers.util.texturesystem.ktxplugin");
		Set<Class<?>> classes = ref.getTypesAnnotatedWith(TextureCodecParameters.class);
		for(Class<?> c: classes){
			TextureCodecParameters p = c.getAnnotation(TextureCodecParameters.class);
			if(p.candecode()){
				registerDecoder(p.extension(), c);
			}
			if(p.canencode()){
				registerEncoder(p.extension(), c);
			}
		}
	}


	/**
	 * registers a new Texture format decoder
	 * @param extension the file extension without '.'
	 * @param codec the format decoder class
	 */
	private static void registerDecoder(String extension, Class<?> codec){
		decoders.put(extension, codec);
	}

	/**
	 * registers a new Texture format encoder
	 * @param extension the file extension without '.'
	 * @param codec the format encoder class
	 */
	private static void registerEncoder(String extension, Class<?> codec){
		encoders.put(extension, codec);
		
	}
	
	/**
	 * create a new instance of a texture at the given path
	 * @param source the data source to load the texture from
	 */
	public static Texture createTexture(String source) {
		if(source.lastIndexOf('.') != -1){
			String extension = source.substring(source.lastIndexOf('.')+1);
			if(decoders.containsKey(extension)){
				Class<?> clazz = decoders.get(extension);
				TextureCodecParameters pars = clazz.getAnnotation(TextureCodecParameters.class);
				if(!pars.candecode())
					throw new RuntimeException("the registered codec for "
							+extension+" can not decode.");
				try {
					Method decoder = clazz.getMethod("decode", String.class);
					return (Texture) decoder.invoke(null, source);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException("the class "
							+ clazz.getCanonicalName()
							+" does not follow the method conventions. Thus"
							+" it cannot be used.", e);
				} catch (SecurityException e) {
					Game.logger.throwing(TextureCodecRegistry.class.getName(), "createTexture", e);
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					Game.logger.throwing(TextureCodecRegistry.class.getName(), "createTexture", e);
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					Game.logger.throwing(TextureCodecRegistry.class.getName(), "createTexture", e);
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					Game.logger.throwing(TextureCodecRegistry.class.getName(), "createTexture", e);
					throw new RuntimeException(e);
				}
			}else{
				throw new RuntimeException("The extension "+extension+" not registered at the formatregistry.");
			}
		}else{
			throw new RuntimeException("could not determine file extension of "+source);
		}
	}

}
