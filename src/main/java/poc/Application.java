package poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(PersonRepository personRepository) {
		return (args) -> {
			personRepository.save(new Person("Huppu", "Ankka", "SAD"));
			personRepository.save(new Person("Tuppu", "Ankka", "EVIL"));
			personRepository.save(new Person("Lupus", "Ankka", "THINGS")); 
		};
	}
}
