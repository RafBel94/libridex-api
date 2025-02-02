package com.rafbel94.libridex_api.service;

import java.util.List;

import com.rafbel94.libridex_api.entity.Customer;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    List<Customer> listAllCustomers();
    Customer findCustomerById(int id);
    void removeCustomer(int id);
}
