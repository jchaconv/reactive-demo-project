package springframework.reactive.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonCommand {

    private String firstName;
    private String lastName;

    public PersonCommand(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

    public String sayMyName() {
        return "From Command Class: My Name is " + firstName + " " + lastName + ".";
    }

}
