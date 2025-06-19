package com.sagapattern.sagaorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(com.sagapattern.common.configuration.RabbitMQConfig.class)
public class SagaOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagaOrchestratorApplication.class, args);
	}

}
