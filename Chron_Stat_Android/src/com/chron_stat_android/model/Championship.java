package com.chron_stat_android.model;

import java.io.Serializable;

public class Championship implements Serializable {
	private static final long serialVersionUID = -4316122855958782268L;
	
	private int id;
	private String name;
	
	public Championship() {
		super();
	}

	public Championship(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
