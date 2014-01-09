package com.chron_stat_android.model;

import java.io.Serializable;

public class Fact implements Serializable {
	
	private static final long serialVersionUID = 5002576145787552941L;
	
	private TypeFact type;
	private int time; 		// in seconde
	private Player player;
	
	public Fact(TypeFact type, int time, Player player){
		this.type = type;
		this.time = time;
		this.player = player;
	}
	
	public TypeFact getType(){
		return type;
	}
	
	public int getTime(){
		return time;
	}
	
	public Player getPlayer(){
		return player;
	}
}
