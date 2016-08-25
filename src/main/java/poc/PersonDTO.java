package poc;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PersonDTO {
 
  private long id;

  private String firstName;

  private String lastName;
 
  public PersonDTO(long id, String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.id = id;
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
}
