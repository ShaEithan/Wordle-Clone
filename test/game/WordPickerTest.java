package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class WordPickerTest {
  WordPicker wordPicker = new WordPicker();

  @Test
  public void getResponseReturnsResponseString() throws Exception{
    assertTrue(wordPicker.getResponse().length() > 0);
  }

  @Test
  public void parseResponseTakesStringofWordsReturnsListofWords(){
	  String wordList = "FAVOR, TEST, RIVER, FOCUS";
    assertTrue(wordPicker.parseResponse(wordList).size() > 0);
  }

  @Test
  public void parseReturnsTakesEmptyStringofWordsAndReturnsEmptyList(){
    assertEquals(List.of(), wordPicker.parseResponse(" "));
  }

  @Test
  public void parseThrowsExceptionIfStringDoesNotHaveAList(){
    assertThrows(RuntimeException.class, () -> wordPicker.parseResponse(""));
  }
}
