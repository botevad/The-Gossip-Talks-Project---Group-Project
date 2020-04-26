package bg.codeacademy.spring.gossiptalks;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.print.Book;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class Main
{

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args); }

	/*@Bean
	CommandLineRunner initDatabase(UserRepository repository) {
		return args -> {

			repository.save(new User("dani1", "pass", "dani1@gmail.com", "dani1", "Admin", true));

		};
	}*/

}


