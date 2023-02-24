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
  public static WordleCell[][] cells;

  public static int currentRow = 0;
  public static int currentCol = 0;

  JPanel wordleContainer;
  public static JPanel gridUI;
  JPanel guessButtonPanel;
  public static JButton guessButton;

  @Override
  protected void frameInit(){
    super.frameInit();
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    createPanels();
    createCells();

    super.add(wordleContainer);

    guessButton.setEnabled(false);

    super.addKeyListener(new KeyAdapter() {
      int keyCodeBackSpace = 8;
      int keyCodeEnter = 10;

      @Override
      public void keyTyped(KeyEvent e) {
        Character myChar = e.getKeyChar();
        if (currentCol == 4 && cells[currentRow][currentCol].cellChar != ""){
          return;
        }

        if (currentCol < WORD_SIZE && Character.isLetterOrDigit(myChar)) {
          cells[currentRow][currentCol].cellChar = String.valueOf(myChar);
          cells[currentRow][currentCol].setText(cells[currentRow][currentCol].cellChar.toUpperCase());
          e.consume();

          if (currentCol == 4) {
            guessButton.setEnabled(true);
            return;
          }

          currentCol++;
        }
      }

      @Override
      public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == keyCodeBackSpace && currentCol >= 0){
          if (currentCol == 0) {
            return;
          }

          currentCol--;
        }
      }

      @Override
      public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == keyCodeEnter && guessButton.isEnabled()){
          if (currentRow < MAX_NUMBER_GUESSES){
            guessButton.setFocusable(false);

            setCellColor();
            showGameStatus();

            currentRow++;
            currentCol = 0;
            guessButton.setEnabled(false);
          }
        }

        if (e.getKeyCode() == keyCodeBackSpace && currentCol >= 0){

          if (cells[currentRow][currentCol].getText() == "" && currentCol > 0){
            cells[currentRow][currentCol - 1].setText("");
            cells[currentRow][currentCol].cellChar = "";
            return;
          }

          cells[currentRow][currentCol].setText("");
          e.consume();
          cells[currentRow][currentCol].cellChar = "";

          if (currentCol == 0) {
            return;
          }

          if (currentCol < 4) {
            guessButton.setEnabled(false);
          }
        }
      }
    });

    super.setTitle("Wordle");
    super.setSize(500, 500);
    super.setVisible(true);
  }

  public void createPanels(){
    wordleContainer = new JPanel();
    wordleContainer.setLayout(new BoxLayout(wordleContainer, BoxLayout.Y_AXIS));

    gridUI = new JPanel(new GridLayout(MAX_NUMBER_GUESSES, WORD_SIZE));
    gridUI.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

    guessButtonPanel = new JPanel();
    guessButton = new JButton("Guess");
    guessButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentRow < MAX_NUMBER_GUESSES){
          guessButton.setFocusable(false);

          setCellColor();
          showGameStatus();

          currentRow++;
          currentCol = 0;
          guessButton.setEnabled(false);
        }
      }
    });

    guessButtonPanel.add(guessButton);

    wordleContainer.add(gridUI);
    wordleContainer.add(guessButtonPanel);
  }

  public void createCells(){
    cells = new WordleCell[MAX_NUMBER_GUESSES][WORD_SIZE];

    for(int i = 0; i < MAX_NUMBER_GUESSES; i++){
      for (int j = 0; j < WORD_SIZE; j++){
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

  public String getGuess(){
    String guessResult = "";
    for (int j = 0; j < WORD_SIZE; j++){
      guessResult += cells[currentRow][j].cellChar.toUpperCase();
    }
    return guessResult;
  }

  public void setCellColor() {
    String myGuess = getGuess();
    Response guessResponse = Wordle.play(TARGET, myGuess, currentRow);

    HashMap<Match, Color> myMap = new HashMap<>();
    myMap.put(Match.EXACT, Color.GREEN);
    myMap.put(Match.EXISTS, Color.YELLOW);
    myMap.put(Match.NO_MATCH, Color.GRAY);

    for (int i = 0; i < WORD_SIZE; i++) {
      Match matchType = guessResponse.response().get(i);

      cells[currentRow][i].setBackground(myMap.get(matchType));
    }
  }

  public void showGameStatus(){
    String myGuess = getGuess();

    if (currentRow + 1 == MAX_NUMBER_GUESSES){
      currentRow++;
    }
    
    Response guessResponse = Wordle.play(TARGET, myGuess, currentRow);

    if (!guessResponse.status().equals(GameStatus.IN_PROGRESS)){
      JOptionPane.showMessageDialog(super.getComponent(0), guessResponse.message());
      super.setFocusable(false);
    }
  }

  public static void main(String[] args){
    new WordleGrid();
  }
}