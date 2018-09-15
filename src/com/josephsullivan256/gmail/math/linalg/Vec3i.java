package com.josephsullivan256.gmail.math.linalg;

public class Vec3i {
	public final int x,y,z;
	
	public Vec3i(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3i plus(Vec3i v){
		return new Vec3i(x+v.x,y+v.y,z+v.z);
	}
	
	public Vec3i scaledBy(int f){
		return new Vec3i(f*x,f*y,f*z);
	}
	
	public Vec3i normalize(){
		return new Vec3i(x/Math.max(1, Math.abs(x)),y/Math.max(1, Math.abs(y)),z/Math.max(1, Math.abs(z)));
	}
	
	public int dot(Vec3i v){
		return x*v.x+y*v.y+z*v.z;
	}
	
	public int mag2(){
		return dot(this);
	}
	
	public boolean nonNegativeMultiple(Vec3i v){
		int sq = dot(v);
		sq*=sq;
		return sq == mag2()*v.mag2();
	}
	
	public boolean equals(Object obj){
		Vec3i v = (Vec3i) obj;
		return x==v.x && y==v.y && z==v.z;
	}
	
	@Override
	public String toString(){
		return "<"+x+","+y+","+z+">";
	}
}
