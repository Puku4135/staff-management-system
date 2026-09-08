package com.example.staffmanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.staffmanagement.model.Skill;

@Controller
public class SkillController {
	
	private List<Skill> skillList = new ArrayList<>();
	
	public SkillController() {
		
		skillList.add(
			new Skill(
				1, "挨拶", "接客"));
		
		skillList.add(
			new Skill(
				2, "レジ操作", "接客"));
		
		skillList.add(
			new Skill(
				3, "商品提供", "接客"));
		
		skillList.add(
			new Skill(
				4, "基本調理", "調理"));
		
		skillList.add(
			new Skill(
				5, "清掃", "清掃"));
	}
	
	@GetMapping("/skills")
	public String showSkillList(Model model) {
		model.addAttribute("skillList", skillList);
		return "skill-list";
	}
	
	@PostMapping("/skills")
	public String addSkill(
			@RequestParam String name,
			@RequestParam String category) {
		
		skillList.add(
			new Skill(
				skillList.size() + 1,
				name,
				category
			)
		);
		return "redirect:/skills";
	}

}

