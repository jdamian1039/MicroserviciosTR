package com.jorged.paciente;

import com.jorged.commons.exceptions.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"com.jorged.paciente", "com.jorged.commons"})
@Import(GlobalExceptionHandler.class)
public class PacienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacienteApplication.class, args);
	}

}
