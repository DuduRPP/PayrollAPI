package com.dudurpp.Payroll.models;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.dudurpp.Payroll.controllers.EmployeeController;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>>{
    
    @Override
    public EntityModel<Employee> toModel(Employee employee){
        return EntityModel.of(employee, //
            linkTo(methodOn(EmployeeController.class).getOne(employee.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")
        );
    }

}
