package com.josephsullivan256.gmail.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.josephsullivan256.gmail.gl.BufferObject;
import com.josephsullivan256.gmail.gl.Shader;
import com.josephsullivan256.gmail.gl.Texture;
import com.josephsullivan256.gmail.gl.VertexArrayObject;
import com.josephsullivan256.gmail.gl.VertexAttributes;
import com.josephsullivan256.gmail.util.Pair;
import com.josephsullivan256.gmail.util.Procedure;

public class ScreenRenderProcedure implements Procedure {

	private Shader s;
	private Texture[] textures;
	private Procedure updatingUniforms;
	
	private VertexArrayObject vao;
	
	public ScreenRenderProcedure(Shader s){
		this(s,new Texture[]{});
	}
	
	public ScreenRenderProcedure(Shader s, Texture[] textures){
		this(s,textures,Procedure.nothing);
	}
	
	public ScreenRenderProcedure(Shader s, Texture[] textures, Procedure initialUniforms){
		this(s,textures,initialUniforms,Procedure.nothing);
	}
	
	public ScreenRenderProcedure(Shader s, Texture[] textures, Procedure initialUniforms, Procedure updatingUniforms){
		this.s = s;
		this.textures = textures;
		this.updatingUniforms = updatingUniforms;
		
		this.vao = new VertexArrayObject();
		this.vao.initialize(
				Pair.init(
					BufferObject.vbo().bind().bufferData(
							new float[]{
								-1f,1f,
								-1f,-1f,
								1f,-1f,
								1f,-1f,
								-1f,1f,
								1f,1f,
							}, GL15.GL_STATIC_DRAW
						),
					new VertexAttributes().with(2, GL11.GL_FLOAT)
				)
			);
		
		s.use();
		initialUniforms.run();
	}
	
	@Override
	public void run() {
		vao.bind();
		s.use();
		updatingUniforms.run();
		for(int i = 0; i < textures.length; i++){
			textures[i].assignToUnit(i);
		}
		vao.drawArrays(6); //6=number of vertices :) I <3 magic numbers
		vao.unbind();
	}
}
