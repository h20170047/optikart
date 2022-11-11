package com.svj.integrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.svj.controller.CustomerController;
import com.svj.dto.CustomerRequestDTO;
import com.svj.dto.CustomerResponseDTO;
import com.svj.entity.CustomerEntity;
import com.svj.repository.CustomerRepository;
import com.svj.service.CustomerService;
import com.svj.utilities.AppUtils;
import com.svj.utilities.DTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.svj.utilities.DTOConverter.mapDTOToEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class OptiKartApplication {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.mockMvc= MockMvcBuilders
                .standaloneSetup(customerController)
                .build();
    }

    private final String uri= "/customers";

    @Test
    public void addCustomerTest() throws Exception {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO("Test", "App", LocalDateTime.parse(LocalDate.now().plusDays(1).toString(), AppUtils.dateTimeFormatter));
        CustomerEntity customer= new CustomerEntity(100, requestDTO.getFirstName(), requestDTO.getLastName(), requestDTO.getDateTimeOfPurchase());
        when(customerRepository.save(any())).thenReturn(customer);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(writeJson(requestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.id").value(100));
    }

    @Test
    public void getCustomerByIdTest() throws Exception {
        CustomerEntity customer= new CustomerEntity(100, "Test", "App", LocalDateTime.parse(LocalDate.now().plusDays(1).toString(), AppUtils.dateTimeFormatter));
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
        mockMvc.perform(MockMvcRequestBuilders.get(uri+"/{id}", 100)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.id").value(100));
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        CustomerEntity customer= new CustomerEntity(100, "Test", "App", LocalDateTime.parse(LocalDate.now().plusDays(1).toString(), AppUtils.dateTimeFormatter));
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response[0].id").value(100));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        CustomerRequestDTO customer= new CustomerRequestDTO("Test", "App", LocalDateTime.parse(LocalDate.now().plusDays(1).toString(), AppUtils.dateTimeFormatter));
        CustomerEntity entity= mapDTOToEntity(customer);
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(entity));
        when(customerRepository.save(any())).thenReturn(entity);
        mockMvc.perform(MockMvcRequestBuilders.put(uri+"/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeJson(customer))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.id").value(100));
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        CustomerRequestDTO customer= new CustomerRequestDTO("Test", "App", LocalDateTime.parse(LocalDate.now().plusDays(1).toString(), AppUtils.dateTimeFormatter));
        CustomerEntity entity= mapDTOToEntity(customer);
        doNothing().when(customerRepository).deleteById(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/{id}", 100)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Customer with id 100 is deleted"));
    }

    private String writeJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

}
