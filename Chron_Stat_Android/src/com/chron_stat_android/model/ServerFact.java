package com.chron_stat_android.model;

import java.io.Serializable;

public class ServerFact implements Serializable {
	
	private static final long serialVersionUID = 7406419766650858543L;
	
	private TypeFact type;
	private int time; 		// in seconde
	private int player_id;
	
	public ServerFact() {
		super();
	}
	
	public ServerFact(TypeFact type, int time, int player_id){
		this.type = type;
		this.time = time;
		this.player_id = player_id;
	}
	
	public ServerFact(Fact fact) {
		this.type = fact.getType();
		this.time = fact.getTime();
		this.player_id = fact.getPlayer().getId();
	}
	
	public TypeFact getType(){
		return type;
	}
	
	public int getTime(){
		return time;
	}
	
	public int getPlayerId() {
		return player_id;
	}
}
