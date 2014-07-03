package com.springapp.mvc.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nik on 03.07.2014.
 */
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String firstName;

    @Basic
    private String lastName;

    @Basic
    private String email;


    //@OneToMany(fetch = FetchType.EAGER,mappedBy = "user") - без этой фигни lazy load сваливается
    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new HashSet<Order>();

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Order> getOrders() {
        return orders;
    }
}
