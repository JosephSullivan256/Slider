package com.josephsullivan256.gmail.core;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public class ExtraKeyCallback implements GLFWKeyCallbackI {
	
	private boolean p = false;
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW_KEY_P && action == GLFW_PRESS ) {
			p=true;
		}
		
		if ( key == GLFW_KEY_P && action == GLFW_RELEASE ) {
			p=false;
		}
	}
	
	public boolean isPDown(){
		return p;
	}
}
