package com.chron_stat_android.model;

public enum TypeFact {
	
	GOAL("Goal"), 
	TWO_MIN("Foul"),
	YELLOW_CARD("YellowCard"),
	RED_CARD("RedCard"),
	PENALTY("Penalty");
	
	String txtType;

	private TypeFact(String txtType){
		this.txtType = txtType;
	}
	
	public String toString(){
		return txtType;
	}
}
