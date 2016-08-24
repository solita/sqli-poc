package poc;


import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
public class PersonController {

    @Autowired
    private EntityManager entityManager;

    @RequestMapping("/person")
    public Person person(@RequestParam(value="name", defaultValue="Jack") String name) {
        QPerson person = QPerson.person;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        Person result = query.select(person)
                .from(person)
                .where(person.firstName.eq(name))
                .fetchOne();
        return result;
    }
}