package com.chron_stat_android.model;

public class MatchSheet {
	private int homeScore;
	private int awayScore;
	private Fact[] facts;
	private int id;
	
	public MatchSheet() {
		super();
	}
	
	public MatchSheet(Match match) {
		this.homeScore = match.getHomeScore();
		this.awayScore = match.getAwayScore();
		this.facts = match.getFacts();
		this.id = match.getId();
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public Fact[] getFacts() {
		return facts;
	}

	public void setFacts(Fact[] facts) {
		this.facts = facts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
