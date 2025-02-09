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
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<?> postNewEmployee(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(employeeRepository.save(employee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getOne(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PutMapping("employees/{id}")
    ResponseEntity<?> replaceEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
        Employee updatedEmployee = employeeRepository.findById(id)
            .map(employee -> {
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return employeeRepository.save(employee);
            }).orElseGet(() -> {
                return employeeRepository.save(newEmployee);
            });
        
        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    

}
