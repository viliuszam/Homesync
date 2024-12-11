package me.vilius.homesync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// TODO: pririst User objektus prie home, responsuose grazint tik naudotojam priklausancius objektus, pakeist response kodus i 401 ir 403 atitinkamai neprisijungusiem
@SpringBootApplication
public class HomesyncApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomesyncApplication.class, args);
	}
}
