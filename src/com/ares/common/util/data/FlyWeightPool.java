package com.ares.common.util.data;


public interface FlyWeightPool {
	
	public Object getFlyWeight(Object key); 
	public void addFlyWeight(Object key);
	
}
