package ru.ythree.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.ythree.blog")
public class Y3BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(Y3BlogApplication.class, args);
    }

}
