Feature: Volunteer Registration

  As a visitor
  I want to register as a volunteer
  So that I can participate in volunteering activities

  Scenario: Successful registration with institutional email
    Given a volunteer with name "Ana Silva", email "ana@ua.pt" and password "StrongPass123!"
    When the volunteer submits the registration
    Then the volunteer is successfully registered

  Scenario: Registration fails with non-institutional email
    Given a volunteer with name "Joao Costa", email "joao@gmail.com" and password "StrongPass123!"
    When the volunteer submits the registration
    Then the registration is rejected due to invalid email

  Scenario: Registration fails with weak password
    Given a volunteer with name "Maria Lopes", email "maria@ua.pt" and password "123"
    When the volunteer submits the registration
    Then the registration is rejected due to weak password
