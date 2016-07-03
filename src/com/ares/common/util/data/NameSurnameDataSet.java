package com.ares.common.util.data;

public class NameSurnameDataSet {

	private StringFlyWeightPool firstName;
	private StringFlyWeightPool surname;
	
	
	public StringFlyWeightPool getFirstName() {
		return firstName;
	}
	public void setFirstName(StringFlyWeightPool firstName) {
		this.firstName = firstName;
	}
	public StringFlyWeightPool getSurname() {
		return surname;
	}
	public void setSurname(StringFlyWeightPool surname) {
		this.surname = surname;
	} 
	
	
	
}
