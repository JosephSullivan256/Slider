package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.List;

public class VertexAttributes {
	
	private List<VertexAttribute> attributes;
	
	public VertexAttributes() {
		attributes = new ArrayList<VertexAttribute>();
	}
	
	public VertexAttributes with(int count, int type) {
		attributes.add(new VertexAttribute(count,type,false));
		return this;
	}
	
	public VertexAttributes withInstanced(int count, int type){
		attributes.add(new VertexAttribute(count,type,true));
		return this;
	}
	
	public int apply(int loc) {
		int stride = 0;
		for(VertexAttribute attrib: attributes) {
			stride+=getSize(attrib.type)*attrib.count;
		}
		
		int offset = 0;
		int location = loc;
		for(VertexAttribute attrib: attributes) {
			glEnableVertexAttribArray(location);
			glVertexAttribPointer(location, attrib.count, attrib.type, false, stride, offset);
			
			if(attrib.instanced){
				glVertexAttribDivisor(location,1);
			}
			
			location++;
			offset+=getSize(attrib.type)*attrib.count;
		}
		
		return location;
	}
	
	public int apply(){
		return apply(0);
	}
	
	private static int getSize(int type) {
		if(type == GL_FLOAT) {
			return 4;
		} else if (type == GL_INT) {
			return 4;
		} else if (type == GL_BYTE) {
			return 1;
		}
		return 0;
	}
	
	private static class VertexAttribute{
		public final int count, type;
		public final boolean instanced;
		public VertexAttribute(int count, int type, boolean instanced) {
			this.count = count;
			this.type = type;
			this.instanced = instanced;
		}
	}
}
