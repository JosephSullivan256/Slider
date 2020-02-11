package com.josephsullivan256.gmail.gl;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Texture {
	
	private int texture;
	
	public static Texture createEmpty(int width, int height, int wrapping, int minifyFilter, int magnifyFilter){
		Texture tex = new Texture(wrapping,minifyFilter,magnifyFilter);
		tex.generate(width,height);
		return tex;
	}
	
	public static Texture createEmptyDepth(int width, int height, int wrapping, int minifyFilter, int magnifyFilter){
		Texture tex = new Texture(wrapping,minifyFilter,magnifyFilter);
		tex.generateDepth(width,height);
		return tex;
	}
	
	public Texture(int wrapping, int minifyFilter, int magnifyFilter){
		texture = GL11.glGenTextures();
		bind();
		setWrapping(wrapping);
		setFilters(minifyFilter,magnifyFilter);
	}
	
	public Texture(int wrapping, int minifyFilter, int magnifyFilter, BufferedImage img){
		this(wrapping,minifyFilter,magnifyFilter);
		
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		
		//puts alpha at end rather than beginning, and flips the image upside down (so it starts at bottom left)
		int[] data = new int[width*height];
		for(int i = width*(height-1); i > 0; i-=width){
			for(int i2 = 0; i2 < width; i2++){
				int loc1 = i+i2;
				int loc2 = width*height-i+i2;
				int a = (pixels[loc2] & 0xff000000) >> 24;
				int r = (pixels[loc2] & 0xff0000) >> 16;
				int g = (pixels[loc2] & 0xff00) >> 8;
				int b = (pixels[loc2] & 0xff);
				
				data[loc1] = a << 24 | b << 16 | g << 8 | r;
			}
		}
		
		generate(width,height,data);
		generateMipmap();
	}
	
	public Texture(BufferedImage img){
		this(GL11.GL_CLAMP,GL11.GL_NEAREST,GL11.GL_LINEAR,img);
	}

	private void setWrapping(int wrapping){
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapping);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapping);
	}
	
	private void setFilters(int minifyFilter, int magnifyFilter) {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minifyFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magnifyFilter);
	}
	
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}
	
	public void generate(int width, int height, int[] data){
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
	}
	
	public void generate(int width, int height){
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, NULL);
	}
	
	public void generateDepth(int width, int height){
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, NULL);
	}
	
	public void generateMipmap(){
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
	}
	
	public void assignToUnit(int n){
		GL13.glActiveTexture(GL13.GL_TEXTURE0+n);
		bind();
	}
	
	public void attachToBoundFrameBufferColor(){
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
	}
	
	public void attachToBoundFrameBufferDepth(){
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, texture, 0);
	}
}
