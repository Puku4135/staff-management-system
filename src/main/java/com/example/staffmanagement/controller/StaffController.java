package com.example.staffmanagement.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.staffmanagement.model.Staff;

@Controller
public class StaffController {
	private List<Staff> staffList = new ArrayList<>();
	
	public StaffController() {
		
		staffList.add(
			new Staff(
				1,"山田 太郎",LocalDate.of(2026,9,1)
			)
		);
			
		staffList.add(
			new Staff(
				2,"佐藤 花子",LocalDate.of(2026,8,20)
			)
		);
			
		staffList.add(
			new Staff(
				3,"鈴木 一郎",LocalDate.of(2026,7,15)
			)
		);
			
	}
	
	@GetMapping("/staff")
	public String showStaffList(Model model) {
		
		model.addAttribute("staffList", staffList);
		
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
		
		staffList.add(
			new Staff(
				staffList.size() + 1,
				name,
				joinDate
			)
		);
		
		return "redirect:/staff";
		
	}

}
