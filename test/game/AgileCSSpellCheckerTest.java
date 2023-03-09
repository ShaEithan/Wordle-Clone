package game;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AgileCSSpellCheckerTest{
  AgileCSSpellChecker agileCSSpellChecker = new AgileCSSpellChecker();

  @Test
  public void getResponseTakesFAVORReturnsResponse() throws Exception{
    assertTrue(agileCSSpellChecker.getResponse("FAVOR").length() > 0);
  }

  @Test
  public void parseTakesTruetoReturnBooleanTrue(){
    assertTrue(agileCSSpellChecker.parseResponse("true"));
  }

  @Test
  public void parseTakesFalsetoReturnBooleanFalse(){
    assertFalse(agileCSSpellChecker.parseResponse("false"));
  }

  @Test
  public void isSpellingCorrectCallsGetResponseAndParse() throws Exception{
    AgileCSSpellChecker agileCSSpellCheckerMock =  Mockito.mock(AgileCSSpellChecker.class);

    when(agileCSSpellCheckerMock.getResponse("FAVOR")).thenReturn("true");
    when(agileCSSpellCheckerMock.parseResponse("true")).thenReturn(true);
    when(agileCSSpellCheckerMock.isSpellingCorrect(anyString())).thenCallRealMethod();

    boolean result = agileCSSpellCheckerMock.isSpellingCorrect("FAVOR");

    Mockito.verify(agileCSSpellCheckerMock).getResponse("FAVOR");

    Mockito.verify(agileCSSpellCheckerMock).parseResponse("true");
    assertTrue(result);
  }

  @Test
  public void isSpellingCorrectPassesOnExceptionFromGetResponse() throws Exception{
    AgileCSSpellChecker spy = Mockito.spy(new AgileCSSpellChecker());

    Mockito.doThrow(IOException.class).when(spy).getResponse(anyString());

    assertThrows(RuntimeException.class, () -> spy.isSpellingCorrect("FAVOR"));
  }
}