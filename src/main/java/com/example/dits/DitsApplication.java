package com.example.dits;

import com.example.dits.entity.Topic;
import com.example.dits.service.TopicService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DitsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DitsApplication.class, args);

    }

}
