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
	public CommandLineRunner demo(PersonRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Person("Jack", "Bauer"));
			repository.save(new Person("Chloe", "O'Brian"));
			repository.save(new Person("Kim", "Bauer"));
			repository.save(new Person("David", "Palmer"));
			repository.save(new Person("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Person person : repository.findAll()) {
				log.info(person.toString());
			}
            log.info("");

			// fetch an individual person by ID
			Person person = repository.findOne(1L);
			log.info("Person found with findOne(1L):");
			log.info("--------------------------------");
			log.info(person.toString());
            log.info("");

			// fetch customers by last name
			log.info("Person found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (Person bauer : repository.findByLastName("Bauer")) {
				log.info(bauer.toString());
			}
            log.info("");

			// Test querydsl
			QPerson person2 = QPerson.person;
			JPAQuery<?> query = new JPAQuery<Void>(entityManager);
			Person jack = query.select(person2)
					.from(person2)
					.where(person2.firstName.eq("Jack"))
					.fetchOne();
			log.info("-------------QDSL------------------");
			log.info(jack.toString());
		};
	}
}
