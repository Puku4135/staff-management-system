package com.example.staffmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "skill")
public class Skill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String category;
	private int estimatedDays;
	
	public Skill() {
	}
	
	public Skill(String name, String category, int estimatedDays) {
		this.name = name;
		this.category = category;
		this.estimatedDays = estimatedDays;
	}
	
	public Skill(
			int id, String name, String category, int estimatedDays) {
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
	
	public void setName(String name) {
		this.name = name;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setEstimatedDays(int estimatedDays) {
		this.estimatedDays = estimatedDays;
	}
	

}
