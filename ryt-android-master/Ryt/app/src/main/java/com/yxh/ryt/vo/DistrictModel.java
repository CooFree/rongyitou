package com.yxh.ryt.vo;

import java.io.Serializable;

public class DistrictModel implements Serializable {
	private String name;
	private String id;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
