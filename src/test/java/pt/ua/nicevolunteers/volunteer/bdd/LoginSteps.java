package pt.ua.nicevolunteers.volunteer.bdd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pt.ua.nicevolunteers.auth.service.AuthService;
import pt.ua.nicevolunteers.volunteer.service.VolunteerService;

@SpringBootTest
public class LoginSteps {

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private AuthService authService;

    private boolean loginResult;

    @Given("a registered volunteer with email {string} and password {string}")
    public void a_registered_volunteer(String email, String password) {
        volunteerService.register("John", email, password);
    }

    @When("the volunteer tries to login with email {string} and password {string}")
    public void tries_login(String email, String password) {
        try {
            authService.login(email, password);
            loginResult = true;
        } catch (Exception e) {
            loginResult = false;
        }
    }

    @Then("the login is successful")
    public void login_successful() {
        assert loginResult;
    }

    @Then("the login is rejected")
    public void login_rejected() {
        assert !loginResult;
    }
}
