package poc;

import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@SpringBootApplication
public class Application {

	@Autowired
	private EntityManager entityManager;

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(PersonRepository personRepository, PlaceRepository placeRepository) {
		return (args) -> {
			personRepository.save(new Person("Hupu", "Ankka"));
			personRepository.save(new Person("Tupu", "Ankka"));
			personRepository.save(new Person("Lupu", "Ankka"));

			placeRepository.save(new Place("Oulu", "Finland", "Europe"));
			placeRepository.save(new Place("Tampere", "Finland", "Europe"));
			placeRepository.save(new Place("Helsinki", "Finland", "Europe"));
		};
	}
}
