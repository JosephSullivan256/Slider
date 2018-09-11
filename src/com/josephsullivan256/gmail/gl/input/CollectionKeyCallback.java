package com.josephsullivan256.gmail.gl.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public class CollectionKeyCallback implements GLFWKeyCallbackI {
	
	private List<GLFWKeyCallbackI> callbacks;
	
	public CollectionKeyCallback() {
		callbacks = new ArrayList<GLFWKeyCallbackI>();
	}
	
	public void addCallback(GLFWKeyCallbackI callback) {
		callbacks.add(callback);
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		for(GLFWKeyCallbackI callback: callbacks) {
			callback.invoke(window, key, scancode, action, mods);
		}
	}
}
