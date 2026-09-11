package com.example.staffmanagement.model;

public class Skill {
	
	private int id;
	private String name;
	private String category;
	private int estimatedDays;
	
	public Skill(
			int id,
			String name,
			String category,
			int estimatedDays) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.estimatedDays = estimatedDays;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public int getEstimatedDays() {
		return estimatedDays;
	}
	

}
