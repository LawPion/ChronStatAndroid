package com.chron_stat_android.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import com.chron_stat_android.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

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

	public Match() {
		super();
	}

	public Match(int id, String date, int homeScore, int awayScore,
			int championship_id, int gym_id, int team_id1_id, int team_id2_id) {
		super();
		this.id = id;
		this.date = date;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.championship_id = championship_id;
		this.gym_id = gym_id;
		this.team_id1_id = team_id1_id;
		this.team_id2_id = team_id2_id;
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
	
	public void setFacts(ArrayList<Fact> facts) {
		
	}

	public void writeToStorage(Context context) {
		Gson gson = new Gson();
		String filename = ""+id+R.string.JSON_EXT;
		String json = gson.toJson(this, Match.class);
		FileOutputStream outputStream;

		try {
		  outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(json.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
}
