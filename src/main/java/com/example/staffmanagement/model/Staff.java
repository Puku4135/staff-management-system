package com.example.staffmanagement.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private LocalDate joinDate;
	
	public Staff() {
	}
	
	public Staff(String name, LocalDate joinDate) {
		this.name = name;
		this.joinDate = joinDate;
	}
	
	public Staff(int id, String name, LocalDate joinDate) {
		this.id = id;
		this.name = name;
		this.joinDate = joinDate;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getJoinDate() {
		return joinDate;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}
}