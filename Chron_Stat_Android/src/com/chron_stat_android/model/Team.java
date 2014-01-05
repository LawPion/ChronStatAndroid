package com.chron_stat_android.model;

import java.io.Serializable;

public class Team implements Serializable {

	private static final long serialVersionUID = -3671570499613435693L;
	
	private int id;
	private String name;
	private String homeColor;
	private String awayColor;
	private int clubID;
	
	public Team() {
		super();
	}

	public Team(int id, String name, String homecolor, String awaycolor, int clubID) {
		super();
		this.id = id;
		this.name = name;
		this.homeColor = homecolor;
		this.awayColor = awaycolor;
		this.clubID = clubID;
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
		return clubID;
	}
	
	@Override
	public String toString() {
		return name+" ("+clubID+")";
	}
}
