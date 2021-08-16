package com.example.jrtest.resources;

import com.example.jrtest.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/api/v1" )
public class CustomerResource {

  public ResponseEntity<String> createCustomer( Customer pCustomer )
  {
    return null;
  }

  public ResponseEntity<String> getAllCustomers( Customer pCustomer )
  {
    return null;
  }

  public ResponseEntity<String> updateCustomer( Customer pCustomer )
  {
    return null;
  }

  public ResponseEntity<String> deleteCustomer( Integer pId )
  {
    return null;
  }
}
