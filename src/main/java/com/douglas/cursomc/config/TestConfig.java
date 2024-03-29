package com.douglas.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.douglas.cursomc.services.DBService;
import com.douglas.cursomc.services.EmailService;
import com.douglas.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	DBService dbservice;

	@Bean
	public Boolean instantiateDatabase() throws ParseException {

		dbservice.instantiateDatabase();

		return true;
	}

	@Bean
	public EmailService emailService() {

		return new MockEmailService();

	}

}
