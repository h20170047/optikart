package com.svj.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.svj.exception.CustomerException;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class AppUtils {
    public static DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart().appendPattern("HH:mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();
    public static ObjectMapper objectMapper= new ObjectMapper();
    static {
        System.out.println("Making sure that objectMapper is rightly registered with JavaTimeModule");
        objectMapper.registerModule(new JavaTimeModule());
    }
    public static String convertObjectToJson(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new CustomerException(e.getMessage());
        }
    }
}
