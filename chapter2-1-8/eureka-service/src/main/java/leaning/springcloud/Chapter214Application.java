package leaning.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Chapter214Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter214Application.class, args);
	}
}
