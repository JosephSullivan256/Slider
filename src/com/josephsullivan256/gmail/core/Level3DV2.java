package com.josephsullivan256.gmail.core;

import java.util.ArrayList;

import com.josephsullivan256.gmail.math.linalg.Vec3i;

public class Level3DV2 {
	private boolean[][][] level;
	private int x1,y1,z1;
	
	private Vec3i exit;
	private Vec3i dir;
	private ArrayList<Vec3i> path;
	
	public Level3DV2(int x1, int y1, int z1, int iterations){
		level = new boolean[x1][y1][z1];
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		
		generateWalls();
		generateExit();
		
		path = new ArrayList<Vec3i>();
		path.add(exit);
		path.add(random3D(path.get(path.size()-1),dir));
		
		for(int i = 0; i < iterations; i++){
			boolean success = false;
			while(!success){
				Vec3i prev = path.get(path.size()-1);
				
				Vec3i dir2 = randomDir(dir);
				Vec3i next = random3D(prev,dir2);
				
				Vec3i sDir2 = prev.plus(next.scaledBy(-1)).normalize();
				Vec3i block = prev.plus(sDir2);
				if(inhibits(block)){
					set(block,true);
					path.add(next);
					dir = dir2;
					success = true;
				}
			}
		}
	}
	
	private boolean inhibits(Vec3i block){
		boolean success = true;
		for(int i = 0; i < path.size()-1; i++){
			if(between(block,path.get(i),path.get(i+1))) success = false;
		}
		
		return success;
	}
	
	private boolean between(Vec3i block, Vec3i start, Vec3i end){
		Vec3i sb = block.plus(start.scaledBy(-1));
		Vec3i se = end.plus(start.scaledBy(-1));
		
		if(!se.nonNegativeMultiple(sb)) return false;
		
		return sb.mag2() <= se.mag2();
	}

	private void generateWalls(){
		for(int x = 0; x < x1; x++){
			for(int y = 0; y < y1; y++){
				for(int z = 0; z < z1; z++){
					if(!(x>0 && x<x1-1 && y>0 && y<y1-1 && z>0 && z<z1-1)){
						level[x][y][z] = true;
					}
				}
			}
		}
	}
	
	private Vec3i randomDir(Vec3i not){
		Vec3i[] vs = {new Vec3i(1,0,0), new Vec3i(0,1,0), new Vec3i(0,0,1)};
		if(not.equals(vs[0])) return vs[1+(int)(Math.random()*2.0)];
		if(not.equals(vs[1])) return vs[2*(int)(Math.random()*2.0)];
		return vs[(int)(Math.random()*2.0)];
	}
	
	private Vec3i random3D(Vec3i start, Vec3i dir){
		if(dir.x != 0) return new Vec3i(random(1,x1-1),start.y,start.z);
		if(dir.y != 0) return new Vec3i(start.x,random(1,y1-1),start.z);
		return new Vec3i(start.x,start.y,random(1,z1-1));
	}
	
	/*private int random1D(int dir, int start, int lower, int upper){
		if(dir > 0) return random(start,upper);
		return random(lower,start);
	}*/
	
	private int random(int lower, int upper){
		return lower+(int)(Math.random()*(upper-lower));
	}
	
	private void generateExit() {
		int w1 = (x1-2)*(y1-2);
		int w2 = (y1-2)*(z1-2);
		int w3 = (z1-2)*(x1-2);
		
		exit = new Vec3i(0,0,0);
		
		int r = (int)(Math.random()*(w1+w2+w3));
		
		if(r < w1){
			exit = new Vec3i((int)(Math.random()*(x1-2))+1,(int)(Math.random()*(y1-2))+1,(z1-1)*(int)(Math.random()*2));
			dir = new Vec3i(0,0,1);
		} else if (r < w1+w2) {
			exit = new Vec3i((x1-1)*(int)(Math.random()*2),(int)(Math.random()*(y1-2))+1,(int)(Math.random()*(z1-2))+1);
			dir = new Vec3i(1,0,0);
		} else {
			exit = new Vec3i((int)(Math.random()*(x1-2))+1,(y1-1)*(int)(Math.random()*2),(int)(Math.random()*(z1-2))+1);
			dir = new Vec3i(0,1,0);
		}
		
		set(exit,false);
	}
	
	public boolean get(Vec3i pos){
		return level[pos.x][pos.y][pos.z];
	}
	
	public void set(Vec3i pos, boolean value){
		level[pos.x][pos.y][pos.z] = value;
	}
	
	public boolean[][][] getLevel(){
		return level;
	}
}
