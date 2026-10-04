package com.example.staffmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String showSkillList(
			@RequestParam(required = false) String category,
			Model model) {
		
		if (category == null
				|| category.isEmpty()
				|| category.equals("all")) {
			model.addAttribute("skillList",
				managementService.getSkillList());
			} 
		else {
			model.addAttribute("skillList",
				managementService.searchSkillByCategory(category));
		}
		model.addAttribute("selectedCategory",category);
		return "skill-list";
	}
	
	@PostMapping("/skills")
	public String addSkill(
			@RequestParam String name,
			@RequestParam String category,
			@RequestParam(required = false)
			Integer estimatedDays,
			Model model) {
		
		if (name == null || name.trim().isEmpty()) {			
			model.addAttribute(
				"errorMessage",
				"教育項目名を入力してください。"
			);
			model.addAttribute(
				"skillList",
				managementService.getSkillList()
			);
			return "skill-list";
		}
		
		if (!isValidCategory(category)) {			
			model.addAttribute(
					"errorMessage", "正しいカテゴリを選択してください。"
			);
			model.addAttribute(
					"skillList", managementService.getSkillList()
			);
			return "skill-list";
		}
		
		if (estimatedDays == null || estimatedDays < 1) {			
			model.addAttribute(
				"errorMessage",
				"習得目安は1日以上で入力してください。"
			);
			model.addAttribute(
				"skillList",
				managementService.getSkillList()
			);
			return "skill-list";
		}
		
		managementService.saveSkill(
			new Skill(name.trim(), category, estimatedDays)
			);
		return "redirect:/skills";
	}
	
	@GetMapping("/skills/edit/{id}")
	public String showEditForm(
			@PathVariable int id,
			Model model) {
		
		Skill targetSkill =
				managementService.getSkillById(id);
		
		if (targetSkill == null) {
			return "redirect:/skills";
		}
		
		model.addAttribute(
			"skill",
			targetSkill
		);
		return "skill-edit";
	}
	
	@PostMapping("/skills/edit/{id}")
	public String updateSkill(
			@PathVariable int id,
			@RequestParam String name,
			@RequestParam String category,
			@RequestParam (required = false)
			Integer estimatedDays, Model model) {
		
		Skill targetSkill =
				managementService.getSkillById(id);
		
		if (targetSkill == null) {
			return "redirect:/skills";
		}
		
		if (name == null || name.trim().isEmpty()) {
			model.addAttribute(
					"errorMessage", "教育項目名を入力してください。"
			);
			model.addAttribute(
					"skill", targetSkill
			);
			return "skill-edit";
		}
		
		if (!isValidCategory(category)) {
			model.addAttribute(
					"errorMessage", "正しいカテゴリを選択してください。"
			);
			model.addAttribute(
					"skill", targetSkill
			);
			return "skill-edit";
		}
		
		if (estimatedDays == null || estimatedDays < 1) {
			model.addAttribute(
					"errorMessage", "習得目安は1日以上で入力してください。"
			);
			model.addAttribute(
					"skill", targetSkill
			);
			return "skill-edit";
		}
		
		managementService.updateSkill(
				id, name.trim(), category, estimatedDays);
		return "redirect:/skills";
	}
	
	@PostMapping("/skills/delete/{id}")
	public String deleteSkill(
			@PathVariable int id) {
		
		Skill targetSkill =
				managementService.getSkillById(id);
		
		if (targetSkill == null) {
			return "redirect:/skills";
		}
		
		managementService.deleteSkill(id);
		
		return "redirect:/skills";
	}
	
	private boolean isValidCategory(String category) {
		return category != null && (
				category.equals("接客")
				|| category.equals("調理")
				|| category.equals("清掃")
				|| category.equals("店舗運営")
				|| category.equals("その他")
				);
	}
}

