package com.chron_stat_android.model;

public enum TypeFact {
	
	Goal("Goal"), 
	Foul("Foul"),
	YellowCard("YellowCard"),
	RedCard("RedCard"),
	Penalty("Penalty");
	
	String txtType;

	private TypeFact(String txtType){
		this.txtType = txtType;
	}
	
	public String toString(){
		return txtType;
	}
}
