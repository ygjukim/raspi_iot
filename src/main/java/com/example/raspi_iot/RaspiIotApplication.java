package com.example.raspi_iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.example" })
//@ComponentScan("com.example")
public class RaspiIotApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RaspiIotApplication.class, args);
	}
}
