package com.example.staffmanagement.model;

public class TrainingProgress {
	
	private int staffId;
	private int skillId;
	private SkillStatus status;
	private String trainerName;
	private String memo;
	
	public TrainingProgress(
			int staffId,
			int skillId,
			SkillStatus status) {
	
		this.staffId = staffId;
		this.skillId = skillId;
		this.status = status;
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
