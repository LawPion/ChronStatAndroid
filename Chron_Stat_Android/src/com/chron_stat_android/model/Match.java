package com.chron_stat_android.model;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.chron_stat_android.R;
import com.google.gson.Gson;
import android.content.Context;

public class Match implements Serializable {

	private static final long serialVersionUID = -2367056262987197017L;

	private int id;
	private String date;
	private int homeScore;
	private int awayScore;
	private int championship_id;
	private int gym_id;
	private int team_id1_id;
	private int team_id2_id;
	private Team team1; // the current team
	private Team team2; // the opponent team
	private Championship championship;
	private Gym gym;
	private Fact[] facts;
	private boolean isOver;

	public Match() {
		super();
	}

	public Match(int id, String date, int homeScore, int awayScore,
			int championship_id, int gym_id, int team_id1_id, int team_id2_id, boolean isOver) {
		super();
		this.id = id;
		this.date = date;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.championship_id = championship_id;
		this.gym_id = gym_id;
		this.team_id1_id = team_id1_id;
		this.team_id2_id = team_id2_id;
		this.isOver = isOver;
	}

	public Match(int id, String date, int homeScore, int awayScore,
			int championship_id, int gym_id, int team_id1_id, int team_id2_id,
			Team team1, Team team2) {
		super();
		this.id = id;
		this.date = date;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.championship_id = championship_id;
		this.gym_id = gym_id;
		this.team_id1_id = team_id1_id;
		this.team_id2_id = team_id2_id;
		this.team1 = team1;
		this.team2 = team2;
	}

	public int getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public int getChampionship_id() {
		return championship_id;
	}
	
	public void setHomeScore(int score){
		this.homeScore = score;
	}
	
	public void setAwayScore(int score){
		this.awayScore = score;
	}

	public void setChampionship_id(int id) {
		this.championship_id = id;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public Gym getGym() {
		return gym;
	}

	public void setGym(Gym gym) {
		this.gym = gym;
	}

	public int getGym_id() {
		return gym_id;
	}

	public int getTeam_id1_id() {
		return team_id1_id;
	}

	public int getTeam_id2_id() {
		return team_id2_id;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public Fact[] getFacts() {
		return this.facts;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public void setFacts(ArrayList<Fact> facts) {
		this.facts = facts.toArray(new Fact[0]);
	}

	public void writeToStorage(Context context) {
		Gson gson = new Gson();
		String filename = "match_" + id + context.getString(R.string.JSON_EXT);
		String json = gson.toJson(this, Match.class);
		FileOutputStream outputStream;

		try {
			outputStream = context.openFileOutput(filename,
					Context.MODE_PRIVATE);
			outputStream.write(json.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteFromStorage(Context context) {
		context.deleteFile("match_" + id + context.getString(R.string.JSON_EXT));
	}
}
