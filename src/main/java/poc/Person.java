package poc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String firstName;

    private String lastName;
    
    private String secretHash;

    protected Person() {}

    public Person(String firstName, String lastName, String secretHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.secretHash = secretHash;
    }

    @Override
    public String toString() {
        return String.format(
                "Person[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String getSecretHash() {
      return secretHash;      
    }

}
