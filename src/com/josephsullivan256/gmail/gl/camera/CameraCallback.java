package com.josephsullivan256.gmail.gl.camera;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import com.josephsullivan256.gmail.gl.input.MouseDXCallback;
import com.josephsullivan256.gmail.math.linalg.Vec2;
import com.josephsullivan256.gmail.math.linalg.Vec3;

public class CameraCallback implements GLFWKeyCallbackI, MouseDXCallback {

	private Camera c;
	
	private boolean w, a, s, d, space, shift;
	private boolean up,down,left,right;
	private float dx,dy;
	
	private boolean useMouse;
	
	public static CameraCallback mouseRotationCallback(Camera c){
		return new CameraCallback(c,true);
	}
	
	public static CameraCallback keyRotationCallback(Camera c){
		return new CameraCallback(c,true);
	}
	
	private CameraCallback(Camera c, boolean useMouse) {
		this.c = c;
		this.useMouse = useMouse;
		w=false;
		a=false;
		s=false;
		d=false;
		space=false;
		shift=false;
		
		up=false;
		down=false;
		left=false;
		right=false;
		
		this.dx = 0;
		this.dy = 0;
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW_KEY_W && action == GLFW_PRESS ) {
			w=true;
		}
		if ( key == GLFW_KEY_A && action == GLFW_PRESS ) {
			a=true;
		}
		if ( key == GLFW_KEY_S && action == GLFW_PRESS ) {
			s=true;
		}
		if ( key == GLFW_KEY_D && action == GLFW_PRESS ) {
			d=true;
		}
		if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS ) {
			space=true;
		}
		if ( key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS ) {
			shift=true;
		}
		if ( key == GLFW_KEY_UP && action == GLFW_PRESS ) {
			up=true;
		}
		if ( key == GLFW_KEY_DOWN && action == GLFW_PRESS ) {
			down=true;
		}
		if ( key == GLFW_KEY_LEFT && action == GLFW_PRESS ) {
			left=true;
		}
		if ( key == GLFW_KEY_RIGHT && action == GLFW_PRESS ) {
			right=true;
		}
		
		if ( key == GLFW_KEY_W && action == GLFW_RELEASE ) {
			w=false;
		}
		if ( key == GLFW_KEY_A && action == GLFW_RELEASE ) {
			a=false;
		}
		if ( key == GLFW_KEY_S && action == GLFW_RELEASE ) {
			s=false;
		}
		if ( key == GLFW_KEY_D && action == GLFW_RELEASE ) {
			d=false;
		}
		if ( key == GLFW_KEY_SPACE && action == GLFW_RELEASE ) {
			space=false;
		}
		if ( key == GLFW_KEY_LEFT_SHIFT && action == GLFW_RELEASE ) {
			shift=false;
		}
		if ( key == GLFW_KEY_UP && action == GLFW_RELEASE ) {
			up=false;
		}
		if ( key == GLFW_KEY_DOWN && action == GLFW_RELEASE ) {
			down=false;
		}
		if ( key == GLFW_KEY_LEFT && action == GLFW_RELEASE ) {
			left=false;
		}
		if ( key == GLFW_KEY_RIGHT && action == GLFW_RELEASE ) {
			right=false;
		}
	}

	public boolean isW() {
		return w;
	}

	public boolean isA() {
		return a;
	}

	public boolean isS() {
		return s;
	}

	public boolean isD() {
		return d;
	}

	public boolean isSpace() {
		return space;
	}

	public boolean isShift() {
		return shift;
	}
	
	public Vec3 getMovement() {
		Vec3 m = new Vec3(0,0,0);
		if(w)m=m.plus(Vec3.k);
		if(a)m=m.plus(Vec3.i);
		if(d)m=m.minus(Vec3.i);
		if(s)m=m.minus(Vec3.k);
		if(space)m=m.plus(Vec3.j);
		if(shift)m=m.minus(Vec3.j);
		if(!m.equals(Vec3.zero)) m=m.normalized();
		return m;
	}
	
	public Vec2 getRotation() {
		Vec2 r = Vec2.zero;
		if(useMouse){
			r = new Vec2(-dx,-dy).scaledBy(1f);
			dx = 0;
			dy = 0;
		} else {
			if(up)r=r.plus(new Vec2(0,1));
			if(down)r=r.plus(new Vec2(0,-1));
			if(left)r=r.plus(new Vec2(1,0));
			if(right)r=r.plus(new Vec2(-1,0));
			
			if(!r.equals(Vec2.zero)) r = r.normalized();
		}
		
		return r;
	}

	@Override
	public void invoke(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}
}
