package game;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.*;

import static game.Wordle.play;
import static game.Wordle.tally;
import static game.Wordle.Match.*;
import static game.Wordle.GameStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

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
      () -> assertThrows(RuntimeException.class, () -> tally("FAVOR","FOR")),
      () -> assertThrows(RuntimeException.class, () -> tally("FAVOR","FERVER"))
    );
  }

  @Test
  public void playGameWithTargetFAVORGuessFAVOR() {
    var result = play("FAVOR", "FAVOR", 0);

    assertEquals(1, result.attempts());
    assertEquals(List.of(EXACT, EXACT, EXACT, EXACT, EXACT), result.response());
    assertEquals(WIN, result.status());
    assertEquals("Amazing", result.message());
  }

  @Test
  public void playGameWithTargetFAVORGuessDEAL() {
    assertThrows(RuntimeException.class, () -> play("FAVOR", "DEAL", 0));
  }

  @Test
  public void playGameWithTargetFAVORGuessTESTS() {
    var result = play("FAVOR", "TESTS", 0);

    assertEquals(new Response(1, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), result);
  }

  @Test
  public void playGameWithTargetFAVORGuessFAVORWinAtAllAttempts() {
    assertAll(
      () -> assertEquals(new Response(2, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Splendid"), play("FAVOR", "FAVOR", 1)),
      () -> assertEquals(new Response(3, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Awesome"), play("FAVOR", "FAVOR", 2)),
      () -> assertEquals(new Response(4, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Yay"), play("FAVOR", "FAVOR", 3)),
      () -> assertEquals(new Response(5, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Yay"), play("FAVOR", "FAVOR", 4)),
      () -> assertEquals(new Response(6, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), WIN, "Yay"), play("FAVOR", "FAVOR", 5)),
      () -> assertEquals(new Response(7, List.of(EXACT, EXACT, EXACT, EXACT, EXACT), LOSS, "It was FAVOR, better luck next time"), play("FAVOR", "FAVOR", 6))
    );
  }

  @Test
  public void playGameWithTargetFAVORGuessFAVORLoseAtAllAttempts() {
    assertAll(
      () -> assertEquals(new Response(2, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), play("FAVOR", "TESTS", 1)),
      () -> assertEquals(new Response(3, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), play("FAVOR", "TESTS", 2)),
      () -> assertEquals(new Response(4, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), play("FAVOR", "TESTS", 3)),
      () -> assertEquals(new Response(5, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), play("FAVOR", "TESTS", 4)),
      () -> assertEquals(new Response(6, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), IN_PROGRESS, ""), play("FAVOR", "TESTS", 5)),
      () -> assertEquals(new Response(8, List.of(NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH, NO_MATCH), LOSS, "It was FAVOR, better luck next time"), play("FAVOR", "TESTS", 7))
    );
  }
}
