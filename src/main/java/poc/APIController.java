package poc;


import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@RestController
public class APIController {

    @Autowired
    private EntityManager entityManager;

    @RequestMapping("/personlist")
    public List<Person> personList(@RequestParam(value="order", defaultValue="firstName") String order) {
        QPerson person = QPerson.person;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        PathBuilder orderByExpression = new PathBuilder(Object.class, "person");
        OrderSpecifier o = new OrderSpecifier(com.querydsl.core.types.Order.DESC, 
            orderByExpression.get(order));
        List<Person> result = query.select(person)
                .from(person)
                .orderBy(o)
                .fetch();
        return result;
    }

    @RequestMapping("/person")
    public Person person(@RequestParam(value="name", defaultValue="Hupu") String name) {
        QPerson person = QPerson.person;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        Person result = query.select(person)
                .from(person)
                .where(person.firstName.eq(name))
                .fetchOne();
        return result;
    }

    @RequestMapping("/place")
    public Place place(@RequestParam(value="name", defaultValue="Oulu") String name) {
        QPlace place = QPlace.place;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        Place result = query.select(place)
                .from(place)
                .where(place.name.eq(name))
                .fetchOne();
        return result;
    }
}