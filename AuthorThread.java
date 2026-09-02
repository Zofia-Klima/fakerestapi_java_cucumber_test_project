import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;

public class AuthorThread extends Thread {
    private Author author;
    private int postStatusCode;
    private Author postResponseAuthor;

    private Author getResponseAuthor;


    public AuthorThread(Author author){
        this.author = author;
    }

    @Override
    public void run(){
        try{
            Gson gson = new Gson();
	        String jsonRequest = gson.toJson(author);

            // POST section
            HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://fakerestapi.azurewebsites.net/api/v1/Authors"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonRequest))
                .build();
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());

            postStatusCode = postResponse.statusCode();

            postResponseAuthor = gson.fromJson(postResponse.body(), Author.class);

        }catch (Exception e){
            e.printStackTrace();
            }
        
    }

    public void retrieveAuthorByID() throws Exception{
   
        // FakerestApi does not actually create resorces with the use of POST request.
        // However, for the sake of testing the aplication, I have decided to compare the POST response with four previously created authors.
        Gson gson = new Gson();

            // GET section
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(new URI("https://fakerestapi.azurewebsites.net/api/v1/Authors/" + author.getId()))
            .header("Accept", "application/json")
            .GET()
            .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());

        getResponseAuthor = gson.fromJson(getResponse.body(), Author.class);

    }

    public int getPostStatusCode(){
        return postStatusCode;
    }

    public Author getPostResponseAuthor(){
        return postResponseAuthor;
    }

    public Author getGetResponseAuthor(){
        return getResponseAuthor;
    }



}
