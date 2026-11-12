# Project Requirements

The project was created based on the following guidelines:

Using the public API available at https://fakerestapi.azurewebsites.net/index.html, write a test in Java. All API operations must be performed from within the Java code, not directly through the Swagger interface.

Requirements:

1. Create four authors (one author per thread) in four parallel threads.

2. Verify via the API that all authors were created correctly.

3. Next, open the UI for the same page and compare the authors displayed in the UI with the data retrieved from the API.

4. Prepare a report summarizing the results.

5. For better readability and structure, consider using Cucumber.

## Requirements

This project requires Java and Google Chrome to be installed.

External libraries are located in the `lib` directory.

These include:

- Gson
- Selenium WebDriver
- dependencies required by Selenium
- Cucumber
- dependencies required by Cucumber

## Compilation

To compile the project on Windows, run the following:

```powershell
javac -cp ".;lib/*;lib/cucumber/*" Author.java AuthorThread.java AuthorApiClient.java AuthorUiTest.java AuthorSteps.java
```

## Run

To run all scenarios:

```powershell
java -ea -cp ".;lib/*;lib/cucumber/*" io.cucumber.core.cli.Main features/authors.feature
```

The `-ea` option enables Java assertions.

## Project Structure

- `AuthorSteps.java` - definitions of Cucumber steps and assertions used in test scenarios.
- `Author.java` - author data model.
- `AuthorThread.java` - handles POST operations for a single author executed in a separate thread and GET requests for an author by ID.
- `AuthorApiClient.java` - retrieves a list of all authors directly from the API.
- `AuthorUiTest.java` - interacts with Swagger UI using Selenium and retrieves author data displayed in the UI.
- `features/authors.feature` - contains test scenarios written in Gherkin.
- `lib/` - contains Gson, Selenium WebDriver, and required dependencies.
- `lib/cucumber/` - contains the libraries required to run Cucumber without using Maven.
- `docs/FakeRESTApi_Cucumber_version_Test_Report.pdf` - contains the test report.

## Selenium

Simple Selenium code is used on my own initiative due to point 3 of the task:
>(...)
>3. Next, open the UI of the same page and compare the authors displayed in the UI with the data retrieved from the API.

I assume the intention was not to open the UI using Selenium, but I did so to present the usage of Selenium in Java.

The code in the AuthorUiTest.java file opens the page https://fakerestapi.azurewebsites.net/index.html, calls the GET endpoint /api/v1/Authors, then clicks the “Try it out” and “Execute” buttons, and stores the displayed response body in the variable responseBody.

This allows me to compare the data retrieved directly via a GET request with the data returned by the same endpoint when run in Swagger UI.

## Additional Notes

The comments in the code are written in English to maintain best practices and consistency in the code.
The project includes a version converted to the Cucumber structure.
The original project was made in polish language and this is the translated version.
