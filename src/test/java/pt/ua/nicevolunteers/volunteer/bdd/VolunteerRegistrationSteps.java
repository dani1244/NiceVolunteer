package pt.ua.nicevolunteers.volunteer.bdd;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ua.nicevolunteers.volunteer.service.VolunteerService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VolunteerRegistrationSteps {

    @Autowired
    private VolunteerService volunteerService;

    private String name;
    private String email;
    private String password;
    private Exception exception;

    @Given("a volunteer with name {string}, email {string} and password {string}")
    public void a_volunteer_with_data(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @When("the volunteer submits the registration")
    public void the_volunteer_submits_the_registration() {
        try {
            volunteerService.register(name, email, password, null, null, null);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("the volunteer is successfully registered")
    public void the_volunteer_is_successfully_registered() {
        assertNull(exception);
    }

    @Then("the registration is rejected due to invalid email")
    public void the_registration_is_rejected_due_to_invalid_email() {
        assertNotNull(exception);
        assertTrue(exception.getMessage().toLowerCase().contains("email"));
    }

    @Then("the registration is rejected due to weak password")
    public void the_registration_is_rejected_due_to_weak_password() {
        assertNotNull(exception);
        assertTrue(exception.getMessage().toLowerCase().contains("password"));
    }
}
