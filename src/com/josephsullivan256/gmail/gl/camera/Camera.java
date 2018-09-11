package com.josephsullivan256.gmail.gl.camera;

import com.josephsullivan256.gmail.math.linalg.Matrix;
import com.josephsullivan256.gmail.math.linalg.Vec2;
import com.josephsullivan256.gmail.math.linalg.Vec3;
import com.josephsullivan256.gmail.math.linalg.Vec4;

public class Camera {
	
	private Vec3 pos;
	private float azimuth, zenith;
	
	public Camera(Vec3 pos) {
		this.pos = pos;
		this.azimuth = 0;
		this.zenith = 0;
	}
	
	public void rotate(Vec2 r) {
		this.azimuth += r.x;
		this.zenith += r.y;
		if(this.zenith > Math.PI/2f) zenith = (float) Math.PI/2f;
		if(this.zenith < -Math.PI/2f) zenith = -(float) Math.PI/2f;
	}
	
	public void move(Vec3 v) {
		Vec3 forward = new Vec3(-azimuth,0);
		Vec3 perp = forward.cross(Vec3.j);
		forward = Vec3.j.cross(perp);
		Vec3 net = forward.scaledBy(v.z).plus(Vec3.j.scaledBy(v.y)).plus(perp.scaledBy(-v.x));
		pos = pos.plus(net);
	}
	
	private Matrix getRotate() {
		return Matrix.rx44(-zenith).times(Matrix.ry44(-azimuth));
	}
	
	private Matrix getRotateT() {
		return Matrix.rx44(zenith).times(Matrix.ry44(azimuth));
	}
	
	public Matrix getTransform() {
		return getRotate().times(Matrix.t44(pos.scaledBy(-1f)));
	}
}