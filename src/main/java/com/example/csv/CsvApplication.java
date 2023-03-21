package com.example.csv;

import com.example.csv.domain.Role;
import com.example.csv.domain.UserRole;
import com.example.csv.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication

public class CsvApplication {


	public static void main(String[] args) {SpringApplication.run(CsvApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (RoleRepository repo){
		return args -> {
			UserRole consultant= new UserRole(1l,Role.CONSULTANT,null);
			repo.save(consultant);
		};
	};

}
