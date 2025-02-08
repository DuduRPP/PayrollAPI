package com.dudurpp.Payroll.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.dudurpp.Payroll.exceptions.EmployeeNotFoundException;
import com.dudurpp.Payroll.models.Employee;
import com.dudurpp.Payroll.repositories.EmployeeRepository;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    EmployeeController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    List<Employee> getAll() {
        return employeeRepository.findAll();
    }
    @PostMapping("/employees")
    Employee postNewEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }
    @GetMapping("/employees/{id}")
    Employee getOne(@PathVariable Long id) {
        return employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    @PutMapping("employees/{id}")
    Employee replaceEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return employeeRepository.save(employee);
            }).orElseGet(() -> {
                return employeeRepository.save(newEmployee);
            });
    }
    @DeleteMapping("employees/{id}")
    void deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
    }
    

}
