import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;

public class AuthorApiClient {

    public Author[] getAllAuthorsApi() throws Exception {

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://fakerestapi.azurewebsites.net/api/v1/Authors"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();

        Author[] apiAuthors = gson.fromJson(getResponse.body(), Author[].class);

        return apiAuthors;
    }
}