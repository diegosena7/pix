package br.com.dsena7.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.dsena7.pix.repository")
public class PixProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixProjectApplication.class, args);
	}

}
