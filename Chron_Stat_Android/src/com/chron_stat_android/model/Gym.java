package com.chron_stat_android.model;

import java.io.Serializable;

public class Gym implements Serializable {
	private static final long serialVersionUID = -4049109196191062918L;
	
	private String name;
	private String adress;
	private String zip;
	private String city;
	
	public Gym() {
		super();
	}
	
	public Gym(String name, String adress, String zip, String city,
			String federation) {
		super();
		this.name = name;
		this.adress = adress;
		this.zip = zip;
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public String getAdress() {
		return adress;
	}

	public String getZip() {
		return zip;
	}

	public String getCity() {
		return city;
	}
}
