package com.ichmed.trinketeers.util.texturesystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TextureCodecParameters {
	String extension();
	boolean canencode();
	boolean candecode();
}
