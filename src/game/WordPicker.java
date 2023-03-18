package game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class WordPicker {
  private Random generator = new Random();
  long seed;

  String getResponse() throws Exception {
    var URL = "https://agilec.cs.uh.edu/words";

    var client = HttpClient.newHttpClient();

    var request = HttpRequest.newBuilder(URI.create(URL))
            .build();

    return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
  }

  List<String> parseResponse(String wordString) throws RuntimeException {
    if (wordString.equals("[]")) {
      return List.of();
    }

    wordString = wordString.substring(1, wordString.length() - 1).replaceAll("[^A-Za-z]+", " ");

    List<String> wordList = new ArrayList<>(Arrays.asList(wordString.split(" ")));
    wordList.removeIf(element -> element.length() != 5);

    return wordList;

  }

  String getRandomWordGivenASeed(long _seed, List<String> wordList) {
    seed = _seed;

    int randomWordIndex = generator.nextInt(wordList.size());

    return wordList.get(randomWordIndex);
  }

  public String getRandomWord() throws Exception {
    List<String> parsedResponse = parseResponse(getResponse());
    String word = getRandomWordGivenASeed(System.currentTimeMillis(), parsedResponse);

    word.toUpperCase();

    if (!checkWordIsValid(word)){
      throw new RuntimeException("Invalid Word");
    }

    return word;
  }

  public boolean checkWordIsValid(String word) {
    return word.length() == 5 ? true : false;
  }
}
