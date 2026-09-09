package com.example.staffmanagement.controller;

import java.time.LocalDate;
import java.util.HashMap;
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
import com.example.staffmanagement.service.ManagementService;

@Controller
public class StaffController {
	private ManagementService managementService;
	
	public StaffController(ManagementService managementService) {
		this.managementService = managementService;
	}
	
	@GetMapping("/staff")
	public String showStaffList(Model model) {
		
		model.addAttribute("staffList", managementService.getStaffList());
		
		return "staff-list";
	}
	
	@GetMapping("/staff/new")
	public String showStaffForm() {
		return "staff-form";
	}
	
	@PostMapping("/staff")
	public String addStaff(
			@RequestParam String name,
			@RequestParam
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate joinDate) {
		
		managementService.getStaffList().add(
			new Staff(
					managementService.getStaffList().size() + 1,
				name,
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
			Model model) {
		
		Staff targetStaff = null;
		
		for (Staff staff : managementService.getStaffList()) {
			if (staff.getId() == id) {
				targetStaff = staff;
				break;
			}
		}
		
		model.addAttribute("staff", targetStaff);
		model.addAttribute("skillList", managementService.getSkillList());
		
		Map<Integer, String> statusMap = new HashMap<>();
		
		for (Skill skill : managementService.getSkillList()) {
			
			statusMap.put(
					skill.getId(),
					managementService. getSkillStatus(id, skill.getId()). name()
			);
		}
		model.addAttribute("statusMap", statusMap);
		
		return "staff-training";
	}
	
	
	@PostMapping("/staff/delete/{id}")
	public String deleteStaff(
			@PathVariable int id) {
		managementService.getStaffList().removeIf(staff -> staff.getId() == id);
		
		return "redirect:/staff";
	}
	
	@PostMapping("/staff/training/{staffId}/skill/{skillId}")
	public String updateTrainingStatus(
			@PathVariable int staffId,
			@PathVariable int skillId,
			@RequestParam SkillStatus status) {
		
		managementService.updateSkillStatus(
			staffId,
			skillId,
			status
		);
		return "redirect:/staff/training/" + staffId;
	}

}
