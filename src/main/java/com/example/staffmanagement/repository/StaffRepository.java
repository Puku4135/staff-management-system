package com.example.staffmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.staffmanagement.model.Staff;

public interface StaffRepository
		extends JpaRepository<Staff, Integer> {
}