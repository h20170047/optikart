package com.svj.controller;

import com.svj.dto.CustomerRequestDTO;
import com.svj.dto.CustomerResponseDTO;
import com.svj.dto.ServiceResponse;
import com.svj.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.svj.utilities.AppUtils.convertObjectToJson;

@RestController
@Slf4j
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService service;

    @Autowired
    public void CustomerController(CustomerService customerService){
        service= customerService;
    }

    @GetMapping
    @Operation(summary = "Fetch all customers")
    public ServiceResponse getCustomers(){
        log.info("CustomerController: getCustomers starting method");
        List<CustomerResponseDTO> customers = service.getAllCustomers();
        ServiceResponse response= new ServiceResponse(HttpStatus.OK, customers, null);
        log.info("CustomerController: getCustomers Response {}", convertObjectToJson(response));
        return response;
    }

    @PostMapping
    @Operation(summary = "Add a new customer to system")
    @ApiResponses(value={
            @ApiResponse(responseCode ="201", description="customer added successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponseDTO.class))
            }),
            @ApiResponse(responseCode ="400", description="validation error")
    })
    public ServiceResponse addCustomer(@RequestBody @Valid CustomerRequestDTO requestDTO){
        log.info("CustomerController: addCustomer Request payload: {}", convertObjectToJson(requestDTO));
        CustomerResponseDTO responseDTO = service.addCustomer(requestDTO);
        ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.CREATED, responseDTO, null);
        log.info("CustomerController: addCustomer Response {}", convertObjectToJson(serviceResponse));
        return serviceResponse;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find customer by id")
    public ServiceResponse getCustomerByID(@PathVariable Integer id){
        log.info("CustomerController: getCustomerByID Request payload: {}", id);
        CustomerResponseDTO customer = service.getCustomerById(id);
        ServiceResponse response= new ServiceResponse(HttpStatus.OK, customer, null);
        log.info("CustomerController: getCustomerByID Response: {}", convertObjectToJson(response));
        return response;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by id")
    public ServiceResponse updateCustomer(@PathVariable Integer id, @RequestBody @Valid CustomerRequestDTO requestDTO){
        log.info("CustomerResponse: updateCustomer Request payload: {}", convertObjectToJson(requestDTO));
        CustomerResponseDTO responseDTO = service.updateCustomer(id, requestDTO);
        ServiceResponse response= new ServiceResponse(HttpStatus.NO_CONTENT, responseDTO, null);
        log.info("CustomerResponse: updateCustomer Response: {}", convertObjectToJson(response));
        return response;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by id")
    public ServiceResponse deleteCustomer(@PathVariable Integer id){
        log.info("CustomerResponse: deleteCustomer Starting method");
        String deleteCustomer = service.deleteCustomer(id);
        ServiceResponse response= new ServiceResponse(HttpStatus.OK, deleteCustomer, null);
        log.info("CustomerResponse: deleteCustomer Response: {}", convertObjectToJson(response));
        return response;
    }


}
