package com.josephsullivan256.gmail.math.linalg;

public final class Vec4 {
	public final float x,y,z,w;
	
	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public static Vec4 fromRowMatrix(Matrix m) {
		float[][] v = m.getVals();
		return new Vec4(v[0][0],v[0][1],v[0][2],v[0][3]);
	}
	
	public static Vec4 fromColMatrix(Matrix m) {
		return Vec4.fromRowMatrix(m.transpose());
	}
	
	public Vec4(float x, float y, float z) {
		this(x,y,z,0);
	}
	
	public Vec4 plus(Vec4 v) {
		return new Vec4(x+v.x,y+v.y,z+v.z,w+v.w);
	}
	
	public Vec4 scaledBy(float s) {
		return new Vec4(s*x,s*y,s*z,s*w);
	}
	
	public Vec4 minus(Vec4 v) {
		return this.plus(v.scaledBy(-1f));
	}
	
	public float dot(Vec4 v) {
		return x*v.x+y*v.y+z*v.z+w*v.w;
	}
	
	public Vec4 cross(Vec4 v) {
		return new Vec4(y*v.z-z*v.x,z*v.x-x*v.z,x*v.y-y*v.x);
	}
	
	public Matrix asRowMatrix() {
		return new Matrix(new float[][] {{x,y,z,w}});
	}
	
	public Matrix asColumnMatrix() {
		return asRowMatrix().transpose();
	}
}