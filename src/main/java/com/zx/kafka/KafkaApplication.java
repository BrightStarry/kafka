package com.zx.kafka;

import com.zx.kafka.boot.Kafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}


	@Autowired
	private Kafka kafka;

	@Override
	public void run(String... strings) throws Exception {
		kafka.send();
	}
}
