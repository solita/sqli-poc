package poc;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends  PagingAndSortingRepository<Person, Long> {
  
  @Query("SELECT person FROM Person person WHERE " +
      "LOWER(person.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
      "LOWER(person.lastName) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
  List<Person> findBySearchTerm(@Param("searchTerm") String searchTerm, Sort sort);
}
