import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.URISyntaxException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        try{
            String url = "https://api.github.com/users/AquibMS";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .header("Accept","application/json")
                    .build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Code: " + response.statusCode());
            HttpHeaders headers = response.headers();
            headers.map().forEach((k,v) -> System.out.println(k + ":" + v));
            System.out.println("Response Body: " + response.body());
        } catch (URISyntaxException | IOException | InterruptedException e){
            System.out.println("could not hit the API");
        }
    }
}