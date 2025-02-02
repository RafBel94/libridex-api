package com.rafbel94.libridex_api.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafbel94.libridex_api.entity.Customer;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer,Serializable>{
    Customer findById(int id);
}
