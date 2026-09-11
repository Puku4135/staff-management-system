package com.example.staffmanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.staffmanagement.model.Skill;
import com.example.staffmanagement.model.SkillStatus;
import com.example.staffmanagement.model.Staff;
import com.example.staffmanagement.model.TrainingProgress;

@Service
public class ManagementService {
	
	private List<Staff> staffList = new ArrayList<>();
	private List<Skill> skillList = new ArrayList<>();
	private List<TrainingProgress> progressList = new ArrayList<>();
	
	public ManagementService() {
		staffList.add(
			new Staff(
				1 ,"山田 太郎", LocalDate.of(2026,9,1)
			)
		);
		staffList.add(
			new Staff(
				2, "佐藤 花子", LocalDate.of(2026,8,20)
			)
		);
		staffList.add(
			new Staff(
				3, "鈴木 一郎", LocalDate.of(2026,7,15)
			)
		);
		
		skillList.add(
			new Skill(
				1, "挨拶", "接客", 1
			)
		);
		skillList.add(
			new Skill(
				2, "レジ操作", "接客", 1
			)
		);
		skillList.add(
			new Skill(
				3, "商品提供", "接客", 2
			)
		);
		skillList.add(
			new Skill(
				4, "基本調理", "清掃", 3
			)
		);
		skillList.add(
			new Skill(
				5, "清掃", "清掃", 2
			)
		);
	}
		
	public List<Staff> getStaffList() {
		return staffList;
	}
	public List<Skill> getSkillList() {
		return skillList;
	}
	public List<TrainingProgress> getProgressList() {
		return progressList;
	}
	
	public TrainingProgress getTrainingProgress(
			int staffId, int skillId) {
		
		for (TrainingProgress progress : progressList) {
			
			if (progress.getStaffId() == staffId
					&& progress.getSkillId() == skillId) {
				
				return progress;
			}
		}
		return null;
	}
	
	public void updateTrainingProgress(
			int staffId,
			int skillId,
			SkillStatus status,
			String trainerName,
			String memo) {
		
		TrainingProgress targetProgress =
				getTrainingProgress(
					staffId,
					skillId
				);
		if (targetProgress == null) {
			targetProgress =
				new TrainingProgress(
					staffId,
					skillId,
					status
				);
			progressList.add(targetProgress);
		}
		targetProgress.setStatus(status);
		targetProgress.setTrainerName(trainerName);
		targetProgress.setMemo(memo);
	}
	
	public SkillStatus getSkillStatus(
			int staffId,
			int skillId) {
		
		TrainingProgress progress =
				getTrainingProgress(
					staffId,
					skillId
				); 
			
			if (progress != null) {
				return progress.getStatus();
			}
		return SkillStatus.NOT_STARTED;
	}
	
	public void updateSkillStatus(
			int staffId, int skillId, SkillStatus status) {
		
		for (TrainingProgress progress : progressList) {
			
			if (progress.getStaffId() == staffId
					&& progress.getSkillId() == skillId) {
				
				progress.setStatus(status);
				return;
			}
		}
		progressList.add(
			new TrainingProgress(
				staffId, skillId, status
			)
		);
	}

}
