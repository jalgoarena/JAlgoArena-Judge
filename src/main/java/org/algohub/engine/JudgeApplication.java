package org.algohub.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@SpringBootApplication
public class JudgeApplication {

    public static void main(final String[] args) throws IOException, InterruptedException {
        SpringApplication.run(JudgeApplication.class, args);
    }
}
