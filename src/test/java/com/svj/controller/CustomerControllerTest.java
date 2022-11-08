package com.svj.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllCustomers() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders
                .get("/customers")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result= mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "\t{\n" +
                        "\t\t\n" +
                        "\t    \"id\": 1,\n" +
                        "\t    \"firstName\": \"Ram\",\n" +
                        "\t    \"lastName\": \"K\",\n" +
                        "\t    \"dateOfPurchase\": \"27/10/2022\"\n" +
                        "\t}\n" +
                        "]\t"))
                .andReturn();
    }
}
