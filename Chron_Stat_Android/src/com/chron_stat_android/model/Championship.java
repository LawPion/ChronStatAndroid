package com.chron_stat_android.model;

import java.io.Serializable;

public class Championship implements Serializable {
	private static final long serialVersionUID = -4316122855958782268L;
	
	private String name;
	
	public Championship() {
		super();
	}

	public Championship(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
