package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.List;

public class CityModel implements Serializable {
	private String name;
	private List<DistrictModel> districtList;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList, String id) {
		this.name = name;
		this.districtList = districtList;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
