package com.example.gebeya.ussd.ussdbankingdemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "USSD Banking  API",description = "Bank API Simulation By Group 2", version="1.9"))

public class UssdBankingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(UssdBankingDemoApplication.class, args);
	}

}
