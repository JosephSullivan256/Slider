package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

import com.josephsullivan256.gmail.math.linalg.Vec3;
import com.josephsullivan256.gmail.math.linalg.Vec4;

public class BufferObject {
	
	private int id;
	private int type;
	
	private BufferObject(int id, int type) {
		this.id = id;
		this.type = type;
	}
	
	public static BufferObject vbo() {
		int vbo = glGenBuffers();
		return new BufferObject(vbo,GL_ARRAY_BUFFER);
	}
	
	public static BufferObject ebo() {
		int ebo = glGenBuffers();
		return new BufferObject(ebo,GL_ELEMENT_ARRAY_BUFFER);
	}
	
	public BufferObject bind() {
		glBindBuffer(type,id);
		return this;
	}
	
	public BufferObject unbind() {
		glBindBuffer(type,0);
		return this;
	}
	
	public BufferObject bufferData(ByteBuffer values, int usage) {
		glBufferData(type,values,usage);
		return this;
	}
	
	public BufferObject bufferData(byte[] values, int usage) {
		try ( MemoryStack stack = stackPush() ) {
			ByteBuffer buffer = stack.malloc(values.length);
			buffer.put(values);
			return bufferData(buffer,usage);
		}
	}
	
	public BufferObject bufferData(float[] values, int usage) {
		glBufferData(type,values,usage);
		return this;
	}
	
	public BufferObject bufferData(int[] values, int usage) {
		glBufferData(type,values,usage);
		return this;
	}
	
	public BufferObject bufferData(short[] values, int usage) {
		glBufferData(type,values,usage);
		return this;
	}
	
	public BufferObject bufferData(Vec3[] values, int usage) {
		bufferData(vec3Bufferer, values, usage);
		return this;
	}
	
	public BufferObject bufferData(Vec4[] values, int usage) {
		bufferData(vec4Bufferer, values, usage);
		return this;
	}
	
	public <T> BufferObject bufferData(DataBufferer<T> db, T[] values, int usage) {
		db.bufferData(this, values, usage);
		return this;
	}
	
	public static interface DataBufferer<T>{
		public void bufferData(BufferObject bo, T[] values, int usage);
	}
	
	public static DataBufferer<Vec4> vec4Bufferer = (bo,values,usage)->{
		float[] floatValues = new float[values.length*4];
		
		for(int i = 0; i < values.length; i++) {
			floatValues[4*i]   = values[i].x;
			floatValues[4*i+1] = values[i].y;
			floatValues[4*i+2] = values[i].z;
			floatValues[4*i+3] = values[i].w;
		}
		
		bo.bufferData(floatValues, usage);
	};
	
	public static DataBufferer<Vec3> vec3Bufferer = (bo,values,usage)->{
		float[] floatValues = new float[values.length*3];
		
		for(int i = 0; i < values.length; i++) {
			floatValues[3*i]   = values[i].x;
			floatValues[3*i+1] = values[i].y;
			floatValues[3*i+2] = values[i].z;
		}
		
		bo.bufferData(floatValues, usage);
	};
}