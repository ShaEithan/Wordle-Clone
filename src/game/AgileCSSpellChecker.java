package game;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AgileCSSpellChecker implements SpellChecker {
  String getResponse(String guess) throws IOException, InterruptedException {
    var URL = "http://agilec.cs.uh.edu/spell?check=" + guess;

    var client = HttpClient.newHttpClient();

    var request = HttpRequest.newBuilder(URI.create(URL))
            .build();

    return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
  }

  boolean parseResponse(String response) {
    return Boolean.parseBoolean(response);
  }

  @Override
  public boolean isSpellingCorrect(String guess) {
    try {
      return parseResponse(getResponse(guess));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}