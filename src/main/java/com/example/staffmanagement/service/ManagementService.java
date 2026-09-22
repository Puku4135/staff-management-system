package com.example.staffmanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.staffmanagement.model.Skill;
import com.example.staffmanagement.model.SkillStatus;
import com.example.staffmanagement.model.Staff;
import com.example.staffmanagement.model.TrainingProgress;
import com.example.staffmanagement.repository.StaffRepository;

@Service
public class ManagementService {
	
	private List<Staff> staffList = new ArrayList<>();
	private List<Skill> skillList = new ArrayList<>();
	private List<TrainingProgress> progressList = new ArrayList<>();
	private StaffRepository staffRepository;
	
	public ManagementService(StaffRepository staffRepository) {
		
		this.staffRepository = staffRepository;
		
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
		return staffRepository.findAll();
	}
	public void saveStaff(Staff staff) {
	    staffRepository.save(staff);
	}
	public Staff getStaffById(int id) {
		
		return staffRepository
				.findById(id)
				.orElse(null);
	}
	public void updateStaff(
			int id,
			String name,
			LocalDate joinDate) {
		
		Staff staff = getStaffById(id);
		
		if (staff != null) {
			staff.setName(name);
			staff.setJoinDate(joinDate);
			staffRepository.save(staff);
		}
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
	
	public void deleteStaff(int staffId) {
		
		staffRepository.deleteById(staffId);
		progressList.removeIf(
			progress -> progress.getStaffId() == staffId);
	}
	
	public void deleteSkill(int skillId) {
		
		skillList.removeIf(
			skill -> skill.getId() == skillId
		);
		progressList.removeIf(
			progress -> progress.getSkillId() == skillId
		);
	}
	
	public int getNextSkillId() {
		int maxId = 0;
		
		for (Skill skill : skillList) {
			
			if (skill.getId() > maxId) {
				maxId = skill.getId();
			}
		}
		return maxId + 1;
	}
	
	public List<Staff> searchStaff(String keyword) {
		List<Staff> resultList = new ArrayList<>();
		
		for (Staff staff : staffRepository.findAll()) {
			if (staff.getName().contains(keyword)) {
				resultList.add(staff);}
		}
		return resultList;
	}
	
	public List<Skill> searchSkillByCategory(String category) {
		List<Skill> resultList = new ArrayList<>();
		
		for (Skill skill : skillList) {
			if (skill.getCategory().equals(category)) {
				resultList.add(skill);
			}
		}
		return resultList;
	}
	
	public List<Skill> searchSkillByStatus(
			int staffId,SkillStatus status) {
		List<Skill> resultList = new ArrayList<>();
		
		for (Skill skill : skillList) {
			SkillStatus currentStatus = getSkillStatus(
					staffId,skill.getId());
			
			if (currentStatus == status) {
				resultList.add(skill);
			}
		}
		return resultList;
	}
}
