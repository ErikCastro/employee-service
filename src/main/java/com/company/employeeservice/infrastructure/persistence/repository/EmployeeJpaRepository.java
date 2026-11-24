package com.company.employeeservice.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.company.employeeservice.infrastructure.persistence.entity.EmployeeEntity;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {

	@Query("SELECT e FROM EmployeeEntity e WHERE "
			+ "LOWER(COALESCE(e.firstName, '')) LIKE LOWER(CONCAT('%', :name, '%')) OR "
			+ "LOWER(COALESCE(e.middleName, '')) LIKE LOWER(CONCAT('%', :name, '%')) OR "
			+ "LOWER(COALESCE(e.lastName, '')) LIKE LOWER(CONCAT('%', :name, '%')) OR "
			+ "LOWER(COALESCE(e.secondLastName, '')) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<EmployeeEntity> findByName(@Param("name") String name);

}