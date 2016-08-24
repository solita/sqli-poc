package poc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Place {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String name;

    private String country;

    private String continent;

    protected Place() {}

    public Place(String name, String country, String continent) {
        this.name = name;
        this.country = country;
        this.continent = continent;
    }

    @Override
    public String toString() {
        return String.format(
                "Place[id=%d, name=%s, country='%s', continent='%s']",
                id, name, country, continent);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getContinent() {
        return continent;
    }

}
