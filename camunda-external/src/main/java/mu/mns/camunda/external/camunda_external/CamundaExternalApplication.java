package mu.mns.camunda.external.camunda_external;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication  // Enables the Camunda process engine
public class CamundaExternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaExternalApplication.class, args);
	}

}
