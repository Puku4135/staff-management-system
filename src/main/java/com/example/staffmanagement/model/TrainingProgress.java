package com.example.staffmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "training_Progress")
public class TrainingProgress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int staffId;
	private int skillId;
	@Enumerated(EnumType.STRING)
	private SkillStatus status;
	private String trainerName;
	private String memo;
	
	public TrainingProgress() {
	}
	
	public TrainingProgress(
			int staffId,
			int skillId,
			SkillStatus status) {
	
		this.staffId = staffId;
		this.skillId = skillId;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public int getStaffId() {
		return staffId;
	}
	public int getSkillId() {
		return skillId;
	}
	public SkillStatus getStatus() {
		return status;
	}
	public void setStatus(SkillStatus status) {
		this.status = status;
	}
	public String getTrainerName() {
		return trainerName;
	}
	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
