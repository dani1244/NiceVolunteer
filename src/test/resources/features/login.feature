Feature: Volunteer Login

  Scenario: Successful login with valid credentials
    Given a registered volunteer with email "john@ua.pt" and password "StrongPass123"
    When the volunteer tries to login with email "john@ua.pt" and password "StrongPass123"
    Then the login is successful

  Scenario: Login fails with wrong password
    Given a registered volunteer with email "john@ua.pt" and password "StrongPass123"
    When the volunteer tries to login with email "john@ua.pt" and password "WrongPass"
    Then the login is rejected
