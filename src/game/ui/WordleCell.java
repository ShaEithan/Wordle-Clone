package game.ui;

import javax.swing.*;
import java.awt.*;

public class WordleCell extends JLabel{
  public final int row;
  public final int column;

  public String cellChar = "";
  public WordleCell(int curRow, int curColumn){
    row = curRow;
    column = curColumn;
  }
}
