package com.josephsullivan256.gmail.gl;

public class Uniform<T> {
	private String name;
	private UniformPasser<T> passer;
	
	public Uniform(String name, UniformPasser<T> passer) {
		this.name = name;
		this.passer = passer;
	}
	
	public int getLocation(Shader s) {
		return s.getUniformLocation(name);
	}
	
	//only call uniform methods after shader.use()
	public void uniform(T t, Shader s) {
		passer.pass(t, getLocation(s));
	}
	
	public void uniform(T t, int location) {
		passer.pass(t, location);
	}
}
