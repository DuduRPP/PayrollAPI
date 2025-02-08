package com.dudurpp.Payroll.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dudurpp.Payroll.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    
}