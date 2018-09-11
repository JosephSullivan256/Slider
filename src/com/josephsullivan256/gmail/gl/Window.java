package com.josephsullivan256.gmail.gl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	//TODO add fullscreen, add windowed fullscreen
	
	private long window;
	
	public Window(String name, int width, int height) {
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		window = glfwCreateWindow(width, height, name, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Default key callback. Used if not set by user later
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1); //for v-sync
		
		center();
		show();
	}
	
	public void center() {
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
	}
	
	public void show() {
		glfwShowWindow(window);
	}
	
	public void hide() {
		glfwHideWindow(window);
	}
	
	public void hideCursor() {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
	
	public void showCurosr() {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	
	public void setCallback(GLFWKeyCallbackI callback) {
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, callback);
	}
	
	public void setCallback(GLFWCursorPosCallbackI callback) {
		glfwSetCursorPosCallback(window, callback);
	}
	
	public int getPixelWidth() {
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer width = stack.mallocInt(1); // int*
			IntBuffer height = stack.mallocInt(1); // int*
			glfwGetFramebufferSize(window,width,height);
			return width.get(0);
		}
	}
	
	public int getPixelHeight() {
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer width = stack.mallocInt(1); // int*
			IntBuffer height = stack.mallocInt(1); // int*
			glfwGetFramebufferSize(window,width,height);
			return height.get(0);
		}
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public void close() {
		glfwSetWindowShouldClose(window, true);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void destroy() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
	}
}
