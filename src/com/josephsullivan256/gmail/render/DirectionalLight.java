package com.josephsullivan256.gmail.render;

import com.josephsullivan256.gmail.math.linalg.Vec3;

public class DirectionalLight {
	public final Vec3 dir, amb, diff, spec;
	
	public DirectionalLight(Vec3 dir, Vec3 amb, Vec3 diff, Vec3 spec){
		this.dir = dir;
		this.amb = amb;
		this.diff = diff;
		this.spec = spec;
	}
}
