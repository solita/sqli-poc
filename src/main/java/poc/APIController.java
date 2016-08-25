package poc;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 

@RestController
public class APIController {

    @Autowired
    private PersonRepository personRepo;

    // Is vulnerable
    @RequestMapping("/personfind")
    public Iterable<PersonDTO> personFind(@RequestParam(value="order", defaultValue="firstName") String order,
        @RequestParam(value="term" ) String term) {
      Iterable<Person> persons = personRepo.findBySearchTerm(term, new Sort(order));
      List<PersonDTO> l = new ArrayList<PersonDTO>();
      for (Person p : persons) {
        l.add(new PersonDTO(p.getId(), p.getFirstName(), p.getLastName()));
      }
      return l;
    }
    
    // Appears to not be vulnerable. 
    @RequestMapping("/personlist")
    public Iterable<Person> personList(@RequestParam(value="order", defaultValue="firstName") String order) {
      return personRepo.findAll(new Sort(order));
    }
}