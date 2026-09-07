package com.example.staffmanagement.model;

import java.time.LocalDate;

public class Staff {

    private int id;
    private String name;
    private LocalDate joinDate;

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