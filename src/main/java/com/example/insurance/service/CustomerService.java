package com.example.insurance.service;

import com.example.insurance.entity.Customer;
import com.example.insurance.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public String createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            return "Error: Customer with email " + customer.getEmail() + " already exists";
        }

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return "Error: Customer name cannot be empty";
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            return "Error: Email cannot be empty";
        }

        Customer savedCustomer = customerRepository.save(customer);
        return "Customer created successfully with ID: " + savedCustomer.getId();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    public String updateCustomerEmail(Long id, String newEmail) {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            return "Error: Customer with ID " + id + " not found";
        }

        Customer existingCustomer = customerRepository.findByEmail(newEmail);
        if (existingCustomer != null && !existingCustomer.getId().equals(id)) {
            return "Error: Email " + newEmail + " is already in use";
        }

        customer.setEmail(newEmail);
        customerRepository.save(customer);

        return "Customer email updated successfully";
    }

    public String deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            return "Error: Customer with ID " + id + " not found";
        }

        customerRepository.deleteById(id);
        return "Customer deleted successfully";
    }
}