package leaning.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Chapter112Application {

	public static void main(String[] args) {
		Long start = System.currentTimeMillis();
		SpringApplication.run(Chapter112Application.class, args);

		System.out.println("start in " + (System.currentTimeMillis() - start)/1000 + "s");
	}
}
