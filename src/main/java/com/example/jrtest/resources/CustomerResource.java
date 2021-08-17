package com.example.jrtest.resources;

import com.example.jrtest.Customer;
import com.example.jrtest.services.CustomerService;
import com.example.jrtest.services.CustomerServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( value = "/api/v1/" )
public class CustomerResource {

  @PutMapping(path="customer", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createCustomer( @RequestBody Customer pCustomer )
  {
    CustomerService service = new CustomerServiceImpl();
    return service.createCustomer(pCustomer);
  }

  @GetMapping(path="customer/all")
  public ResponseEntity<String> getAllCustomers()
  {
    CustomerService service = new CustomerServiceImpl();
    return service.getAllCustomers();
  }

  @GetMapping(path="customer/{pId}/birthday")
  public ResponseEntity<String> getBirthday(@PathVariable long pId )
  {
    CustomerService service = new CustomerServiceImpl();
    return service.getBirthday(pId);
  }

  @GetMapping(path="salarySummary/{salary}")
  public ResponseEntity<String> getCustomerSalaryCount(@PathVariable int salary)
  {
    CustomerService service = new CustomerServiceImpl();
    return service.getSalarySummary(salary);
  }

  @PostMapping(path="customer/",consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateCustomer( @RequestBody Customer pCustomer )
  {
    CustomerService service = new CustomerServiceImpl();
    return service.updateCustomer(pCustomer);
  }

  @GetMapping(path="customer/{pId}")
  public ResponseEntity<String> getCustomer(@PathVariable long pId)
  {
    CustomerService service = new CustomerServiceImpl();
    return service.getCustomer(pId);
  }

  @DeleteMapping(path="customer/{pId}")
  public ResponseEntity<String> deleteCustomer(@PathVariable long pId )
  {
    CustomerService service = new CustomerServiceImpl();
    return service.deleteCustomer(pId);
  }
}
