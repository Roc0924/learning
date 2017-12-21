package learning.springboot.datacache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DataCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataCacheApplication.class, args);
	}
}
