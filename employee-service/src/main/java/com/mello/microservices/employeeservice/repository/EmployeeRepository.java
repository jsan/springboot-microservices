package com.mello.microservices.employeeservice.repository;

import com.mello.microservices.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository <Employee, Long>
{
}
