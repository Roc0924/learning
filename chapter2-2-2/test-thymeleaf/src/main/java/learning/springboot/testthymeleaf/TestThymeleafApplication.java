package learning.springboot.testthymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TestThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestThymeleafApplication.class, args);
	}
}
