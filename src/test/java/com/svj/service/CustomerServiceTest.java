package com.svj.service;

import com.svj.dto.CustomerRequestDTO;
import com.svj.dto.CustomerResponseDTO;
import com.svj.entity.CustomerEntity;
import com.svj.repository.CustomerRepository;
import com.svj.utilities.AppUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.svj.utilities.DTOConverter.mapDTOToEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService service;
    @Mock
    private CustomerRepository repository;

    @BeforeEach
    public void setUp(){
        service= new CustomerService(repository);
    }

    @Test
    public void addCustomer(){
        CustomerRequestDTO customer=new CustomerRequestDTO("Ram", "K", LocalDateTime.parse("2000-11-01", AppUtils.dateTimeFormatter));
        CustomerEntity savedCustomer= mapDTOToEntity(customer);
        savedCustomer.setId(1);
        when(repository.save(any(CustomerEntity.class))).thenReturn(savedCustomer);
        CustomerResponseDTO responseDTO = service.addCustomer(customer);
        assertThat(responseDTO.getId()).isNotNull();
    }

    @Test
    public void getCustomer(){
        CustomerEntity customer=new CustomerEntity(1, "Ram", "K", LocalDateTime.parse("2000-11-01", AppUtils.dateTimeFormatter));
        when(repository.findById(1)).thenReturn(Optional.of(customer));
        CustomerResponseDTO responseDTO = service.getCustomerById(1);
        assertThat(responseDTO.getFirstName()).isNotNull();
    }

    @Test
    public void updateCustomer(){
        CustomerRequestDTO customerOldData=new CustomerRequestDTO("Ram", "K", LocalDateTime.parse("2000-11-01", AppUtils.dateTimeFormatter));
        CustomerRequestDTO updatedCustomerReq=new CustomerRequestDTO("Ram", "K", LocalDateTime.parse("2000-11-01", AppUtils.dateTimeFormatter));
        CustomerEntity updatedCustomerEntity= mapDTOToEntity(customerOldData);
        updatedCustomerEntity.setLastName("Kumar");
        when(repository.save(any(CustomerEntity.class))).thenReturn(updatedCustomerEntity);
        when(repository.findById(1)).thenReturn(Optional.of(mapDTOToEntity(customerOldData)));

        CustomerResponseDTO responseDTO = service.updateCustomer(1, updatedCustomerReq);
        assertThat(responseDTO.getLastName()).isEqualTo("Kumar");
    }

    @Test
    public void testGetAllCustomers(){
        List<CustomerEntity> result= Arrays.asList(
                new CustomerEntity(1, "cust1", "lName", LocalDateTime.parse("2000-11-01", AppUtils.dateTimeFormatter)),
                new CustomerEntity(2, "cust2", "lName", LocalDateTime.parse("2000-11-01", AppUtils.dateTimeFormatter))
                );
        when(repository.findAll()).thenReturn(result);
        List<CustomerResponseDTO> allCustomers = service.getAllCustomers();
        assertThat(allCustomers.size()).isEqualTo(2);
    }

    @Test
    public void testDeleteCustomer(){
        String response= service.deleteCustomer(1);
        assertThat(response).isEqualTo("Customer with id 1 is deleted");
    }

}