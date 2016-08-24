package poc;


import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
public class CustomerController {

    @Autowired
    private EntityManager entityManager;

    @RequestMapping("/customer")
    public Customer customer(@RequestParam(value="name", defaultValue="Jack") String name) {
        QCustomer customer = QCustomer.customer;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        Customer result = query.select(customer)
                .from(customer)
                .where(customer.firstName.eq(name))
                .fetchOne();
        return result;
    }
}