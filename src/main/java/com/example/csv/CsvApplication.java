package com.example.csv;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@EnableScheduling
@EnableSwagger2
@EnableBatchProcessing
@SpringBootApplication
public class CsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvApplication.class, args);
	}

}
