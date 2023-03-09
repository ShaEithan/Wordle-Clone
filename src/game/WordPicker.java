package game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class WordPicker {
  String getResponse() throws Exception {
    var URL = "https://agilec.cs.uh.edu/words";

    var client = HttpClient.newHttpClient();

    var request = HttpRequest.newBuilder(URI.create(URL))
            .build();

    String wordString = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

    return wordString.substring(1, wordString.length() - 1).replaceAll("[^A-Za-z]+", " ");
  }

  List<String> parseResponse(String wordString) throws RuntimeException{
    if (wordString.equals("")){
      throw new RuntimeException("String does not have a list");
    }
    if (wordString.equals(" ")){
      return List.of();
    }

    String[] stringList = wordString.split(" ");

    return Arrays.asList(stringList);
  }
}
