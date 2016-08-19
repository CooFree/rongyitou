package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.List;

public class ProvinceModel implements Serializable {
	private String name;
	private List<CityModel> cityList;
	private String id;
	public ProvinceModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public ProvinceModel(String name, List<CityModel> cityList, String id) {
		this.name = name;
		this.cityList = cityList;
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
