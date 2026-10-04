package com.example.staffmanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.staffmanagement.model.Skill;
import com.example.staffmanagement.model.SkillStatus;
import com.example.staffmanagement.model.Staff;
import com.example.staffmanagement.model.TrainingProgress;
import com.example.staffmanagement.repository.SkillRepository;
import com.example.staffmanagement.repository.StaffRepository;
import com.example.staffmanagement.repository.TrainingProgressRepository;

@Service
public class ManagementService {
	
	private StaffRepository staffRepository;
	private SkillRepository skillRepository;
	private TrainingProgressRepository trainingProgressRepository;
	
	public ManagementService(
			StaffRepository staffRepository,
			SkillRepository skillRepository,
			TrainingProgressRepository trainingProgressRepository) {
		
		this.staffRepository = staffRepository;
		this.skillRepository = skillRepository;
		this.trainingProgressRepository = trainingProgressRepository;
	}
	
	public List<Staff> getStaffList() {
		return staffRepository.findAll();
	}
	public void saveStaff(Staff staff) {
	    staffRepository.save(staff);
	}
	public Staff getStaffById(int id) {
		
		return staffRepository
				.findById(id).orElse(null);
	}
	public void updateStaff(
			int id, String name, LocalDate joinDate) {
		
		Staff staff = getStaffById(id);
		
		if (staff != null) {
			staff.setName(name);
			staff.setJoinDate(joinDate);
			staffRepository.save(staff);
		}
	}
	
	public List<Skill> getSkillList() {
		return skillRepository.findAll();
	}
	public void saveSkill(Skill skill) {
		skillRepository.save(skill);
	}
	public Skill getSkillById(int id) {
		
		return skillRepository
				.findById(id).orElse(null);
	}
	public void updateSkill(
			int id, String name, String category, int estimatedDays) {
		
		Skill skill = getSkillById(id);
		
		if (skill != null) {
			skill.setName(name);
			skill.setCategory(category);
			skill.setEstimatedDays(estimatedDays);
			skillRepository.save(skill);
		}
	}
	
	public List<TrainingProgress> getProgressList() {
		return trainingProgressRepository.findAll();
	}
	
	public TrainingProgress getTrainingProgress(
			int staffId, int skillId) {
		return trainingProgressRepository
				.findByStaffIdAndSkillId(
					staffId, skillId)
				.orElse(null);
	}
	
	public void updateTrainingProgress(
			int staffId, int skillId, SkillStatus status,
			String trainerName, String memo) {
		TrainingProgress targetProgress = getTrainingProgress(
				staffId, skillId);
		
		if (targetProgress == null) {
			targetProgress = new TrainingProgress(
				staffId, skillId, status);
		}
		targetProgress.setStatus(status);
		targetProgress.setTrainerName(trainerName);
		targetProgress.setMemo(memo);
		trainingProgressRepository.save(
				targetProgress);
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
	
	public void deleteStaff(int staffId) {
		trainingProgressRepository .deleteByStaffId(staffId);
		
		staffRepository.deleteById(staffId);
	}
	
	public void deleteSkill(int skillId) {
		trainingProgressRepository .deleteBySkillId(skillId);
		
		skillRepository.deleteById(skillId);
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
		
		for (Skill skill : skillRepository.findAll()) {
			if (skill.getCategory().equals(category)) {
				resultList.add(skill);
			}
		}
		return resultList;
	}
	
	public List<Skill> searchSkillByStatus(
			int staffId,SkillStatus status) {
		List<Skill> resultList = new ArrayList<>();
		
		for (Skill skill : skillRepository.findAll()) {
			SkillStatus currentStatus = getSkillStatus(
					staffId,skill.getId());
			
			if (currentStatus == status) {
				resultList.add(skill);
			}
		}
		return resultList;
	}
	
	public long getStaffCount() {
	    return staffRepository.count();
	}

	public long getSkillCount() {
	    return skillRepository.count();
	}

	public long getCompletedTrainingCount() {
	    return trainingProgressRepository
	            .countByStatus(SkillStatus.COMPLETED);
	}
	
	public String getStaffTrainingStatus(int staffId) {
		List<Skill> skills = skillRepository.findAll();
		
		if (skills.isEmpty()) {return "NOT_STARTED";}
		
		boolean allNotStarted = true;
		boolean allCompleted = true;
		
		for (Skill skill : skills) {
			SkillStatus status = getSkillStatus(
					staffId, skill.getId());
			
			if (status != SkillStatus.NOT_STARTED) {
				allNotStarted = false;}
			if (status != SkillStatus.COMPLETED) {
				allCompleted = false;}
		}
		
		if (allCompleted) {return "COMPLETED";}
		if (allNotStarted) {return "NOT_STARTED";}
		
		return "IN_PROGRESS";
	}
	
	public long getInProgressStaffCount() {
		long count = 0;
		
		for (Staff staff : staffRepository.findAll()) {
			String trainingStatus = getStaffTrainingStatus(
					staff.getId());
			
			if (trainingStatus.equals("IN_PROGRESS")) {count++;}
		}
		return count;
	}
	
	public List<Staff> getSortedStaffList(String sort) {
		List<Staff> staffList = staffRepository.findAll();
		return sortStaffList(staffList, sort);
	}
	
	public List<Staff> sortStaffList(
			List<Staff> staffList, String sort) {		
		if (sort == null
				|| sort.isEmpty()
				|| sort.equals("id")) {
			staffList.sort((staff1, staff2) -> Integer.compare(
					staff1.getId(), staff2.getId())
			);
		} else if (sort.equals("joinDateDesc")) {
			staffList.sort((staff1, staff2) -> staff2.getJoinDate()
					.compareTo(staff1.getJoinDate())
			);
		} else if (sort.equals("joinDateAsc")) {
			staffList.sort((staff1, staff2) -> staff1.getJoinDate()
					.compareTo(staff2.getJoinDate())
			);
		}
		return staffList;
	}
	
	public int getStaffProgressRate(int staffId) {

	    List<Skill> skillList =
	            skillRepository.findAll();

	    if (skillList.isEmpty()) {
	        return 0;
	    }

	    int completedCount = 0;

	    for (Skill skill : skillList) {

	        SkillStatus status =
	                getSkillStatus(
	                        staffId,
	                        skill.getId()
	                );

	        if (status == SkillStatus.COMPLETED) {
	            completedCount++;
	        }
	    }

	    return completedCount * 100
	            / skillList.size();
	}
}
