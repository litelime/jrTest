package com.example.jrtest.dao.impl;

import com.example.jrtest.Customer;
import com.example.jrtest.dao.CustomerDao;
import com.example.jrtest.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerDaoImpl implements CustomerDao {
    DBConnection conn;

    public CustomerDaoImpl() {
        try {
            conn = DBConnection.getInstance("jdbc:h2:mem:testdb", "user", "password");
        } catch (SQLException e) {

        }
    }

    @Override
    public String createCustomer(Customer pCustomer) throws SQLException {

        String insertQuery = "INSERT INTO company.customer (name,age,address,salary) VALUES (?,?,?,?)";
        PreparedStatement pre = conn.getConnection().prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
        pre.setString(1, pCustomer.name);
        pre.setInt(2, pCustomer.age);
        pre.setString(3, pCustomer.address);
        pre.setInt(4, pCustomer.salary);
        pre.executeUpdate();
        ResultSet rs = pre.getGeneratedKeys();
        if(rs.next()){
            pCustomer.id = rs.getLong(1);
            return "Created new customer with ID: "+ rs.getString(1);
        }else{
            return "Failed to create new customer";
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() throws SQLException {

        Statement st = conn.getConnection().createStatement();
        ResultSet result = st.executeQuery("select * from company.customer order by age desc, salary desc");
        ArrayList<Customer> customers = new ArrayList<Customer>();
        while (result.next()) {
            Customer newCust = new Customer();
            newCust.id = result.getLong("id");
            newCust.name = result.getString("name");
            newCust.age = result.getInt("age");
            newCust.address = result.getString("address");
            newCust.salary = result.getInt("salary");
            customers.add(newCust);
        }

        return customers;
    }

    @Override
    public String updateCustomer(Customer pCustomer) throws SQLException {
        String updateQuery = "UPDATE company.customer set name = ?,age = ?,address = ?,salary = ? where id = ?";
        PreparedStatement pre = conn.getConnection().prepareStatement(updateQuery,Statement.RETURN_GENERATED_KEYS);
        pre.setString(1, pCustomer.name);
        pre.setInt(2, pCustomer.age);
        pre.setString(3, pCustomer.address);
        pre.setInt(4, pCustomer.salary);
        pre.setLong(5, pCustomer.id);

        pre.executeUpdate();
        ResultSet rs = pre.getGeneratedKeys();
        if(rs.next()){
            return "Updated customer with ID: "+ rs.getString(1);
        }else{
            return "Failed to update customer";
        }
    }

    @Override
    public String deleteCustomer(long pId) throws SQLException {
        String deleteQuery = "DELETE FROM company.customer where id = ?";
        PreparedStatement pre = conn.getConnection().prepareStatement(deleteQuery,Statement.RETURN_GENERATED_KEYS);
        pre.setLong(1,pId);
        int result = pre.executeUpdate();

        if(result > 0){
            return "Deleted customer with ID: "+ pId;
        }else{
            return "Failed to delete customer";
        }
    }

    @Override
    public int getAge(long pId) throws SQLException{
        String ageQuery = "select age from company.customer where id = ?";
        PreparedStatement pre = conn.getConnection().prepareStatement(ageQuery);
        pre.setLong(1, pId);

        ResultSet rs = pre.executeQuery();
        if(rs.next()){
            return rs.getInt("age");
        }else{
            return -1;
        }
    }

    @Override
    public HashMap<Integer,Integer> getSalarySummary(int salary) throws SQLException {
        String salaryQuery = "select count(*), salaryCheck from (select *,case when salary>? then 1 when salary<? then -1 else 0 end as salaryCheck from company.customer) group by salaryCheck";
        PreparedStatement pre = conn.getConnection().prepareStatement(salaryQuery);
        pre.setInt(1, salary);
        pre.setInt(2, salary);

        ResultSet rs = pre.executeQuery();

        HashMap<Integer,Integer> salaryGroups = new HashMap<Integer,Integer>();
        int i=0;
        while(rs.next()){
            salaryGroups.put(rs.getInt(2), rs.getInt(1));
        }

        return salaryGroups;
    }

    @Override
    public Customer getCustomer(long pId) throws SQLException {

        String custquery = ("select * from company.customer where id = ?");
        PreparedStatement pre = conn.getConnection().prepareStatement(custquery);
        pre.setLong(1,pId);
        ResultSet result = pre.executeQuery();

        Customer customer = new Customer();
        if (result.next()) {
            customer.id = result.getLong("id");
            customer.name = result.getString("name");
            customer.age = result.getInt("age");
            customer.address = result.getString("address");
            customer.salary = result.getInt("salary");
        }

        return customer;
    }
}
