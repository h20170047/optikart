package com.svj.integrationTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.svj.dto.CustomerRequestDTO;
import com.svj.dto.CustomerResponseDTO;
import com.svj.dto.ServiceResponse;
import com.svj.entity.CustomerEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static com.svj.utilities.AppUtils.dateTimeFormatter;
import static com.svj.utilities.AppUtils.objectMapper;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OptiKartApplicationTests {

	@LocalServerPort
	private int port;
	private String baseUrl="http://localhost:";
	private static TestRestTemplate restTemplate;
	@Autowired
	private TestH2Repository repository;

	@BeforeAll
	public static void init() {
		restTemplate = new TestRestTemplate();
	}
	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(String.valueOf(port)).concat("/customers");
	}

	@Test
	@Sql(statements = "DELETE FROM ORDERS_TBL WHERE first_Name='John'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testAddCustomer() {
		CustomerRequestDTO customer = new CustomerRequestDTO("John", "Doe" , LocalDateTime.parse("1990-01-01", dateTimeFormatter));
		ServiceResponse serverResponse = restTemplate.postForObject(baseUrl, customer, ServiceResponse.class);
		final CustomerResponseDTO customerResponse = objectMapper.convertValue(serverResponse.getResponse(), CustomerResponseDTO.class);

		assertEquals("John", customerResponse.getFirstName());
		assertEquals(1, repository.findAll().size());
	}

	@Test
	@Sql(statements = "INSERT INTO ORDERS_TBL (id, first_Name, last_Name, date_Time_Of_Purchase) VALUES (2, 'John', 'Doe', '2000-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM ORDERS_TBL WHERE first_Name='John'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetProducts() {
		 ServiceResponse serverResponse= restTemplate.getForObject(baseUrl, ServiceResponse.class);
		 List<CustomerEntity> list= objectMapper.convertValue(serverResponse.getResponse(), new TypeReference<List<CustomerEntity>>(){});
		assertEquals(1, list.size());
		assertEquals(1, repository.findAll().size());
	}

	@Test
	@Sql(statements = "INSERT INTO ORDERS_TBL (id, first_Name, last_Name, date_Time_Of_Purchase) VALUES (3, 'John', 'Doe', '2000-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM ORDERS_TBL WHERE id=3", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testFindProductById() {
		ServiceResponse customerResponse = restTemplate.getForObject(baseUrl + "/{id}", ServiceResponse.class, 3);
		CustomerResponseDTO customer= objectMapper.convertValue(customerResponse.getResponse(), CustomerResponseDTO.class);
		assertAll(
				() -> assertNotNull(customer),
				() -> assertEquals(3, customer.getId()),
				() -> assertEquals("John", customer.getFirstName())
		);
	}

	@Test
	@Sql(statements = "INSERT INTO ORDERS_TBL (id, first_Name, last_Name, date_Time_Of_Purchase) VALUES (4, 'John', 'Doe', '2000-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM ORDERS_TBL WHERE id=4", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateProduct(){
		CustomerRequestDTO newCustomer = new CustomerRequestDTO("John", "Doe" , LocalDateTime.parse("1990-01-01", dateTimeFormatter));
		restTemplate.put(baseUrl+"/{id}", newCustomer, 4);
		CustomerEntity customerFromDB = repository.findById(4).get();
		assertAll(
				() -> assertNotNull(customerFromDB),
				() -> assertEquals(LocalDateTime.parse("1990-01-01", dateTimeFormatter), customerFromDB.getDateTimeOfPurchase())
		);
	}

	@Test
	@Sql(statements = "INSERT INTO ORDERS_TBL (id, first_Name, last_Name, date_Time_Of_Purchase) VALUES (5, 'John', 'Doe', '2000-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void testDeleteProduct(){
		int recordCount=repository.findAll().size();
		assertEquals(1, recordCount);
		restTemplate.delete(baseUrl+"/{id}", 5);
		assertEquals(0, repository.findAll().size());

	}


}
