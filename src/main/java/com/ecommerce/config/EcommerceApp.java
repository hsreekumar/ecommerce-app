package com.ecommerce.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@ComponentScan("com.ecommerce")
@EnableJpaRepositories("com.ecommerce.repository")
@EntityScan("com.ecommerce.entity")
@SpringBootApplication
@EnableTransactionManagement
public class EcommerceApp {

	
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApp.class, args);
	}

}
