package com.chron_stat_android.model;

public class MatchSheet {
	private int homeScore;
	private int awayScore;
	private Fact[] facts;
	
	public MatchSheet() {
		super();
	}
	
	public MatchSheet(Match match) {
		this.homeScore = match.getHomeScore();
		this.awayScore = match.getAwayScore();
		this.facts = match.getFacts();
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
}