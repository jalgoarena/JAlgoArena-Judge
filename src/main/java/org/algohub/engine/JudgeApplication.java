package org.algohub.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
public class JudgeApplication {

    public static void main(final String[] args) throws IOException, InterruptedException {
        SpringApplication.run(JudgeApplication.class, args);
    }

    @Bean
    public Docket newApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("JAlgoArena")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/problems.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JAlgoArena")
                .description("Algorithms Contest Arena")
                .contact(contact())
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/spolnik/JAlgoArena/blob/master/LICENSE")
                .version("2.0")
                .build();
    }

    private Contact contact() {
        return new Contact("Jacek Spólnik", "https://spolnik.github.io/AboutMe/", "jacek.spolnik@gmail.com");
    }
}
