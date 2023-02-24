package game;

import game.Wordle.*;
import java.util.*;

public record Response(int attempts, List<Match> response, GameStatus status, String message) {
}
