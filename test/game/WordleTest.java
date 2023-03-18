package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static game.Wordle.tally;
import static game.Wordle.Match.*;
import static game.Wordle.GameStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class WordleTest {
  private Wordle wordle;
  SpellChecker spellChecker;

  @BeforeEach
  void init() {
    spellChecker = Mockito.mock(SpellChecker.class);
    wordle = new Wordle();
    wordle.setSpellChecker(spellChecker);
    when(spellChecker.isSpellingCorrect(anyString())).thenReturn(true);
  }

  @Test
  public void canary() {
    assertTrue(true);
  }

  @Test
  public void tallyForTargetANYguessALL() {
    assertAll(
            () -> assertEquals(List.of(EXACT, EXACT, EXACT, EXACT, EXACT), tally("FAVOR", "FAVOR")),
            () -> assertEquals(List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), tally("FAVOR", "TESTS")),
            () -> assertEquals(List.of(EXISTS, EXACT, NO_MATCH, NO_MATCH, NO_MATCH), tally("FAVOR", "RAPID")),
            () -> assertEquals(List.of(NO_MATCH, EXACT, NO_MATCH, EXACT, EXACT), tally("FAVOR", "MAYOR")),
            () -> assertEquals(List.of(NO_MATCH, NO_MATCH, EXACT, NO_MATCH, EXACT), tally("FAVOR", "RIVER")),
            () -> assertEquals(List.of(EXISTS, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), tally("FAVOR", "AMAST"))
    );
  }

  @Test
  public void tallyForTargetSKILLguessALL() {
    assertAll(
            () -> assertEquals(List.of(EXACT, EXACT, EXACT, EXACT, EXACT), tally("SKILL", "SKILL")),
            () -> assertEquals(List.of(EXACT, NO_MATCH, EXACT, NO_MATCH, EXACT), tally("SKILL", "SWIRL")),
            () -> assertEquals(List.of(NO_MATCH, EXISTS, NO_MATCH, NO_MATCH, EXACT), tally("SKILL", "CIVIL")),
            () -> assertEquals(List.of(EXACT, NO_MATCH, EXACT, NO_MATCH, NO_MATCH), tally("SKILL", "SHIMS")),
            () -> assertEquals(List.of(EXACT, EXISTS, EXISTS, EXACT, NO_MATCH), tally("SKILL", "SILLY")),
            () -> assertEquals(List.of(EXACT, EXISTS, EXACT, NO_MATCH, NO_MATCH), tally("SKILL", "SLICE"))
    );
  }

  @Test
  public void guessIsInvalidLength() {
    assertAll(
            () -> assertThrows(RuntimeException.class, () -> tally("FAVOR", "FOR")),
            () -> assertThrows(RuntimeException.class, () -> tally("FAVOR", "FERVER"))
    );
  }

  @Test
  public void playGameWithTargetFAVORGuessFAVOR() {
    var result = wordle.play("FAVOR", "FAVOR", 0);

    assertEquals(1, result.attempts());
    assertEquals(List.of(EXACT, EXACT, EXACT, EXACT, EXACT), result.response());
    assertEquals(WIN, result.status());
    assertEquals("Amazing", result.message());
  }

  @Test
  public void playGameWithTargetFAVORGuessDEAL() {
    assertThrows(RuntimeException.class, () -> wordle.play("FAVOR", "DEAL", 0));
  }

  @Test
  public void playGameWithTargetFAVORGuessTESTS() {
    var result = wordle.play("FAVOR", "TESTS", 0);
    assertEquals(new Response(1, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), result);
  }

  @Test
  public void playGameWithTargetFAVORGuessFAVORWinAtAllAttempts() {
    assertAll(
            () -> assertEquals(new Response(2, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Splendid"), wordle.play("FAVOR", "FAVOR", 1)),
            () -> assertEquals(new Response(3, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Awesome"), wordle.play("FAVOR", "FAVOR", 2)),
            () -> assertEquals(new Response(4, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Yay"), wordle.play("FAVOR", "FAVOR", 3)),
            () -> assertEquals(new Response(5, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Yay"), wordle.play("FAVOR", "FAVOR", 4)),
            () -> assertEquals(new Response(6, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Yay"), wordle.play("FAVOR", "FAVOR", 5)),
            () -> assertEquals(new Response(7, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), LOSS, "It was FAVOR, better luck next time"), wordle.play("FAVOR", "FAVOR", 6))
    );
  }

  @Test
  public void playGameWithTargetFAVORGuessFAVORLoseAtAllAttempts() {
    assertAll(
            () -> assertEquals(new Response(2, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), wordle.play("FAVOR", "TESTS", 1)),
            () -> assertEquals(new Response(3, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), wordle.play("FAVOR", "TESTS", 2)),
            () -> assertEquals(new Response(4, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), wordle.play("FAVOR", "TESTS", 3)),
            () -> assertEquals(new Response(5, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), wordle.play("FAVOR", "TESTS", 4)),
            () -> assertEquals(new Response(6, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), wordle.play("FAVOR", "TESTS", 5)),
            () -> assertEquals(new Response(8, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), LOSS, "It was FAVOR, better luck next time"), wordle.play("FAVOR", "TESTS", 7))
    );
  }

  @Test
  public void playWithCorrectSpellingForFAVOR() {
    when(spellChecker.isSpellingCorrect("FAVOR")).thenReturn(true);

    var response = wordle.play("FAVOR", "FAVOR", 0);

    assertEquals(new Response(1, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Amazing"), response);
    Mockito.verify(spellChecker).isSpellingCorrect("FAVOR");
  }

  @Test
  public void playWithCorrectSpellingForRIVER() {
    when(spellChecker.isSpellingCorrect("RIVER")).thenReturn(true);

    var response = wordle.play("FAVOR", "RIVER", 0);

    assertEquals(new Response(1, List.of(NO_MATCH, NO_MATCH, EXACT, NO_MATCH, EXACT), IN_PROGRESS, ""), response);
    Mockito.verify(spellChecker).isSpellingCorrect("RIVER");
  }

  @Test
  public void playWithIncorrectSpellingForFAVOR() {
    when(spellChecker.isSpellingCorrect("FAVOR")).thenReturn(false);

    var response = wordle.play("FAVOR", "FAVOR", 0);

    assertEquals(new Response(0, List.of(), WRONG_SPELLING, ""), response);
    Mockito.verify(spellChecker).isSpellingCorrect("FAVOR");
  }

  @Test
  public void playWithIncorrectSpellingForRIVER() {
    when(spellChecker.isSpellingCorrect("RIVER")).thenReturn(false);

    var response = wordle.play("FAVOR", "RIVER", 1);

    assertEquals(new Response(1, List.of(), WRONG_SPELLING, ""), response);
    Mockito.verify(spellChecker).isSpellingCorrect("RIVER");
  }

  @Test
  public void playPassesOnExceptionFromIsSpellingCorrectToCaller() {
    Mockito.doThrow(RuntimeException.class).when(spellChecker).isSpellingCorrect("FAVOR");

    assertThrows(RuntimeException.class, () -> wordle.play("FAVOR", "FAVOR", 1));
  }
}

