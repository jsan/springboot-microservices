package com.mello.microservices.departmentservice.repository;

import com.mello.microservices.departmentservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository <Department, Long>
{
}
