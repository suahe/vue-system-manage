package com.example.manageSystem;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {"com.example.manageSystem.admin.module.*.dao"})
@ServletComponentScan("com.example.manageSystem.config.listener")
@SpringBootApplication
public class ManageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageSystemApplication.class, args);
	}

}
