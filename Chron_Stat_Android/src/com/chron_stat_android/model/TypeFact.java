package com.chron_stat_android.model;

public enum TypeFact {
	
	GOAL("but"), 
	TWO_MIN("2min"),
	PENALTY("penalty");
	
	String txtType;

	private TypeFact(String txtType){
		this.txtType = txtType;
	}
	
	public String toString(){
		return txtType;
	}
}
