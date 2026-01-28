Feature: Volunteer Opportunity Creation

  As an organization
  I want to create volunteer opportunities
  So that volunteers can apply and participate

  Scenario: Create opportunity with valid data
    Given an organization exists with id "org-1"
    When I create an opportunity with:
      | title       | Beach Cleanup |
      | description | Clean the beach area |
      | location    | Aveiro |
      | points      | 50 |
    Then the opportunity is created successfully
    And it is stored in the system

  Scenario: Create opportunity with empty title
    Given an organization exists with id "org-1"
    When I create an opportunity with:
      | title       |   |
      | description | Activity |
      | location    | Aveiro |
      | points      | 20 |
    Then an error is returned saying "Title is required"

  Scenario: Create opportunity with negative points
    Given an organization exists with id "org-1"
    When I create an opportunity with:
      | title       | Tree Planting |
      | description | Environmental action |
      | location    | Campus |
      | points      | -10 |
    Then an error is returned saying "Points must be positive"

  Scenario: Create opportunity for non existing organization
    Given no organization exists with id "org-999"
    When I create an opportunity with:
      | title       | Food Drive |
      | description | Help collecting food |
      | location    | UA |
      | points      | 30 |
    Then an error is returned saying "Organization not found"
