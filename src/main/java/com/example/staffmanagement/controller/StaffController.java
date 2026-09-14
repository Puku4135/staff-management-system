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
			@RequestParam(required = false) String keyword,
			Model model) {
		
		if (keyword == null || keyword.trim().isEmpty()) {
			model.addAttribute(
				"staffList",
				managementService.getStaffList()
			);
		} else {
			model.addAttribute(
				"staffList",
				managementService.searchStaff(keyword.trim())
			);
		}
		model.addAttribute("keyword", keyword);
		
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
		
		managementService.getStaffList().add(
			new Staff(
					managementService.getNextStaffId(),
				name.trim(),
				joinDate
			)
		);
		return "redirect:/staff";		
	}
	
	@GetMapping("/staff/edit/{id}")
	public String showEditForm(
			@PathVariable int id,
			Model model) {
		
		Staff targetStaff = null;
		
		for (Staff staff : managementService.getStaffList()) {
			if (staff.getId() == id) {
				targetStaff = staff;
				break;
			}
		}
		model.addAttribute("staff", targetStaff);
		return "staff-edit";
	}
	
	@PostMapping("/staff/edit/{id}")
	public String updateStaff(
			@PathVariable int id,
			@RequestParam String name,
			@RequestParam
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate joinDate) {
		
		for (Staff staff : managementService.getStaffList()) {
			if (staff.getId() == id) {
				staff.setName(name);
				staff.setJoinDate(joinDate);
				break;
			}
		}
		return "redirect:/staff";
	}
	
	@GetMapping("/staff/training/{id}")
	public String showTrainingStatus(
			@PathVariable int id,
			@RequestParam(required = false)
			String statusFilter,
			Model model) {
		
		Staff targetStaff = null;
		
		for (Staff staff : managementService.getStaffList()) {
			if (staff.getId() == id) {
				targetStaff = staff;
				break;
			}
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
		
		managementService.deleteStaff(id);
		
		return "redirect:/staff";
	}
	
	@PostMapping("/staff/training/{staffId}/skill/{skillId}")
	public String updateTrainingprogress(
			@PathVariable int staffId,
			@PathVariable int skillId,
			@RequestParam SkillStatus status,
			@RequestParam String trainerName,
			@RequestParam String memo) {
		
		managementService.updateTrainingProgress(
			staffId,
			skillId,
			status,
			trainerName,
			memo
		);
		return "redirect:/staff/training/" + staffId;
	}

}
