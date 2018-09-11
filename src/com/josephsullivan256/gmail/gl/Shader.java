package com.josephsullivan256.gmail.gl;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class Shader {
	
	private int shaderProgram;
	
	public Shader(String vs, String fs) {
		// vertex shader
	    int vertexShader = glCreateShader(GL_VERTEX_SHADER);
	    compileShader(vertexShader, vs);
	    
	    // fragment shader
	    int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
	    compileShader(fragmentShader, fs);
	    
	    // link shaders
	    shaderProgram = glCreateProgram();
	    glAttachShader(shaderProgram, vertexShader);
	    glAttachShader(shaderProgram, fragmentShader);
	    glLinkProgram(shaderProgram);
	    
	    // check for linking errors
	    try ( MemoryStack stack = stackPush() ) {
			IntBuffer success = stack.mallocInt(1);
			ByteBuffer infoLog = stack.malloc(512);
			IntBuffer length = stack.mallocInt(2);
			glGetProgramiv(shaderProgram, GL_LINK_STATUS, success);
			if (success.get(0) == 0) {
				glGetProgramInfoLog(shaderProgram, length, infoLog);
				byte[] infoLogChar = new byte[512];
				infoLog.get(infoLogChar);
				String infoLogString = new String(infoLogChar);
				System.out.println("failed to link shaders: " + infoLogString);
			}
	    }
	    
	    glDeleteShader(vertexShader);
	    glDeleteShader(fragmentShader);
	}
	
	private void compileShader(int shader, String src) {
		glShaderSource(shader, src);
	    glCompileShader(shader);
	    
	    // check for success in compilation
	    try ( MemoryStack stack = stackPush() ) {
			IntBuffer success = stack.mallocInt(1);
			ByteBuffer infoLog = stack.malloc(512);
			IntBuffer length = stack.mallocInt(2);
			length.put(512);
			glGetShaderiv(shader, GL_COMPILE_STATUS, success);
			if (success.get(0) == 0) {
				glGetShaderInfoLog(shader, length, infoLog);
				byte[] infoLogChar = new byte[512];
				infoLog.get(infoLogChar);
				String infoLogString = new String(infoLogChar);
				System.out.println("failed to compile shader: " + infoLogString);
			}
		}
	}
	
	public void use() {
		glUseProgram(shaderProgram);
	}
	
	public int getUniformLocation(String name) {
		return glGetUniformLocation(shaderProgram, name);
	}
}
