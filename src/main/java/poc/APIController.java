package poc;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.jpa.impl.JPAQuery;

@RestController
public class APIController {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private PersonRepository personRepo;

    @RequestMapping("/personfind")
    public Iterable<PersonDTO> personFind(@RequestParam(value="order", defaultValue="firstName") String order) {
      Iterable<Person> persons = personRepo.findBySearchTerm("Ankka", new Sort(order));
      List<PersonDTO> l = new ArrayList<PersonDTO>();
      for (Person p : persons) {
        l.add(new PersonDTO(p.getId(), p.getFirstName(), p.getLastName()));
      }
      return l;
    }
    
    // https://www.notsosecure.com/injection-in-order-by-clause/
    // https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-six-sorting/
    @RequestMapping("/personlist")
    public Iterable<Person> personList(@RequestParam(value="order", defaultValue="firstName") String order) {
      return personRepo.findAll(new Sort(order));
          
      
      /*
      Iterable<Person> 
      List<T> target = new ArrayList<>();
      source.forEach(target::add);
      return 
        QPerson person = QPerson.person;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        PathBuilder orderByExpression = new PathBuilder(Object.class, "person");
        
        String sortedQuery = QueryUtils.applySorting("select person from poc.Person person", new Sort(order));
        
//        org.springframework.data.jpa.repository.support.Querydsl
        //buildOrderPropertyPathFrom();
//        OrderByExpression obe = new 
        OrderSpecifier o = new OrderSpecifier(com.querydsl.core.types.Order.DESC, 
            orderByExpression.get(order));
        List<Person> result = query.select(person)
                .from(person)
                .orderBy(o)
                .fetch();
        return result;
        */
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