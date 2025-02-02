package com.rafbel94.libridex_api.controller.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafbel94.libridex_api.entity.Customer;
import com.rafbel94.libridex_api.service.CustomerService;

@RestController
@RequestMapping("/api")
public class RestCustomer {

    @Autowired
    @Qualifier("customerService")
    private CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() {
        List<Customer> customerList = customerService.listAllCustomers();

        if(customerList.isEmpty())
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(customerList);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> getCustomer(@PathVariable int customerId) {
        Customer customer =  customerService.findCustomerById(customerId);

        if(customer == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(customer);
    }

    @PostMapping("/customers")
    public ResponseEntity<?> insertCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/customers")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.addCustomer(customer));
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int customerId) {
        if(customerService.findCustomerById(customerId) != null){
            customerService.removeCustomer(customerId);
            // We return noContent() for this kind of operations
            // The user was deleted and we return a response with no content
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
