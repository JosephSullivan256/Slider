package com.josephsullivan256.gmail.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.josephsullivan256.gmail.util.Pair;

public class FrameBuffer {
	private int id;
	
	private FrameBuffer(){
		id = GL30.glGenFramebuffers();
	}
	
	public static Pair<FrameBuffer,Texture> withColor(int width, int height){
		FrameBuffer fb = new FrameBuffer();
		fb.bind();
		Texture col = Texture.createEmpty(width, height, GL11.GL_CLAMP,GL11.GL_NEAREST,GL11.GL_LINEAR);
		col.attachToBoundFrameBufferColor();
		RenderBuffer rb = new RenderBuffer(width,height,GL30.GL_DEPTH24_STENCIL8);
		rb.attachToBoundFrameBuffer(GL30.GL_DEPTH_STENCIL_ATTACHMENT);
		
		if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) System.err.println("FrameBuffer not complete");
		
		fb.unbind();
		
		return Pair.init(fb, col);
	}
	
	//won't work with stencil attachment for renderbuffer
	public static Pair<FrameBuffer,Texture[]> withColorDepth(int width, int height){
		FrameBuffer fb = new FrameBuffer();
		fb.bind();
		Texture[] colorDepth = new Texture[]{
				Texture.createEmpty(width, height, GL11.GL_CLAMP,GL11.GL_NEAREST,GL11.GL_LINEAR),
				Texture.createEmptyDepth(width, height, GL11.GL_CLAMP,GL11.GL_NEAREST,GL11.GL_LINEAR),
		};
		colorDepth[0].attachToBoundFrameBufferColor();
		colorDepth[1].attachToBoundFrameBufferDepth();
		/*RenderBuffer rb = new RenderBuffer(width,height,GL30.GL_DEPTH24_STENCIL8);
		rb.attachToBoundFrameBuffer(GL30.GL_STENCIL_ATTACHMENT);*/
		
		if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) System.err.println("FrameBuffer not complete");
		
		fb.unbind();
		
		return Pair.init(fb, colorDepth);
	}
	
	public void bind(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
	}
	
	public void unbind(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void attachTexture(int attachmentType, Texture tex){
		bind();
	}
	
	private static class RenderBuffer{
		private int id;
		public RenderBuffer(int width, int height, int storageType){
			id = GL30.glGenRenderbuffers();
			bind();
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, storageType, width, height);  
		}
		
		public void bind(){
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
		}
		public void unbind(){
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		}
		public void attachToBoundFrameBuffer(int attachmentType){
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachmentType, GL30.GL_RENDERBUFFER, id);
		}
	}
}
