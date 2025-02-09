package com.dudurpp.Payroll.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dudurpp.Payroll.models.Employee;
import com.dudurpp.Payroll.models.Order;
import com.dudurpp.Payroll.models.Status;
import com.dudurpp.Payroll.repositories.EmployeeRepository;
import com.dudurpp.Payroll.repositories.OrderRepository;


@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
            employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
            employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));
      
            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));
            
            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));
      
            orderRepository.findAll().forEach(order -> {
              log.info("Preloaded " + order);
            });
      
        };
    }
}
