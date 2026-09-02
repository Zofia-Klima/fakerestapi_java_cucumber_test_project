Feature: Author API verification

  Scenario: Send four authors in parallel
    Given four authors are prepared
    When the authors are sent to the API in four parallel threads
    Then all POST requests should return status code 200
    And the POST responses should contain the sent author data


  Scenario: Verify created authors with GET requests
    Given four authors are prepared
    And the authors are sent to the API in four parallel threads
    When I retrieve each author by id
    Then the returned authors should match the authors sent in the POST requests


  Scenario: Compare author data returned by API and Swagger UI
    When I retrieve all authors directly from the API
    And I retrieve all authors from the Swagger UI
    Then the author data returned by the API and UI should be the same


  Scenario: Compare number of authors returned by API and Swagger UI
    When I retrieve all authors directly from the API
    And I retrieve all authors from the Swagger UI
    Then the API and UI should return the same number of authors