package com.example.staffmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.staffmanagement.model.Skill;

public interface SkillRepository
        extends JpaRepository<Skill, Integer> {

}