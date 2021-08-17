package com.example.jrtest;

import com.example.jrtest.dao.CustomerDao;
import com.example.jrtest.dao.impl.CustomerDaoImpl;
import com.example.jrtest.services.CustomerService;
import com.example.jrtest.services.CustomerServiceImpl;
import com.example.jrtest.util.DBConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
class JrTestApplicationTests {

    @BeforeAll
    public static void createSchema() throws SQLException {

        DBConnection conn = DBConnection.getInstance("jdbc:h2:mem:testdb", "user", "password");
        Statement st = conn.getConnection().createStatement();
        st.execute("create schema company");

    }

    @BeforeEach
    public void createDB() throws SQLException {

        DBConnection conn = DBConnection.getInstance("jdbc:h2:mem:testdb", "user", "password");
        Statement st = conn.getConnection().createStatement();
        st.execute("create table if not exists company.customer(id integer NOT NULL AUTO_INCREMENT, name varchar(10), age integer, address varchar, salary integer )");
    }

    @AfterEach
    public void dropDB() throws SQLException {

        DBConnection conn = DBConnection.getInstance("jdbc:h2:mem:testdb", "user", "password");
        Statement st = conn.getConnection().createStatement();
        st.execute("drop table company.customer");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createCustomer() {
        try {

            //test create customer
            CustomerDao dao = new CustomerDaoImpl();

            String result = dao.createCustomer(new Customer("Charles", 25, "California", 2200));

            Assert.isTrue(result.contains("Created"), result);

        } catch (SQLException e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    void saveCustomer() {

        CustomerService service = new CustomerServiceImpl();

        Customer cust = new Customer("Charles", 25, "California", 2200);
        Customer savedCust = service.save(cust);
        cust.id = 1L;
        Assert.isTrue(savedCust.equals(cust),"Should return the same customer");

    }

    @Test
    void saveAllCustomer() {

        CustomerService service = new CustomerServiceImpl();

        ArrayList<Customer> custs = new ArrayList<>();
        custs.add(new Customer("Sarah", 25, "California", 2200));
        custs.add(new Customer("Charles", 25, "California", 2200));
        custs.add(new Customer("Dean", 25, "California", 2200));
        custs.add(new Customer("James", 25, "California", 2200));

        Iterable<Customer> savedCusts;
        savedCusts = service.saveAll(custs);

        Assert.isTrue(savedCusts.equals(custs),"Should return the same customers");
        Assert.isTrue(service.count()==4,"Should have saved 4 customers");

    }

    @Test
    void updateCustomer() {
        try {

            CustomerDao dao = new CustomerDaoImpl();

            String result = dao.createCustomer(new Customer("Charles", 25, "California", 2200));

            Assert.isTrue(result.contains("Created"), result);

            result = dao.updateCustomer(new Customer(1, "Daniel", 24, "Arizona", 242));

            Assert.isTrue(result.contains("Updated"), result);
            Assert.isTrue(result.contains("1"), result);

        } catch (SQLException e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    void deleteCustomer() {
        try {

            //TEST deleteCustomer
            CustomerDao dao = new CustomerDaoImpl();

            String result = dao.createCustomer(new Customer("Charles", 25, "California", 2200));

            Assert.isTrue(result.contains("Created"), result);

            result = dao.deleteCustomer(1);

            Assert.isTrue(result.contains("Deleted"), result);

            //test delete by entity
            CustomerService service = new CustomerServiceImpl();

            service.save(new Customer("Charles", 25, "California", 2200));
            service.delete(new Customer(1,"Charles", 25, "California", 2200));
            Assert.isTrue(service.findById(1L).equals(Optional.empty()), result);

            //test delete by id
            service.save(new Customer("Charles", 25, "California", 2200));
            service.deleteById(1L);
            Assert.isTrue(service.findById(1L).equals(Optional.empty()), result);

            //test delete all
            service.save(new Customer("Charles", 25, "California", 2200));
            service.save(new Customer("Sarah", 25, "California", 2200));
            service.deleteAll();
            Assert.isTrue(service.count()==0, result);

            //test delete all by list
            ArrayList<Customer> custs = new ArrayList<>();
            custs.add(new Customer("Sarah", 25, "California", 2200));
            custs.add(new Customer("Charles", 25, "California", 2200));
            service.saveAll(custs);
            Assert.isTrue(service.count()==2, result);
            service.deleteAll(custs);
            Assert.isTrue(service.count()==0, result);

            //test delete all by id
            service.saveAll(custs);
            Assert.isTrue(service.count()==2, result);
            service.deleteAllById(custs.stream().map(customer -> customer.id).collect(Collectors.toList()));
            Assert.isTrue(service.count()==0, result);

        } catch (SQLException e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    void getCustomer() {

        try {
            CustomerService service = new CustomerServiceImpl();

            String result = service.createCustomer(new Customer("Charles", 25, "California", 2200)).getBody();

            assert result != null;
            Assert.isTrue(result.contains("Created"), result);

            Customer getcust = new Customer();
            result = service.getCustomer(1).getBody();
            ObjectMapper mapper = new ObjectMapper();
            getcust = mapper.readValue(result, Customer.class);

            Assert.isTrue(getcust.id == 1, result);
            Assert.isTrue(getcust.name.equals("Charles"), result);
            Assert.isTrue(getcust.age == 25, result);
            Assert.isTrue(getcust.address.equals("California"), result);
            Assert.isTrue(getcust.salary == 2200, result);

            //Test find by ID
            getcust = service.findById(1L).get();
            Assert.isTrue(getcust.id == 1, result);
            Assert.isTrue(getcust.name.equals("Charles"), result);
            Assert.isTrue(getcust.age == 25, result);
            Assert.isTrue(getcust.address.equals("California"), result);
            Assert.isTrue(getcust.salary == 2200, result);

            //Test find all by Id
            service.save(new Customer("James", 25, "California", 2200));
            service.save(new Customer("Lisa", 25, "California", 2200));

            ArrayList<Long> custIds = new ArrayList<Long>();
            Iterable<Customer> custs = new ArrayList<Customer>();

            custIds.add(1L);
            custIds.add(2L);
            custIds.add(3L);

            custs = service.findAllById(custIds);
            long cnt = 1L;
            for(Customer cust : custs){
                Assert.isTrue(cust.id == cnt, result);
                cnt++;
            }

        }catch(Exception e){
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    void findAllCustomers(){

        CustomerService service = new CustomerServiceImpl();
        ArrayList<Customer> custList = new ArrayList<>();

        custList.add(new Customer("Charles", 23, "California", 2200));
        custList.add(new Customer("James", 23, "California", 2200));
        custList.add(new Customer("Darlene", 23, "California", 2200));
        custList.add(new Customer("Elliot", 23, "California", 2200));

        service.saveAll(custList);

        Iterable<Customer> savedCusts = service.findAll();
        ArrayList<Customer> savedCustList = new ArrayList<>();
        savedCusts.forEach(customer -> savedCustList.add(customer));
        Assert.isTrue(savedCustList.size()==4, "Should find the 4 added customers");
        Assert.isTrue(savedCustList.stream().filter(customer->customer.id==1).collect(Collectors.toList()).get(0).name=="Charles", "Should find the 4 added customers");
        Assert.isTrue(savedCustList.stream().filter(customer->customer.id==2).collect(Collectors.toList()).get(0).name=="James", "Should find the 4 added customers");
        Assert.isTrue(savedCustList.stream().filter(customer->customer.id==3).collect(Collectors.toList()).get(0).name=="Darlene", "Should find the 4 added customers");
        Assert.isTrue(savedCustList.stream().filter(customer->customer.id==4).collect(Collectors.toList()).get(0).name=="Elliot", "Should find the 4 added customers");

    }

    @Test
    void birthday() {

        CustomerService service = new CustomerServiceImpl();
        int age = 25;
        service.createCustomer(new Customer("Charles", age, "California", 2200));
        String result = service.getBirthday(1).getBody();
        int year = LocalDate.now().getYear();
        String birthYear = Integer.toString(year - age);
        assert result != null;
        Assert.isTrue(result.contains(birthYear), result);

    }

    @Test
    void salaryCount() {

        CustomerService service = new CustomerServiceImpl();
        service.createCustomer(new Customer("Charles", 12, "California", 2200));
        service.createCustomer(new Customer("Daniel", 21, "California", 2000));
        service.createCustomer(new Customer("Sarah", 35, "California", 1000));
        service.createCustomer(new Customer("Lily", 31, "California", 500));
        service.createCustomer(new Customer("James", 56, "California", 20));

        String result = service.getSalarySummary(740).getBody();

        assert result != null;
        Assert.isTrue(result.contains("Number of customers with salary greater than 740 is 3"), result);
        Assert.isTrue(result.contains("Number of customers with salary equal to 740 is 0"), result);
        Assert.isTrue(result.contains("Number of customers with salary less than 740 is 2"), result);

    }

}
