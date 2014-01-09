package com.chron_stat_android.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
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

	public void writeToStorage(Context context) {
		// retrieving dependency from server
		String json = null;
		BufferedReader reader = null;
		try {
			URL url = this.uri.toURL();
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			json = buffer.toString();
		} catch (MalformedURLException e) {
			Log.e("retrieve", "retrieveFromServer: " + e.getMessage());
		} catch (IOException e) {
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					Log.e("retrieve", "retrieveFromServer: " + e.getMessage());
				}
		}
		JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
		String dataString = jo.get("data").getAsString();
		byte[] data = Base64.decode(dataString, Base64.DEFAULT);
		File curDir = type == 0 ? getImagesStorageDir(context)
				: getSoundsStorageDir(context);

		// Writing to external storage
		FileOutputStream fileWriter = null;
		String uuid = this.uri.toString().substring(
				MainActivity.SERVER_URL.length(),
				this.uri.toString().indexOf(MainActivity.JSON_EXT));
		File dependencyFile = new File(curDir, uuid + "."
				+ (type == 0 ? "png" : "mp3"));
		if (!dependencyFile.exists()) {
			try {
				dependencyFile.createNewFile();
				fileWriter = new FileOutputStream(dependencyFile);
				fileWriter.write(data);
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				Log.e("retrieve", "writeToFile: " + e.getMessage());
			} finally {
				try {
					if (fileWriter != null)
						fileWriter.close();
				} catch (IOException e) {
					Log.e("retrieve", "writeToFile: " + e.getMessage());
				}
			}
		} else {
			Log.d("retrieve", "dependency already exists in storage: "
					+ this.uri.toString() + " -- " + this.size + "");
		}
	}
}
