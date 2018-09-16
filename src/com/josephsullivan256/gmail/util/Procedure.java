package com.josephsullivan256.gmail.util;

public interface Procedure {
	public void run();
	
	public static final Procedure nothing = ()->{};
}
