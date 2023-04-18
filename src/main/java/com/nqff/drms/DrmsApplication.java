package com.nqff.drms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nqff.drms.dao")
public class DrmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrmsApplication.class, args);
	}

}
