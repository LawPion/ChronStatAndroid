package com.chron_stat_android.model;

import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = -4758151210188430466L;

	// as provided by json model
	private int id;
	private String name;
	private String firstname;
	private String birthday;
	private String phone;
	private boolean sexe;
	private int noDossard;
	private int no_license;
	private String dateQualification;
	
	// auxiliary attributes
	private int nbrGoal = 0;
	private int nbr2Min = 0;

	public Player() {
		super();
	}

	public Player(int id, String name, String firstname, String birthday,
			String phone, boolean sexe, int noDossard, int no_license,
			String dateQualification, int nbrGoal, int nbr2Min) {
		super();
		this.id = id;
		this.name = name;
		this.firstname = firstname;
		this.birthday = birthday;
		this.phone = phone;
		this.sexe = sexe;
		this.noDossard = noDossard;
		this.no_license = no_license;
		this.dateQualification = dateQualification;
		this.nbrGoal = nbrGoal;
		this.nbr2Min = nbr2Min;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getPhone() {
		return phone;
	}

	public boolean isFemale() {
		return sexe;
	}

	public int getNoDossard() {
		return noDossard;
	}

	public int getNo_license() {
		return no_license;
	}

	public String getDateQualification() {
		return dateQualification;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNbrGoal() {
		return nbrGoal;
	}

	public int getNbr2Min() {
		return nbr2Min;
	}

	public void AddGoal() {
		nbrGoal++;
	}

	public void Add2Min() {
		nbr2Min++;
	}

	public String toString() {
		return this.name;
	}
}
