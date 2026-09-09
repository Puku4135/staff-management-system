package com.example.staffmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.staffmanagement.model.Skill;
import com.example.staffmanagement.service.ManagementService;

@Controller
public class SkillController {
	private ManagementService managementService;
	
	public SkillController(ManagementService managementService) {
	    this.managementService = managementService;
	}
	
	@GetMapping("/skills")
	public String showSkillList(Model model) {
		model.addAttribute("skillList", managementService.getSkillList());
		return "skill-list";
	}
	
	@PostMapping("/skills")
	public String addSkill(
			@RequestParam String name,
			@RequestParam String category) {
		
		managementService.getSkillList().add(
			new Skill(
					managementService.getSkillList().size() + 1,
				name,
				category
			)
		);
		return "redirect:/skills";
	}

}

