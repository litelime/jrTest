package com.example.jrtest.services;

import com.example.jrtest.Customer;
import com.example.jrtest.dao.CustomerDao;
import com.example.jrtest.dao.impl.CustomerDaoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomerServiceImpl implements CustomerService{

    private final CustomerDao customerDao;

    public CustomerServiceImpl(){
        this.customerDao = new CustomerDaoImpl();
    }

    @Override
    public ResponseEntity<String> createCustomer(Customer pCustomer) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerDao.createCustomer(pCustomer));
        }
        catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> getAllCustomers() {
        try {
            ArrayList<Customer> custs = customerDao.getAllCustomers();
            ObjectMapper mapper = new ObjectMapper();
            StringWriter sw =new StringWriter();
            mapper.writeValue(sw,custs);
            return ResponseEntity.status(HttpStatus.OK).body(sw.toString());

        }catch(SQLException | IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error while processing the request");
        }
    }

    @Override
    public ResponseEntity<String> updateCustomer(Customer pCustomer) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerDao.updateCustomer(pCustomer));
        }
        catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteCustomer(long pId) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerDao.deleteCustomer(pId));
        }
        catch (SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> getBirthday(long pId) {
        try {

            int age = customerDao.getAge(pId);
            if(age == -1){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id:"+pId+" not found");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.YEAR,-age);
            Date birthdayDt = c.getTime();
            String birthday = sdf.format(birthdayDt);

            return ResponseEntity.status(HttpStatus.OK).body(birthday);

        }catch(SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error while processing the request");
        }
    }

    @Override
    public ResponseEntity<String> getSalarySummary(int salary) {
        try {

            HashMap<Integer,Integer> salarayGroups = customerDao.getSalarySummary(salary);
            StringBuilder sb = new StringBuilder();

            int less = salarayGroups.get(-1) != null ? salarayGroups.get(-1) : 0;
            int more = salarayGroups.get(1) != null ? salarayGroups.get(1) : 0;
            int equal = salarayGroups.get(0) != null ? salarayGroups.get(0) : 0;

            sb.append("Number of customers with salary greater than "+ salary +" is "+more+"\n");
            sb.append("Number of customers with salary equal to "+ salary +" is "+equal+"\n");
            sb.append("Number of customers with salary less than "+ salary +" is "+less+"\n");

            return ResponseEntity.status(HttpStatus.OK).body(sb.toString());

        }catch(SQLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> getCustomer(long pId) {
        try {
            Customer cust = customerDao.getCustomer(pId);
            if(cust.id == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Did not find a customer with the id: "+pId);
            }
            ObjectMapper mapper = new ObjectMapper();
            StringWriter sw =new StringWriter();
            mapper.writeValue(sw,cust);
            return ResponseEntity.status(HttpStatus.OK).body(sw.toString());

        }catch(SQLException | IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error while processing the request");
        }
    }

    @Override
    public <S extends Customer> S save(S customer) {
        try{
            customerDao.createCustomer(customer);
            return customer;
        }catch(SQLException e){
            return (S) new Customer();
        }
    }

    @Override
    public <S extends Customer> Iterable<S> saveAll(Iterable<S> customers) {
        try{
            for(Customer customer : customers)
                customerDao.createCustomer(customer);
            return customers;
        }catch(SQLException e){
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {

        try{
            Customer cust = customerDao.getCustomer(id);
            if(cust.id==null){
                return Optional.empty();
            }
            Optional <Customer> custOpt =Optional.ofNullable(cust);
            return custOpt;

        }catch(SQLException e){
            return Optional.empty();
        }

    }

    @Override
    public boolean existsById(Long id) {
        try {
            return customerDao.getCustomer(id).id!=null;
        }catch(SQLException e){
            return false;
        }
    }

    @Override
    public Iterable<Customer> findAll() {
        try {
            return customerDao.getAllCustomers();
        }catch(SQLException e){
            return new ArrayList<>();
        }
    }


    @Override
    public Iterable<Customer> findAllById(Iterable<Long> customers) {
        ArrayList<Customer> custList = new ArrayList<>();
        try{
            for(Long id : customers) {
                custList.add(customerDao.getCustomer(id));
            }
        }catch(SQLException e){
            return new ArrayList<>();
        }
        return custList;
    }

    @Override
    public long count() {
        try {
            return customerDao.getAllCustomers().size();
        }catch(SQLException e){
            return 0;
        }
    }

    @Override
    public void deleteById(Long id) {
        deleteCustomer(id);
    }

    @Override
    public void delete(Customer customer) {
        deleteCustomer(customer.id);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> custIds) {
        for(Long id : custIds){
            deleteCustomer(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> customers) {
        for(Customer cust : customers){
            deleteCustomer(cust.id);
        }
    }

    @Override
    public void deleteAll() {
        try{
            ArrayList<Customer> custList = customerDao.getAllCustomers();
            for(Customer cust : custList){
                deleteCustomer(cust.id);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
