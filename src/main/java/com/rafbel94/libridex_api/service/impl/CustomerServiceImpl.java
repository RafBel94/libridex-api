package com.rafbel94.libridex_api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafbel94.libridex_api.entity.Customer;
import com.rafbel94.libridex_api.repository.CustomerRepository;
import com.rafbel94.libridex_api.service.CustomerService;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    @Qualifier("customerRepository")
    CustomerRepository customerRepository;

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> listAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findCustomerById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public void removeCustomer(int id) {
        customerRepository.deleteById(id);
    }

}
