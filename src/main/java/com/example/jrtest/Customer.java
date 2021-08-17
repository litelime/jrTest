package com.example.jrtest;

import javax.persistence.*;

@Entity
@Table( schema = "company", name = "customer" )
public class Customer {

  public Customer(){}

  public Customer(String name, int age, String address, int salary){
    this.name = name;
    this.age = age;
    this.address = address;
    this.salary = salary;
  }
  public Customer(long id, String name, int age, String address, int salary){
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.salary = salary;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column( name = "id" )
  public Long id;

  @Column( name = "name" )
  public String name;

  @Column( name = "age" )
  public int age;

  @Column( name = "address" )
  public String address;

  @Column( name = "salary" )
  public int salary;
}
