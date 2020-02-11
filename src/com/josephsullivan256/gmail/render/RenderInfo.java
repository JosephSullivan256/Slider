package com.josephsullivan256.gmail.render;

import com.josephsullivan256.gmail.math.linalg.Vec2;

public class RenderInfo {
	public final Vec2 nearFar;
	public final Vec2 dimensions;
	
	public RenderInfo(Vec2 nearFar, Vec2 dimensions){
		this.nearFar = nearFar;
		this.dimensions = dimensions;
	}
}
