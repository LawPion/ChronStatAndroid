package com.chron_stat_android.model;

public enum TypeFact {
	
	GOAL("but"), 
	TWO_MIN("2min"),
	YELLOW_CARD("carton jaune"),
	RED_CARD("carton rouge"),
	PENALTY("penalty");
	
	String txtType;

	private TypeFact(String txtType){
		this.txtType = txtType;
	}
	
	public String toString(){
		return txtType;
	}
}
