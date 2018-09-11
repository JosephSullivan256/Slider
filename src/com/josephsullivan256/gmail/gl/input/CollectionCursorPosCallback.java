package com.josephsullivan256.gmail.gl.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class CollectionCursorPosCallback implements GLFWCursorPosCallbackI {
	
	private List<MouseDXCallback> callbacks;
	
	private float x,y,dx,dy;
	
	public CollectionCursorPosCallback() {
		callbacks = new ArrayList<MouseDXCallback>();
		this.x = 0;
		this.y = 0;
		this.dx = dx;
		this.dy = dy;
	}
	
	public void addCallback(MouseDXCallback callback) {
		callbacks.add(callback);
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		dx = (float)xpos-x;
		dy = (float)ypos-y;
		x=(float)xpos;
		y=(float)ypos;
		
		for(MouseDXCallback callback: callbacks){
			callback.invoke(dx, dy);
		}
	}
}
