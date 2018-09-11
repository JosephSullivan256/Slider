package com.josephsullivan256.gmail.core;

public class Level3DV1 {
    private boolean[][][] level;
    private int x;
    private int y;
    private int z;
    private int iterations;
    private int direction = 0;// 0 = Z, 1 = Up/Down, 2 = Left/Right
    private boolean fD = true; // true = Away/up/Left; False = Toward/Down/Right
    private Coordinate loc;
    private Coordinate cCoord;
    
    public Level3DV1(int x, int y, int z, int iterations) {
        level = new boolean[x][y][z];
        this.x = x;
        this.y = y;
        this.z = z;
        this.iterations = iterations;
        loc = new Coordinate((int) Math.random()*x, (int)Math.random()*y, 0);
        level[loc.getX()][loc.getY()][0] = true; 
        levelGen();
    }
    
    public boolean[][][] getLevel(){
        return level;
    }
    
    private void levelGen(){
        boolean dChanged = false;
        for(int i = 0; i < iterations; i++) {
            while(!dChanged) {
                cCoord = getCoord();
                if(checkViable(cCoord)) {
                    addPath(cCoord);
                    loc = new Coordinate(cCoord.getX(), cCoord.getY(), cCoord.getZ());
                    newDirection();
                    dChanged = true;
                }
                
            }
            dChanged = false;
            direction = newDirection();
        }
    }
    
    private Coordinate getCoord() {
        Coordinate newCoord = null;
        int distance = -1;
        if(direction == 0) { 
            if(fD) {
                distance = (int)(Math.random()*Math.abs(loc.getZ()-z));
                newCoord = new Coordinate(loc.getX(), loc.getY(), loc.getZ()+distance);
            }else {
                distance = (int)(Math.random()*Math.abs(loc.getZ()));
                newCoord = new Coordinate(loc.getX(), loc.getY(), loc.getZ()-distance);
            }
        }else if(direction == 1) {
            if(fD) {
                distance = (int)(Math.random()*Math.abs(loc.getY()-y));
                newCoord = new Coordinate(loc.getX(), loc.getY()+distance, loc.getZ());
            }else {
                distance = (int)(Math.random()*Math.abs(loc.getY()));
                newCoord = new Coordinate(loc.getX(), loc.getY()-distance, loc.getZ());
            }
        }else if(direction == 2) {
            if(fD) {
                distance = (int)(Math.random()*Math.abs(loc.getX()-x));
                newCoord = new Coordinate(loc.getX()+distance, loc.getY(), loc.getZ());
            }else {
                distance = (int)(Math.random()*Math.abs(loc.getX()));
                newCoord = new Coordinate(loc.getX()-distance, loc.getY(), loc.getZ());
            }
        } 
        
        System.out.println(newCoord.getX() + " " + newCoord.getY() + " " + newCoord.getZ());
        
        return newCoord;
    }
    
    private int newDirection(){
        int nD = direction;
        while(nD == direction) {
            nD = (int)(Math.random()*3);
        }
        
        int r = (int)(Math.random()*2);
        fD = (r==0);
        
        return nD;
    }
    
    private boolean checkViable(Coordinate c) {
        int a = 0;
        int b = 0;
        if(direction == 0) {
            if(c.getZ() > loc.getZ()) {
                b = c.getZ();
                a = loc.getZ() + 1;
            }else {
                a = c.getZ() + 1;
                b = loc.getZ();
            }
            
            for(int i = a; i < b; i++) {
                if(level[loc.getX()][loc.getY()][i]) return false;
            }
            
        }else if(direction == 1) {
            if(c.getY() > loc.getY()) {
                b = c.getY();
                a = loc.getY() + 1;
            }else {
                a = c.getY() + 1;
                b = loc.getY();
            }
            
            for(int i = a; i < b; i++) {
                if(level[loc.getX()][i][loc.getZ()]) return false;
            }
            
        }else if(direction == 2) {
            if(c.getX() > loc.getX()) {
                b = c.getX();
                a = loc.getX() + 1;
            }else {
                a = c.getX() + 1;
                b = loc.getX();
            }
            
            for(int i = a; i < b; i++) {
                if(level[i][loc.getY()][loc.getZ()]) return false;
            }
        }
        System.out.println("it's viable");
        return true;
    }
    
    private void addPath(Coordinate c) {
        int a = 0;
        int b = 0;
        if(direction == 0) {
            if(c.getZ() > loc.getZ()) {
                b = c.getZ();
                a = loc.getZ();
            }else {
                a = c.getZ();
                b = loc.getZ();
            }
            
            for(int i = a; i < b; i++) {
                level[loc.getX()][loc.getY()][i] = true;
            }
            
        }else if(direction == 1) {
            if(c.getY() > loc.getY()) {
                b = c.getY();
                a = loc.getY();
            }else {
                a = c.getY();
                b = loc.getY();
            }
            
            for(int i = a; i < b; i++) {
                level[loc.getX()][i][loc.getZ()] = true;
            }
            
        }else if(direction == 2) {
            if(c.getX() > loc.getX()) {
                b = c.getX();
                a = loc.getX();
            }else {
                a = c.getX();
                b = loc.getX();
            }
            
            for(int i = a; i < b; i++) {
                level[i][loc.getY()][loc.getZ()] = true;
            }
        }
        
    }
    
    private static class Coordinate {
        private int x;
        private int y;
        private int z;
        
        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getZ() {
            return z;
        }
        
    }
}
