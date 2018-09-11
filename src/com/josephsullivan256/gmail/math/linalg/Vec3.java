package com.josephsullivan256.gmail.math.linalg;

public class Vec3 {
	public final float x, y, z;
	
	public Vec3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3(float azimuth, float zenith) {
		float xsin = (float) Math.sin(azimuth); //azimuth
		float xcos = (float) Math.cos(azimuth);
		float ysin = (float) Math.sin(zenith); //zenith
		float ycos = (float) Math.cos(zenith);
		
		this.x = xsin*ycos;
		this.y = ysin;
		this.z = -xcos*ycos;
	}
	
	public Vec3 plus(Vec3 v){
		return new Vec3(x+v.x,y+v.y,z+v.z);
	}
	
	public Vec3 scaledBy(float f){
		return new Vec3(f*x,f*y,f*z);
	}
	
	public Vec3 minus(Vec3 v){
		return plus(v.scaledBy(-1f));
	}
	
	public float dot(Vec3 v){
		return x*v.x+y*v.y+z*v.z;
	}
	
	public Vec3 cross(Vec3 v) {
		return new Vec3(y*v.z-z*v.y,z*v.x-x*v.z,x*v.y-y*v.x);
	}
	
	public float magnitude2(){
		return dot(this);
	}
	
	public float magnitude(){
		return (float) Math.sqrt(magnitude2());
	}
	
	public Matrix asDirection() {
		return new Matrix(new float[][] {
				{x,y,z,0}
		}).transpose();
	}
	
	public Matrix asPosition() {
		return new Matrix(new float[][] {
			{x,y,z,1}
		}).transpose();
	}
	
	public Vec3 normalized() {
		return scaledBy(1/magnitude());
	}
	
	public String toString() {
		return "<"+x+","+y+","+z+">";
	}
	
	public static final Vec3 zero = new Vec3(0,0,0);
	public static final Vec3 i = new Vec3(1,0,0);
	public static final Vec3 j = new Vec3(0,1,0);
	public static final Vec3 k = new Vec3(0,0,1);
	public static final float epsilon = 0.001f;
	
	public boolean equals(Object obj) {
		if(this==obj) return true;
		Vec3 v = (Vec3) obj;
		return Math.abs(x-v.x)<epsilon && Math.abs(y-v.y)<epsilon && Math.abs(z-v.z)<epsilon;
	}
}