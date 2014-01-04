package com.chron_stat_android.model;

public class Team {

	private String name;
	private String homeColor;
	private String awayColor;
	private int clubID;
	
	public Team() {
		super();
	}

	public Team(String name, String homecolor, String awaycolor, int clubID) {
		super();
		this.name = name;
		this.homeColor = homecolor;
		this.awayColor = awaycolor;
		this.clubID = clubID;
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
		return this.name+" ("+this.clubID+")";
	}
}
