package com.chron_stat_android.model;

import java.io.Serializable;

public class Team implements Serializable {

	private static final long serialVersionUID = -3671570499613435693L;
	
	private int id;
	private String name;
	private String homeColor;
	private String awayColor;
	private int club_id;
	private Player[] players;
	
	public Team() {
		super();
	}
	
	public Team(int id, String name, String homeColor, String awayColor,
			int clubID, Player[] players) {
		super();
		this.id = id;
		this.name = name;
		this.homeColor = homeColor;
		this.awayColor = awayColor;
		this.club_id = clubID;
		this.players = players;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getHomecolor() {
		return homeColor;
	}

	public String getAwaycolor() {
		return awayColor;
	}

	public int getClubID() {
		return club_id;
	}
	

	public Player[] getPlayers() {
		return this.players;
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	@Override
	public String toString() {
		return name+" ("+club_id+")";
	}
}
