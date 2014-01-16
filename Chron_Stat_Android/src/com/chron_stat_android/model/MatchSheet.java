package com.chron_stat_android.model;

public class MatchSheet {
	private int homeScore;
	private int awayScore;

	private ServerFact[] facts;
	private int id;

	public MatchSheet() {
		super();
	}

	public MatchSheet(Match match) {
		this.homeScore = match.getHomeScore();
		this.awayScore = match.getAwayScore();

		Fact[] temp = match.getFacts();
		if (temp != null) {
			this.facts = new ServerFact[temp.length];
			for (int i = 0; i < temp.length; i++) {
				this.facts[i] = new ServerFact(temp[i]);
			}
		}
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

	public ServerFact[] getFacts() {
		return facts;
	}

	public void setFacts(ServerFact[] facts) {
		this.facts = facts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
