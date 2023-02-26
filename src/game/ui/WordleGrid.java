package game.ui;

import game.Response;
import game.Wordle;
import game.Wordle.Match;
import game.Wordle.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class WordleGrid extends JFrame {
  private static final int WORD_SIZE = 5;
  private static final int MAX_NUMBER_GUESSES = 6;

  private static final String TARGET = "FAVOR";
  public String currentGuess = "";
  public static WordleCell[][] cells;

  public static int currentRow = 0;
  public static int currentCol = 0;

  JPanel wordleContainer;
  public static JPanel gridUI;
  JPanel guessButtonPanel;
  public static JButton guessButton;

  @Override
  protected void frameInit() {
    super.frameInit();

    createPanels();
    createCells();

    super.add(wordleContainer);

    guessButton.setEnabled(false);

    super.addKeyListener(new KeyAdapter() {
      final int keyCodeBackSpace = 8;
      final int keyCodeEnter = 10;

      @Override
      public void keyTyped(KeyEvent e) {
        if (currentRow >= MAX_NUMBER_GUESSES || (currentCol == 4 && cells[currentRow][currentCol].cellChar.equals(""))) {
          return;
        }

        if (Character.isLetterOrDigit(e.getKeyChar())){
          Character myChar = e.getKeyChar();
          if(currentGuess.length() < 5) {
            currentGuess += myChar.toString().toUpperCase();
            displayGuess();
          }
        }

        toggleGuessButtonStatus();
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == keyCodeEnter && guessButton.isEnabled()){
          onPressingGuessButton();
        }

        if (e.getKeyCode() == keyCodeBackSpace && currentCol >= 0){
          if (currentCol == 0) {
            return;
          }

          currentCol--;
          toggleGuessButtonStatus();
        }
      }

      @Override
      public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == keyCodeEnter && guessButton.isEnabled()) {
          onPressingGuessButton();
        }

        if(e.getKeyCode() == keyCodeBackSpace && currentGuess.length() > 0) {
          onPressingBackSpace();
        }
      }
    });

    guessButton.setFocusable(false);
  }

  public void createPanels() {
    wordleContainer = new JPanel();
    wordleContainer.setLayout(new BoxLayout(wordleContainer, BoxLayout.Y_AXIS));

    gridUI = new JPanel(new GridLayout(MAX_NUMBER_GUESSES, WORD_SIZE));
    gridUI.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

    guessButtonPanel = new JPanel();
    guessButton = new JButton("Guess");
    guessButtonPanel.add(guessButton);

    guessButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onPressingGuessButton();
      }
    });

    wordleContainer.add(gridUI);
    wordleContainer.add(guessButtonPanel);
  }

  public void createCells() {
    cells = new WordleCell[MAX_NUMBER_GUESSES][WORD_SIZE];

    for(int i = 0; i < MAX_NUMBER_GUESSES; i++) {
      for (int j = 0; j < WORD_SIZE; j++) {
        cells[i][j] = new WordleCell(i, j);
        cells[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cells[i][j].setText(cells[i][j].cellChar);
        cells[i][j].setOpaque(true);
        cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
        cells[i][j].setVerticalAlignment(SwingConstants.CENTER);
        gridUI.add(cells[i][j]);
      }
    }
  }

  public void onPressingGuessButton(){
    if (currentRow < MAX_NUMBER_GUESSES){
      guessButton.setFocusable(false);

      setCellColor();
      showGameStatus();

      currentRow++;
      currentCol = 0;
      currentGuess = "";
      guessButton.setEnabled(false);
    }
  }

  public void onPressingBackSpace(){
    for(int i = 0; i < WORD_SIZE; i++) {
      cells[currentRow][i].setText("");
      cells[currentRow][i].cellChar = "";
    }

    currentGuess = currentGuess.substring(0, currentGuess.length()-1);
    displayGuess();
  }

  public void displayGuess() {
    if(currentGuess.length() > 5) {
      return;
    }

    for(int i = 0; i < currentGuess.length(); i++) {
      cells[currentRow][i].cellChar = ("" + currentGuess.charAt(i)).toUpperCase();
      cells[currentRow][i].setText(("" + currentGuess.charAt(i)).toUpperCase());
      currentCol = i;
    }
  }

  public void setCellColor() {
    Response guessResponse = Wordle.play(TARGET, currentGuess, currentRow);

    var responseColorCodes = Map.of(Match.EXACT, Color.GREEN, Match.EXISTS, Color.YELLOW, Match.NO_MATCH, Color.GRAY);

    for (int i = 0; i < WORD_SIZE; i++) {
      Match matchType = guessResponse.response().get(i);
      cells[currentRow][i].setBackground(responseColorCodes.get(matchType));
    }
  }

  public void toggleGuessButtonStatus(){
    boolean isButtonEnabled = currentGuess.length() == 5;
    guessButton.setEnabled(isButtonEnabled);
  }

  public void showGameStatus() {
    if (currentRow + 1 == MAX_NUMBER_GUESSES && !currentGuess.equals(TARGET)) {
      currentRow++;
    }
    
    Response guessResponse = Wordle.play(TARGET, currentGuess, currentRow);

    if (!guessResponse.status().equals(GameStatus.IN_PROGRESS)) {
      JOptionPane.showMessageDialog(super.getComponent(0), guessResponse.message());
      super.setFocusable(false);
    }
  }

  public static void main(String[] args){
    JFrame myGame = new WordleGrid();
    myGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myGame.setTitle("Wordle");
    myGame.setSize(500, 500);
    myGame.setVisible(true);
  }
}