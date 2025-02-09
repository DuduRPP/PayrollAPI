package com.dudurpp.Payroll.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dudurpp.Payroll.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
