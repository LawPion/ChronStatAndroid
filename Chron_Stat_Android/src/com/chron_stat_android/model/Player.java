package com.chron_stat_android.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	
	private static final long serialVersionUID = -4758151210188430466L;
	
	private int num;
	private String name;
	private int nbrGoal = 0;
	private int nbr2Min = 0;
	
	public static ArrayList<String> playersTeam1 = new ArrayList<String>();
	public static ArrayList<String> playersTeam2 = new ArrayList<String>();
	
	public Player(int num,String name,int team){
		this.num = num;
		this.name = name;
		
		if(team == 1)
			playersTeam1.add(name);
		else
			playersTeam2.add(name);
	}
	
	public int getNum(){
		return num;
	}
	
	public String getName(){
		return name;
	}
	
	public int getNbrGoal(){
		return nbrGoal;
	}
	
	public int getNbr2Min(){
		return nbr2Min;
	}
	
	public void AddGoal(){
		nbrGoal++;
	}

	public void Add2Min(){
		nbr2Min++;
	}
	
	public String toString(){
		return this.name;
	}
}
