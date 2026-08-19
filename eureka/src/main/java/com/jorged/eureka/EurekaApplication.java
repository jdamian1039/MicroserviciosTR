package com.jorged.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
	//el botoncito de play en la linea de abajo permite correr el proyecto cuando hay mas de un proyecto mostrado en
	// intelliJ para evitar que inicialice otras instancias
	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}

}
