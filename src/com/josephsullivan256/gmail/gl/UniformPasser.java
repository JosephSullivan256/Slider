package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import com.josephsullivan256.gmail.math.linalg.Matrix;
import com.josephsullivan256.gmail.math.linalg.Vec2;
import com.josephsullivan256.gmail.math.linalg.Vec2i;
import com.josephsullivan256.gmail.math.linalg.Vec3;
import com.josephsullivan256.gmail.math.linalg.Vec4;

public interface UniformPasser<T>{
	public void pass(T t, int location);
	
	public static UniformPasser<Integer> uniform1i = (t,l)->{
		glUniform1i(l, t);
	};
	
	public static UniformPasser<Vec2i> uniform2i = (t,l)->{
		glUniform2i(l, t.x, t.y);
	};
	
	public static UniformPasser<Vec2> uniform2f = (t,l)->{
		glUniform2f(l, t.x, t.y);
	};
	
	public static UniformPasser<Vec3> uniform3f = (t,l)->{
		glUniform3f(l, t.x, t.y, t.z);
	};
	
	public static UniformPasser<Vec4> uniform4f = (t,l)->{
		glUniform4f(l, t.x, t.y, t.z, t.w);
	};
	
	public static UniformPasser<Matrix> uniformMatrix4 = (t,l)->{
		/*try ( MemoryStack stack = stackPush() ) {
			float[][] values = t.getVals();
			FloatBuffer valuesBuffer = stack.mallocFloat(values.length*values[0].length); // float*
			for(int col = 0; col < values[0].length; col++) {
				for(int row = 0; row < values.length; row++) {
					valuesBuffer.put(values[row][col]);
				}
			}
			glUniformMatrix4fv(l, false, valuesBuffer);
		}*/
		
		float[] values = new float[4*4];
		for(int row = 0; row < 4; row++){
			for(int col = 0; col < 4; col++){
				values[col*4+row] = t.getVals()[row][col];
			}
		}
		
		glUniformMatrix4fv(l, false, values);
	};
	
	public static UniformPasser<Vec2[]> uniformVec2 = (t,l)->{
		try ( MemoryStack stack = stackPush() ) {
			Vec2[] values = t;
			FloatBuffer valuesBuffer = stack.mallocFloat(2*values.length); // float*
			for(int i = 0; i < values.length; i++) {
				valuesBuffer.put(values[i].x);
				valuesBuffer.put(values[i].y);
			}
			glUniform2fv(l, valuesBuffer);
		}
	};
	
	public static UniformPasser<float[]> uniformFloatsV2 = (t,l)->{
		glUniform2fv(l, t);
	};

	public static UniformPasser<float[]> uniformFloatsV3 = (t,l)->{
		glUniform3fv(l, t);
	};
}