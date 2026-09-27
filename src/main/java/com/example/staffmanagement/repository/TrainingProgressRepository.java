package com.example.staffmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.staffmanagement.model.TrainingProgress;

public interface TrainingProgressRepository
	extends JpaRepository<TrainingProgress, Integer> {
	
	Optional<TrainingProgress> findByStaffIdAndSkillId(
			int staffId, int skillId);
	
	void deleteByStaffId(int staffId);
	
	void deleteBySkillId(int skillId);
}