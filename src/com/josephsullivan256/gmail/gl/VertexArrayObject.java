package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

import com.josephsullivan256.gmail.util.Pair;

public class VertexArrayObject {
	
	private int vao;
	
	public VertexArrayObject() {
		vao = glGenVertexArrays();
	}
	
	@SafeVarargs
	public final void initialize(BufferObject ebo, Pair<BufferObject, VertexAttributes>... pairs) {
		bind();
		
		int loc = 0;
		
		ebo.bind();
		for(Pair<BufferObject,VertexAttributes> pair: pairs){
			pair.a.bind();
			loc += pair.b.apply(loc);
		}
		
		unbind();
		
		for(Pair<BufferObject,VertexAttributes> pair: pairs){
			pair.a.unbind();
		}
		ebo.unbind();
	}
	
	@SafeVarargs
	public final void initialize(Pair<BufferObject, VertexAttributes>... pairs) {
		bind();
		
		int loc = 0;
		for(Pair<BufferObject,VertexAttributes> pair: pairs){
			pair.a.bind();
			loc += pair.b.apply(loc);
		}
		
		unbind();
		
		for(Pair<BufferObject,VertexAttributes> pair: pairs){
			pair.a.unbind();
		}
	}
	
	public void bind() {
		glBindVertexArray(vao);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	public void drawArrays(int count){
		glDrawArrays(GL_TRIANGLES, 0, count);
	}
	
	public void drawElements(int count) {
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
	}
	
	public void drawArraysInstanced(int indexCount, int instanceCount){
		glDrawArraysInstanced(GL_TRIANGLES, 0, indexCount, instanceCount);
	}
	
	public void drawElementsInstanced(int indexCount, int instanceCount){
		glDrawElementsInstanced(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0, instanceCount);
	}
}
