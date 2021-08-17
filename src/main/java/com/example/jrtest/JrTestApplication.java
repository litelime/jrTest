package com.example.jrtest;

import com.example.jrtest.util.DBConnection;
import org.hibernate.engine.jdbc.spi.ResultSetReturn;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class JrTestApplication {

  public static void main( String[] args ) throws SQLException {

    System.out.println( "Creating database, started" );
    DBConnection conn = DBConnection.getInstance("jdbc:h2:mem:testdb", "user", "password" );

    try ( Statement st = conn.getConnection().createStatement() ) {
      st.execute( "create schema company" );
      st.execute( "create table company.customer(id integer NOT NULL AUTO_INCREMENT, name varchar(10), age integer, address varchar, salary integer )" );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Juan', 32, 'Alajuela', 2000 ) " );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Diego', 25, 'Heredia', 1500 ) " );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Brayan', 23, 'Cartago', 2000 ) " );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Marvin', 25, 'San Jose', 6500 ) " );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Rodolfo', 27, 'Guanacaste', 8500 ) " );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Oscar', 22, 'Puntarenas', 4500 ) " );
      st.execute( "INSERT INTO company.customer (name,age,address,salary) VALUES ('Maria', 24, 'Limon', 10000 ) " );
    }
    System.out.println( "Creating database, ended" );
    SpringApplication.run( JrTestApplication.class, args );
  }

}
