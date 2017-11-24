package leanning.spingcloud.zipkintrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ZipkinTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinTradeApplication.class, args);
	}
}
