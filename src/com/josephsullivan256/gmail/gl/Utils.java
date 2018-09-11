package com.josephsullivan256.gmail.gl;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Utils {
	
	public static String getVersion() {
		return Version.getVersion();
	}
	
	public static void initGLFW() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
	}
	
	public static void terminateGLFW() {
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static void pollGLFWEvents() {
		glfwPollEvents();
	}
	
	public static void initGL() {
		GL.createCapabilities();
	}
	
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static String readFile(String path) throws IOException {
		return readFile(path, StandardCharsets.UTF_8);
	}
}
