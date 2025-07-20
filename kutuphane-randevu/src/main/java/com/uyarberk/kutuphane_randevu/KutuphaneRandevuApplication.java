package com.uyarberk.kutuphane_randevu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KutuphaneRandevuApplication {

	public static void main(String[] args) {
		SpringApplication.run(KutuphaneRandevuApplication.class, args);
	}

}
