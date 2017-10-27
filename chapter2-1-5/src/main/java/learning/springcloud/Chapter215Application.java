package learning.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class Chapter215Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter215Application.class, args);
	}
}
