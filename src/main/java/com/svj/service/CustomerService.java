package com.svj.service;

import com.svj.dto.CustomerRequestDTO;
import com.svj.dto.CustomerResponseDTO;
import com.svj.entity.CustomerEntity;
import com.svj.exception.CustomerException;
import com.svj.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.svj.utilities.AppUtils.convertObjectToJson;
import static com.svj.utilities.DTOConverter.*;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository repository){
        customerRepository= repository;
    }

    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO){
        try{
            log.info("CustomerService: addCustomer method execution started.");
            CustomerEntity customerEntity = mapDTOToEntity(customerRequestDTO);
            CustomerEntity savedEntity = customerRepository.save(customerEntity);
            log.debug("customer entity response from database is {}", convertObjectToJson(savedEntity));
            log.info("CustomerService: addCustomer method execution ended.");
            return mapEntityToResponse(savedEntity);
        }catch (Exception exception){
            log.error("CourseService: addCustomer exception occurred while saving customer to DB: {}", exception.getMessage());
            throw new CustomerException(String.format("Adding customer service method failed: %s", exception.getMessage()));
        }
    }
    public CustomerResponseDTO getCustomerById(int id){
        try {
            log.info("CustomerService: getCustomerById method execution started.");
            CustomerEntity customerEntity = getCustomerEntity(id);
            log.info("CustomerService: getCustomerById method execution ended.");
            return mapEntityToResponse(customerEntity);
        }catch (Exception exception){
            log.error("CustomerService: getCustomerById exception occurred while retrieving customer with ID: {}", exception.getMessage());
            throw new CustomerException(String.format("Getting customer service by ID failed: %s", exception.getMessage()));
        }
    }

    private CustomerEntity getCustomerEntity(int id) {
        log.info("CustomerService: getCustomerEntity method execution started.");
        try {
            CustomerEntity customer = customerRepository.findById(id)
                    .orElseThrow(
                            () -> new CustomerException(String.format("Customer not found with id- %s", id))
                    );
            log.debug("CustomerService: getCustomerEntity response from DB: {}", convertObjectToJson(customer));
            return customer;
        }catch(Exception e){
            log.error("CustomerService: getCustomerEntity Error occured while retrieving customer with ID: {}", e.getMessage());
            throw new CustomerException(String.format("Getting customer from db with id failed: %s", e.getMessage()));
        }
    }

    public CustomerResponseDTO updateCustomer(int id, CustomerRequestDTO customer){
        try {
            log.info("CustomerService: updateCustomer method execution started.");
            CustomerEntity savedCustomer = getCustomerEntity(id);
            copyToEntity(customer, savedCustomer);
            savedCustomer.setId(id);
            CustomerEntity updatedCustomer = customerRepository.save(savedCustomer);
            log.debug("CustomerService: updateCustomer response from DB: {}", convertObjectToJson(updatedCustomer));
            log.info("CustomerService: updateCustomer method execution ended.");
            return mapEntityToResponse(updatedCustomer);
        }catch (Exception e){
            log.error("CustomerService: updateCustomer Error occured while updating customer details: {}", e.getMessage());
            throw new CustomerException(String.format("Updating customer service failed: %s", e.getMessage()));
        }
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        try {
            log.info("CustomerService: getAllCustomers method execution started.");
            List<CustomerEntity> allCustomers = customerRepository.findAll();
            log.debug("CustomerService: getAllCustomers response from DB is {}", convertObjectToJson(allCustomers));
            List<CustomerResponseDTO> result = new LinkedList<>();
            for (CustomerEntity customer : allCustomers) {
                result.add(mapEntityToResponse(customer));
            }
            log.info("CustomerService: getAllCustomers method execution ended.");
            return result;
        }catch (Exception exception){
            log.error("CustomerService: getAllCustomers Error occured while getting all customers: {}", exception.getMessage());
            throw new CustomerException(String.format("Getting all customer service failed: %s", exception.getMessage()));
        }
    }

    public String deleteCustomer(int customerId) {
        log.info("CustomerService: deleteCustomer method execution started.");
        try {
            customerRepository.deleteById(customerId);
            log.info("CustomerService: deleteCustomer method execution ended.");
            return String.format("Customer with id %d is deleted", customerId);
        }catch (Exception exception){
            log.error("CustomerService: deleteCustomer Exception occured while deleting customer using id: {}", exception.getMessage());
            throw new CustomerException(String.format("Delete customer service failed: %s", exception.getMessage()));
        }
    }
}
