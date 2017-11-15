package com.ares.common.util.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StringFlyWeightPool {

	private List<String> pool; 
	
	public StringFlyWeightPool() {
		pool = new ArrayList<String>(); 
	}
	
	public String getFlyWeight(int key) {

		String s = pool.get(key);
		return s;
	}

	/*
	 * Each String element is unique. 
	 * When trying to add a new String,
	 * we first check if it already exists
	 * 
	 */
	
	public void addFlyWeight(String key) {

		if (!(pool.contains(key))) {
			pool.add(key); 		
			Collections.sort(pool);
		}
	}
}
