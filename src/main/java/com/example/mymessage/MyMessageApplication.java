package com.example.mymessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication()
@EnableMongoRepositories
public class MyMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyMessageApplication.class, args);
    }

}
