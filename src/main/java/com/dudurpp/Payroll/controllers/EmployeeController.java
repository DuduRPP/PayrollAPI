package com.dudurpp.Payroll.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.dudurpp.Payroll.exceptions.EmployeeNotFoundException;
import com.dudurpp.Payroll.models.Employee;
import com.dudurpp.Payroll.models.EmployeeModelAssembler;
import com.dudurpp.Payroll.repositories.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final EmployeeModelAssembler assembler;

    EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler assembler){
        this.employeeRepository = employeeRepository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> getAll() {
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());        
    }

    @PostMapping("/employees")
    Employee postNewEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getOne(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));

        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).getOne(id)).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")
        );
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
