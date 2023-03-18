package game;

import java.util.*;
import java.util.stream.*;

public class Wordle {
  final static int WORD_SIZE = 5;
  final static int MAX_GUESS_NUMBER = 6;
  public enum Match {EXACT, EXISTS, NO_MATCH}
  public enum GameStatus {WIN, LOSS, IN_PROGRESS, WRONG_SPELLING}

  private SpellChecker spellChecker;

  public void setSpellChecker(SpellChecker aSpellChecker) {
    spellChecker = aSpellChecker;
  }

  public
  static List<Match> tally(String target, String guess) throws RuntimeException {
    if (guess.length() != WORD_SIZE) {
      throw new RuntimeException("Invalid Guess Length");
    }

    return IntStream.range(0, WORD_SIZE)
            .mapToObj(index -> tallyForPosition(index, target, guess))
            .toList();
  }

  private static Match tallyForPosition(int position, String target, String guess) {
    if(target.charAt(position) == guess.charAt(position)) {
      return Match.EXACT;
    }

    String curLetterAtPosition = String.valueOf(guess.charAt(position));

    int positionalMatches = countPositionalMatches(target, guess, curLetterAtPosition);
    int nonPositionalOccurrencesInTarget = countNumberOfOccurrencesUntilPosition(WORD_SIZE , target, curLetterAtPosition) - positionalMatches;
    int numberOfOccurrencesInGuessUntilPosition = countNumberOfOccurrencesUntilPosition(position, guess, curLetterAtPosition);

    return nonPositionalOccurrencesInTarget > numberOfOccurrencesInGuessUntilPosition ? Match.EXISTS : Match.NO_MATCH;
  }

  private static int countPositionalMatches(String target, String guess, String curLetter) {
    return (int) IntStream.range(0, WORD_SIZE)
            .filter(index -> String.valueOf(target.charAt(index)).equals(curLetter))
            .filter(index -> String.valueOf(guess.charAt(index)).equals(curLetter))
            .count();
  }

  private static int countNumberOfOccurrencesUntilPosition(int position, String word, String letter) {
    return (int) IntStream.range(0, position)
            .filter(index -> String.valueOf(word.charAt(index)).equals(letter))
            .count();
  }

  static GameStatus getGameStatus(int attempts, boolean isAllExact){
    return ((attempts + 1) > MAX_GUESS_NUMBER) ? GameStatus.LOSS
            : (isAllExact) ? GameStatus.WIN
            : GameStatus.IN_PROGRESS;
  }

  static String getEndGameMessage(GameStatus gameStatus, int attempts, final String target){
    List<String> messageBank = List.of("Amazing", "Splendid", "Awesome");

    return (gameStatus == GameStatus.WIN) ? messageBank.get(attempts)
            : (gameStatus == GameStatus.LOSS) ? ("It was " + target + ", better luck next time")
            : "";
  }

  public Response play(final String target, final String guess, final int attempts) {
    if(!spellChecker.isSpellingCorrect(guess)) {
      return new Response(attempts, List.of(), GameStatus.WRONG_SPELLING, "");
    }

    var matches = tally(target, guess);

    boolean isAllExact = IntStream.range(0, WORD_SIZE)
            .allMatch(index -> matches.get(index) == Match.EXACT);


    GameStatus gameStatus = getGameStatus(attempts, isAllExact);

    String message = gameStatus == GameStatus.WIN && attempts >= 3 ?
            "Yay" : getEndGameMessage(gameStatus, attempts, target);

    return new Response(attempts + 1, matches, gameStatus, message);
  }
}
