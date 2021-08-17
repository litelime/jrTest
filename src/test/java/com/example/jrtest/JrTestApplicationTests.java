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

import java.io.StringWriter;
import java.sql.*;
import java.time.LocalDate;

import static java.lang.Long.valueOf;

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

            CustomerDao dao = new CustomerDaoImpl();

            String result = dao.createCustomer(new Customer("Charles", 25, "California", 2200));

            Assert.isTrue(result.contains("Created"), result);

        } catch (SQLException e) {
            Assert.isTrue(false, e.getMessage());
        }
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

            CustomerDao dao = new CustomerDaoImpl();

            String result = dao.createCustomer(new Customer("Charles", 25, "California", 2200));

            Assert.isTrue(result.contains("Created"), result);

            result = dao.deleteCustomer(1);

            Assert.isTrue(result.contains("Deleted"), result);

            CustomerService service = new CustomerServiceImpl();

            service.save(new Customer("Charles", 25, "California", 2200));

        } catch (SQLException e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    void getCustomer() {

        try {
            CustomerService service = new CustomerServiceImpl();

            String result = service.createCustomer(new Customer("Charles", 25, "California", 2200)).getBody();

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

            getcust = service.findById(valueOf(1)).get();
            Assert.isTrue(getcust.id == 1, result);
            Assert.isTrue(getcust.name.equals("Charles"), result);
            Assert.isTrue(getcust.age == 25, result);
            Assert.isTrue(getcust.address.equals("California"), result);
            Assert.isTrue(getcust.salary == 2200, result);

        }catch(Exception e){
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    void birthday() {

        CustomerService service = new CustomerServiceImpl();
        int age = 25;
        service.createCustomer(new Customer("Charles", age, "California", 2200));
        String result = service.getBirthday(1).getBody();
        int year = LocalDate.now().getYear();
        String birthYear = Integer.toString(year - age);
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

        Assert.isTrue(result.contains("Number of customers with salary greater than 740 is 3"), result);
        Assert.isTrue(result.contains("Number of customers with salary equal to 740 is 0"), result);
        Assert.isTrue(result.contains("Number of customers with salary less than 740 is 2"), result);

    }

}
