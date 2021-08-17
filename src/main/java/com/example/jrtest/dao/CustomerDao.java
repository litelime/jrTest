package com.example.jrtest.dao;

import com.example.jrtest.Customer;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface CustomerDao {

    public String createCustomer(Customer pCustomer) throws SQLException ;

    public ArrayList<Customer> getAllCustomers() throws SQLException;

    public String updateCustomer( Customer pCustomer ) throws SQLException;

    public String deleteCustomer( long pId ) throws SQLException;

    public int getAge(long pId) throws SQLException;

    public HashMap<Integer,Integer> getSalarySummary(int salary) throws SQLException;

    public Customer getCustomer(long pId) throws SQLException;
}
