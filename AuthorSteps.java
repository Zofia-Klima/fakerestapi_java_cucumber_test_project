import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.Objects;

public class AuthorSteps {
    private Author[] authors;
    private AuthorThread[] threads;

    private Author[] apiAuthors;
    private Author[] uiAuthors;

    //Here, I create four authors and store them in the authors array.
    @Given("four authors are prepared")
    public void fourAuthorsArePrepared(){
        Author author1 = new Author();
        author1.setId(0);
        author1.setIdBook(0);
        author1.setFirstName("Adam");
        author1.setLastName("Mickiewicz");

        Author author2 = new Author();
        author2.setId(1);
        author2.setIdBook(1);
        author2.setFirstName("Edgar Allan");
        author2.setLastName("Poe");

        Author author3 = new Author();
	    author3.setId(2);
        author3.setIdBook(2);
        author3.setFirstName("Witold");
        author3.setLastName("Gombrowicz");

        Author author4 = new Author();
	    author4.setId(3);
        author4.setIdBook(3);
        author4.setFirstName("Franz");
        author4.setLastName("Kafka");

        authors = new Author[] {author1, author2, author3, author4};
    }

    @When("the authors are sent to the API in four parallel threads")
    public void sendAuthorThreads() throws InterruptedException{

        threads = new AuthorThread[authors.length];

        //Creating 4 threads - one for each author
        for(int i = 0; i < threads.length; i++){
            threads[i] = new AuthorThread(authors[i]);
        }

        // Starting all 4 threads
        for(AuthorThread thread : threads){
            thread.start();
        }

        // Waiting for all 4 threads to finish.
        for(AuthorThread thread : threads){
            thread.join();
        }

    }

    @Then("all POST requests should return status code 200")
    public void postRequestsShouldReturn200(){
        
        // Verifies that all POST requests returned status code 200.
        for(int i = 0; i < threads.length; i++){
            assert threads[i].getPostStatusCode() == 200 : "POST failed for author: " + authors[i].getFirstName() + " " + authors[i].getLastName();
        }
    }

    @And("the POST responses should contain the sent author data")
    public void postResponsesShouldContainAuthorData(){

        // comparing all fields of the sent authors with the corresponding POST response data.
        for(int i = 0; i < threads.length; i++){
            Author sentAuthor = authors[i];
            Author responseAuthor = threads[i].getPostResponseAuthor();

            assert responseAuthor != null : "POST response body is empty";

            assert sentAuthor.getId().equals(responseAuthor.getId())
            && sentAuthor.getIdBook().equals(responseAuthor.getIdBook())
            && sentAuthor.getFirstName().equals(responseAuthor.getFirstName())
            && sentAuthor.getLastName().equals(responseAuthor.getLastName())
            : "POST responsse data is different for author: "
             + sentAuthor.getFirstName() + " " + sentAuthor.getLastName();

        }
    }

    @When("I retrieve each author by id")
    public void retrieveEachAuthorById() throws Exception{
        for(AuthorThread thread : threads){
            thread.retrieveAuthorByID();
        }
    }

    @Then("the returned authors should match the authors sent in the POST requests")
    public void returnedAuthorsShouldMatchSent(){

        StringBuilder errors = new StringBuilder();
        for(int i = 0; i < threads.length; i++){
            Author getAuthor = threads[i].getGetResponseAuthor();
            Author sentAuthor = authors[i];

            assert getAuthor != null : "GET response body is empty";
            
            boolean authorsAreTheSame = Objects.equals(getAuthor.getId(), sentAuthor.getId()) 
            && Objects.equals(getAuthor.getIdBook(), sentAuthor.getIdBook())
            && Objects.equals(getAuthor.getFirstName(), sentAuthor.getFirstName())
            && Objects.equals(getAuthor.getLastName(), sentAuthor.getLastName());
            
            //If given author's data is different from the GET response under the same ID, program appends this author's first and last name to display it later.
            if(!authorsAreTheSame){
                errors.append("GET data is different for author: ")
                .append(sentAuthor.getFirstName())
                .append(" ")
                .append(sentAuthor.getLastName())
                .append("\n");
            }

        }
        assert errors.length() == 0 : errors.toString();
    }

    @When("I retrieve all authors directly from the API")
    public void retriveAllAuthorsApi() throws Exception{
        
        AuthorApiClient apiClient = new AuthorApiClient();
        apiAuthors = apiClient.getAllAuthorsApi();
    }

    @And("I retrieve all authors from the Swagger UI")
    public void retrieveAllAuthorsUi() throws Exception{

        AuthorUiTest uiTest = new AuthorUiTest();
        uiAuthors = uiTest.getAllAuthorsUi();
    }

    @Then("the author data returned by the API and UI should be the same")
    public void apiAndUiDataShouldBeTheSame(){

        assert apiAuthors != null : "API authors response is empty";
        assert uiAuthors != null : "UI authors response is empty";


        StringBuilder differentIndices = new StringBuilder();
        int i = 0;

            while (true) {

                if (i >= apiAuthors.length || i >= uiAuthors.length) {
                //end the loop once the end of one of the author lists is reached.
                    break;
                }

            Author apiAuthor = apiAuthors[i];
            Author uiAuthor = uiAuthors[i];

            boolean authorsAreTheSame =
                    apiAuthor.getId().equals(uiAuthor.getId())
                    && apiAuthor.getIdBook().equals(uiAuthor.getIdBook())
                    && apiAuthor.getFirstName().equals(uiAuthor.getFirstName())
                    && apiAuthor.getLastName().equals(uiAuthor.getLastName());

            // Append index of the author which is mismatched between API and UI response. It is displayed later, with the assertion.
            if (!authorsAreTheSame) {
                if (differentIndices.length() > 0) {
                    differentIndices.append(", ");
                }
                differentIndices.append(i);
            }
            i++;

        }
        // Display indices of mismatched records if the assertion fails.
        assert differentIndices.length() == 0 : "API and UI author data is different at indices: " + differentIndices.toString();
    }

    @Then("the API and UI should return the same number of authors")
    public void apiAndUiLengthShouldBeTheSame(){

        assert apiAuthors.length == uiAuthors.length : "The number of authors returned by API and UI is different. API authors number: " + apiAuthors.length + " UI authors number: " + uiAuthors.length;
    }
}
