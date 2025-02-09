package com.dudurpp.Payroll.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.dudurpp.Payroll.controllers.OrderController;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>>{
    @Override
    public EntityModel<Order> toModel(Order order){
        EntityModel<Order> orderModel = EntityModel.of(order,
            linkTo(methodOn(OrderController.class).getOne(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).getAll()).withRel("orders")
        );
        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }
      
        
        return orderModel;
    }
}
