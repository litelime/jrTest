package com.example.jrtest.services;

import com.example.jrtest.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerService extends CrudRepository<Customer, Long> {

    ResponseEntity<String> createCustomer(Customer pCustomer);

    ResponseEntity<String> getAllCustomers();

    ResponseEntity<String> updateCustomer(Customer pCustomer);

    ResponseEntity<String> deleteCustomer(long pId);

    ResponseEntity<String> getBirthday(long pId);

    ResponseEntity<String> getSalarySummary(int salary);

    ResponseEntity<String> getCustomer(long pId);

}
