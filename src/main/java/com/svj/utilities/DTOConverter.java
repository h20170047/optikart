package com.svj.utilities;

import com.svj.dto.CustomerRequestDTO;
import com.svj.dto.CustomerResponseDTO;
import com.svj.entity.CustomerEntity;

public class DTOConverter {
    public static CustomerEntity mapDTOToEntity(CustomerRequestDTO customerRequestDTO){
        CustomerEntity customerEntity = new CustomerEntity();
        copyToEntity(customerRequestDTO, customerEntity);
        return customerEntity;
    }

    public static void copyToEntity(CustomerRequestDTO customerRequestDTO, CustomerEntity customerEntity) {
        customerEntity.setFirstName(customerRequestDTO.getFirstName());
        customerEntity.setLastName(customerRequestDTO.getLastName());
        customerEntity.setDateTimeOfPurchase(customerRequestDTO.getDateTimeOfPurchase());
    }

    public static CustomerResponseDTO mapEntityToResponse(CustomerEntity customerEntity){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(customerEntity.getId());
        customerResponseDTO.setFirstName(customerEntity.getFirstName());
        customerResponseDTO.setLastName(customerEntity.getLastName());
        customerResponseDTO.setDateTimeOfPurchase(customerEntity.getDateTimeOfPurchase());
        return customerResponseDTO;
    }
}
