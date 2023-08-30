package com.mello.microservices.employeeservice;

import com.mello.microservices.employeeservice.controller.EmployeeController;
import com.mello.microservices.employeeservice.service.EmployeeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EmployeeServiceApplication
{
/*  Using OpenFeign for reaching Departments endpoint instead
	@Bean
	public RestTemplate rest()
	{
		return new RestTemplate();
	}

	@Bean
	public WebClient webClient(){
		return WebClient.builder().build();
	}
*/

	public static void main(String[] args)
	{
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
