package com.example.staffmanagement.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.staffmanagement.model.Skill;
import com.example.staffmanagement.model.SkillStatus;
import com.example.staffmanagement.model.Staff;
import com.example.staffmanagement.model.TrainingProgress;
import com.example.staffmanagement.service.ManagementService;

@Controller
public class StaffController {
	private ManagementService managementService;
	
	public StaffController(ManagementService managementService) {
		this.managementService = managementService;
	}
	
	@GetMapping("/staff")
	public String showStaffList(
			@RequestParam(required = false)
			String keyword, 
			@RequestParam(required = false)
			String sort,
			Model model) {
		
		List<Staff> displayStaffList;

		if (keyword == null
		        || keyword.trim().isEmpty()) {

		    displayStaffList =
		            managementService
		                    .getSortedStaffList(sort);

		} else {

		    displayStaffList =
		            managementService.searchStaff(
		                    keyword.trim()
		            );

		    displayStaffList =
		            managementService.sortStaffList(
		                    displayStaffList,
		                    sort
		            );
		}
		model.addAttribute("staffList", displayStaffList);
		model.addAttribute("keyword", keyword);
		model.addAttribute(
		        "selectedSort",
		        sort
		);
		model.addAttribute("staffCount", managementService.getStaffCount());
		model.addAttribute("inProgressStaffCount", managementService.getInProgressStaffCount());
		model.addAttribute("completedTrainingCount", managementService .getCompletedTrainingCount());
		
		Map<Integer, String> staffStatusMap = new HashMap<>();
		
		for (Staff staff : displayStaffList) {
			staffStatusMap.put(
					staff.getId(), managementService .getStaffTrainingStatus(staff.getId())
			);
		}
		model.addAttribute("staffStatusMap", staffStatusMap);
		
		Map<Integer, Integer> progressRateMap =
		        new HashMap<>();

		for (Staff staff : displayStaffList) {

		    progressRateMap.put(
		            staff.getId(),
		            managementService
		                    .getStaffProgressRate(
		                            staff.getId()
		                    )
		    );
		}

		model.addAttribute(
		        "progressRateMap",
		        progressRateMap
		);
		
		return "staff-list";
	}
	
	@GetMapping("/staff/new")
	public String showStaffForm() {
		return "staff-form";
	}
	
	@PostMapping("/staff")
	public String addStaff(
			@RequestParam String name,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate joinDate,
			Model model) {
		
		if (name == null || name.trim().isEmpty()) {
			model.addAttribute(
				"errorMessage",
				"名前を入力してください。"
			);
			return "staff-form";
		}
		
		if (joinDate == null) {
			model.addAttribute(
				"errorMessage",
				"入社日を入力してください。"
			);
			return "staff-form";
		}
		
		managementService.saveStaff(
				new Staff(name.trim(),joinDate)
			);
		return "redirect:/staff";		
	}
	
	@GetMapping("/staff/edit/{id}")
	public String showEditForm(
			@PathVariable int id,
			Model model) {
		
		Staff targetStaff =
		        managementService.getStaffById(id);
		
		if (targetStaff == null) {
			return "redirect:/staff";
		}
		
		model.addAttribute("staff", targetStaff);
		return "staff-edit";
	}
	
	@PostMapping("/staff/edit/{id}")
	public String updateStaff(
			@PathVariable int id,
			@RequestParam String name,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate joinDate, Model model) {
		
		Staff targetStaff =
		        managementService.getStaffById(id);
		
		if (targetStaff == null) {
			return "redirect:/staff";
		}
		
		if (name == null || name.trim().isEmpty()) {
			model.addAttribute(
					"errorMessage", "名前を入力してください。"
			);
			model.addAttribute(
					"staff", targetStaff
			);
			return "staff-edit";
		}
		
		if (joinDate == null) {
			model.addAttribute(
					"errorMessage", "入社日を入力してください。"
			);
			model.addAttribute(
					"staff", targetStaff
			);
			return "staff-edit";
		}
		
		managementService.updateStaff(
				id, name.trim(), joinDate
		);
		return "redirect:/staff";
	}
	
	@GetMapping("/staff/training/{id}")
	public String showTrainingStatus(
			@PathVariable int id,
			@RequestParam(required = false)
			String statusFilter,
			Model model) {
		
		Staff targetStaff = managementService.getStaffById(id);
		
		if (targetStaff == null) {
			return "redirect:/staff";
		}
		
		model.addAttribute("staff", targetStaff);
		
		List<Skill> displaySkillList;
		
		if (statusFilter == null
				|| statusFilter.isEmpty()
				|| statusFilter.equals("all")) {
			displaySkillList =managementService.getSkillList();
			}
		else {
			SkillStatus selectedStatus = SkillStatus.valueOf(statusFilter);
			displaySkillList = managementService.searchSkillByStatus(
					id, selectedStatus);
		}
		
		model.addAttribute("skillList", displaySkillList);
		
		Map<Integer, String> statusMap = new HashMap<>();
		Map<Integer, TrainingProgress> progressMap = new HashMap<>();
		
		for (Skill skill : displaySkillList) {
			
			statusMap.put(
					skill.getId(),
					managementService. getSkillStatus(id, skill.getId()). name()
			);
			TrainingProgress progress =
					managementService.getTrainingProgress(
							id, skill.getId()
						);
			if (progress != null) {
				progressMap.put(
						skill.getId(),progress
						);
			}
		}
		model.addAttribute("statusMap", statusMap);
		model.addAttribute("progressMap", progressMap);
		model.addAttribute("selectedStatus", statusFilter);
		
		return "staff-training";
	}
	
	
	@PostMapping("/staff/delete/{id}")
	public String deleteStaff(
			@PathVariable int id) {
		
		Staff targetStaff =
		        managementService.getStaffById(id);
		
		if (targetStaff == null) {
			return "redirect:/staff";
		}
		
		managementService.deleteStaff(id);
		
		return "redirect:/staff";
	}
	
	@PostMapping("/staff/training/{staffId}/skill/{skillId}")
	public String updateTrainingprogress(
			@PathVariable int staffId,
			@PathVariable int skillId,
			@RequestParam String status,
			@RequestParam String trainerName,
			@RequestParam String memo) {
		
		Staff targetStaff =
		        managementService.getStaffById(staffId);
		
		if (targetStaff == null) {
			return "redirect:/staff";
		}
		
		Skill targetSkill =
		        managementService.getSkillById(skillId);
		
		if (targetSkill == null) {
			return "redirect:/staff/training/" + staffId;
		}
		
		SkillStatus selectedStatus;
		
		try {selectedStatus = SkillStatus.valueOf(status);
		
		} catch (IllegalArgumentException e) {
			return "redirect:/staff/training/" + staffId;
		}
		
		managementService.updateTrainingProgress(
			staffId, skillId, selectedStatus, trainerName, memo
		);
		return "redirect:/staff/training/" + staffId;
	}

}
