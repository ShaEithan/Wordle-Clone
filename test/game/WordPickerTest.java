package game;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.*;

public class WordPickerTest {
  WordPicker wordPicker = new WordPicker();

  @Test
  public void getResponseReturnsResponseString() throws Exception {
    assertTrue(wordPicker.getResponse().length() > 0);
  }

  @Test
  public void parseResponseTakesStringofWordsReturnsListofWords() {
    String wordList = "[FAVOR, TEST, RIVER, FOCUS]";

    assertEquals(List.of("FAVOR", "RIVER", "FOCUS"), wordPicker.parseResponse(wordList));
  }

  @Test
  public void parseTakesEmptyStringofWordsAndReturnsEmptyList() {
    assertEquals(List.of(), wordPicker.parseResponse("[]"));
  }

  @Test
  public void parseThrowsExceptionIfStringDoesNotHaveAList() {
    assertThrows(RuntimeException.class, () -> wordPicker.parseResponse(""));
  }

  @Test
  public void getRandomWordFromSeedAndListofWords() {
    long seed = 6576578768686L;

    assertTrue(wordPicker.getRandomWordGivenASeed(seed, List.of("FAVOR", "TESTS", "RIVER", "FOCUS", "SKILL", "AMAST")).length() > 0);
  }

  @Test
  public void verifyWithSameSeedTwoRandomWordsAreDifferent() throws Exception {
    long seed = 6576578768686L;

    List<String> wordList = wordPicker.parseResponse(wordPicker.getResponse());

    String word1 = wordPicker.getRandomWordGivenASeed(seed, wordList);
    String word2 = wordPicker.getRandomWordGivenASeed(seed, wordList);

    assertNotEquals(word1, word2);
  }

  @Test
  public void verifyGetARandomWordCallsGetResponseParseAndGetRandomWordGivenASeed() throws Exception {
    WordPicker spy = Mockito.spy(new WordPicker());

    when(spy.getResponse()).thenReturn("[PRINT]");
    when(spy.parseResponse("[PRINT]")).thenReturn(List.of("PRINT"));
    when(spy.getRandomWordGivenASeed(6576578768686L, List.of("PRINT"))).thenReturn("PRINT");

    spy.getRandomWord();

    Mockito.verify(spy).getResponse();
    Mockito.verify(spy).parseResponse("[PRINT]");
    Mockito.verify(spy).getRandomWordGivenASeed(anyLong(), anyList());
  }

  @Test
  public void verifyGetARandomWordCallsGetARandomWordGivenASeedWithASeed() throws Exception {
    WordPicker spy = Mockito.spy(new WordPicker());

    spy.getRandomWord();

    Mockito.verify(spy).getRandomWordGivenASeed(anyLong(), anyList());
  }

  @Test
  public void verifyGetARandomWordCallsGetARandomWordGivenASeedWithADifferentSeedOnSecondCall() throws Exception {
    wordPicker.getRandomWord();
    long seed1 = wordPicker.seed;
    wordPicker.getRandomWord();
    long seed2 = wordPicker.seed;

    assertNotEquals(seed1, seed2);
  }

  @Test
  public void checkIfRandomWordIsOfLength5() throws Exception {
    WordPicker spy = Mockito.spy(new WordPicker());

    when(spy.getResponse()).thenReturn("[PRNT]");
    when(spy.parseResponse("[PRNT]")).thenReturn(List.of("PRNT"));

    assertThrows(RuntimeException.class, () -> spy.getRandomWord());
  }

  @Test
  public void checkIfRandomWordIsAllCaps() throws Exception {
    WordPicker spy = Mockito.spy(new WordPicker());

    when(spy.getResponse()).thenReturn("[print]");
    when(spy.parseResponse("[print]")).thenReturn(List.of("PRINT"));

    assertEquals("PRINT", spy.getRandomWord());
  }
}
