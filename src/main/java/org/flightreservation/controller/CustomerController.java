package org.flightreservation.controller;

import org.flightreservation.data.CustomerDAO;
import org.flightreservation.entity.Customer;

import java.util.List;

public class CustomerController {
    private CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public boolean addCustomer(Customer customer) {
        return customerDAO.save(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.update(customer);
    }

    public Customer getCustomerById(int id) {
        return customerDAO.findById(id);
    }

    public Customer getCustomerByEmail(String email) {
        return customerDAO.findByEmail(email);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    public boolean deleteCustomer(int id) {
        return customerDAO.delete(id);
    }
}