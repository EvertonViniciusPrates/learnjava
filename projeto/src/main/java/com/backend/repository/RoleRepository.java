package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.Role;

/**
 * RoleRepository
 */
public interface RoleRepository extends JpaRepository<Role, Long> { }  
