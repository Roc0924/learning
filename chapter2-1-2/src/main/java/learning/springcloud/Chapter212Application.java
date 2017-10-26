package learning.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Chapter212Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter212Application.class, args);
	}
}
