package me.vilius.homesync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Pridet validacijas (duomenu tipai, kad butu visi reikalingi duomenys)
// Neteisingos 404 pakeitimas
// Panaikint nested esybiu kurima

@SpringBootApplication
public class HomesyncApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomesyncApplication.class, args);
	}
}
