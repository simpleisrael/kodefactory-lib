package com.kodefactory.tech.lib.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JsonUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> String toJsonString(T object) throws JsonProcessingException {
        String response = new ObjectMapper().writeValueAsString(object);
        System.out.println(response);
        return response;
    }

    public <T> Object toJsonObject(String jsonString, Class<T> klass) throws IOException {
        return objectMapper.readValue(jsonString, klass);
    }


    public static void main(String[] args) throws JsonProcessingException {
        Person person = new Person();

        String payload = new ObjectMapper().writeValueAsString(person);
        System.out.println(payload);
    }

    @Data
    static class Person{
        String firstName;
        String lastName;
        java.util.Date dob;
        java.util.List<String> phones;

        Person(){
            firstName = "Java";
            lastName = "Israel";
            dob = new java.util.Date();
            phones = new ArrayList<>();
            phones.add("08939838598");
            phones.add("984384583498");
        }
    }

}
