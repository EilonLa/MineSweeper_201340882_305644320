package ui_package;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import activities.GamePlayActivity;
import logic.MineButton;
import logic.MineSweeper_Logic;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

/**
 * Created by אילון on 20/11/2016.
 */

public class GamePlay_UI_Enabler {

    private GamePlayActivity gamePlay;
    private MineSweeper_Logic sweeper;
    private TextView textView_timer = null;
    private TextView textView_numOfMines = null;
    private TextView textView_playerName = null;
    private String playerName;
    private static GridLayout grid;
    private static LinearLayout board;
    private static LinearLayout clockRow;
    private Button restartButton;

    public GamePlay_UI_Enabler(final GamePlayActivity activity, MineSweeper_Logic sweeper) {
        this.sweeper = sweeper;
        this.gamePlay = activity;
        textView_timer = (TextView) gamePlay.findViewById(R.id.textTimer);
        textView_playerName = (TextView) gamePlay.findViewById(R.id.playerName);
        playerName = gamePlay.getPlayerName();
        textView_playerName.setText("Player: " + playerName);
        clockRow = (LinearLayout) gamePlay.findViewById(R.id.upper_row);
        this.restartButton = (Button) gamePlay.findViewById(R.id.restartButton);
        textView_numOfMines = (TextView) gamePlay.findViewById(R.id.numOfMines);
    }

    public void setNumOfMinesTextView() {
        setNumOfMinesTextView(gamePlay);
        if (textView_numOfMines != null)
            clockRow.removeView(textView_numOfMines);

        clockRow.addView(textView_numOfMines);

        gamePlay.getHandler().post(new Runnable() {
            @Override
            public void run() {
                textView_numOfMines.setText("Mines left: " + sweeper.getNumOfMines());
            }
        });
    }

    public LinearLayout getClockRow() {
        return clockRow;
    }

    public void showStartGame() {
        grid = (GridLayout) gamePlay.findViewById(R.id.panel);
        board = (LinearLayout) gamePlay.findViewById(R.id.board);
        grid.setPadding(0, 0, 0, 0);
        board.setPadding(0, 0, 0, 0);
        grid.setColumnCount(sweeper.getMatrix().length);
        grid.setRowCount(sweeper.getMatrix().length);
        if (grid != null)
            board.removeView(grid);

        grid = sweeper.createNewGrid(grid, gamePlay);
        board.addView(grid, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    public void addButtonToGrid(GridLayout grid, MineButton button, int i, int j) {
        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
        Display display = gamePlay.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        param.width = size.x / sweeper.getMatrix().length;
        if (gamePlay.getLevel() != 3) {
            param.height = 100;
        }
        else
            param.height = 200;
        param.setMargins(0, 0, 0, 0);
        param.setGravity(0);
        grid.addView(button, param);
    }

    public void showAllMatrix(MineButton[][] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                buttons[i][j].setHiddenIcon();
            }
        }

    }

    public void setTextForTimer(String textForTimer) {
        textView_timer.setText(textForTimer);
    }

    public TextView getTextView_numOfMines() {
        return textView_numOfMines;
    }

    public void setRestartButton() {
        restartButton.setBackgroundResource(R.mipmap.restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamePlay.startGame();
            }
        });
    }

    public void setNumOfMinesTextView(final GamePlayActivity activity) {
        if (getTextView_numOfMines() != null)
            getClockRow().removeView(getTextView_numOfMines());


        activity.getHandler().post(new Runnable() {
            @Override
            public void run() {
                getTextView_numOfMines().setText("Mines left: " + activity.getGameLogic().getNumOfMines());
            }
        });
    }

    public void completeZeros(int[][] matrix, int i, int j, MineSweeper_Logic sweeper) {
        if (matrix[i][j] == sweeper.getMine() || matrix[i][j] > 0) {
            return;
        }

        if (i == 0 && j == 0 && matrix.length > 1) { // top left corner
            if (!sweeper.getButtons()[i][j + 1].isPressed()) {
                (sweeper.getButtons()[i][j + 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j].isPressed()) {
                (sweeper.getButtons()[i + 1][j]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j + 1].isPressed()) {
                (sweeper.getButtons()[i + 1][j + 1]).callOnClick();
            }
        }


        if (i == 0 && j > 0 && j < matrix.length - 1 && matrix.length > 1) {// top row not corners
            if (!sweeper.getButtons()[i][j - 1].isPressed()) {
                sweeper.getButtons()[i][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j - 1].isPressed()) {
                sweeper.getButtons()[i + 1][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j].isPressed()) {
                sweeper.getButtons()[i + 1][j].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j + 1].isPressed()) {
                sweeper.getButtons()[i + 1][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j + 1].isPressed()) {
                sweeper.getButtons()[i][j + 1].callOnClick();
            }
        }

        if (j == 0 && i > 0 && i < matrix.length - 1 && matrix.length > 1) {// left col not corners
            if (!sweeper.getButtons()[i - 1][j].isPressed()) {
                sweeper.getButtons()[i - 1][j].callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j + 1].isPressed()) {
                sweeper.getButtons()[i - 1][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j + 1].isPressed()) {
                sweeper.getButtons()[i][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j + 1].isPressed()) {
                sweeper.getButtons()[i + 1][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j].isPressed()) {
                sweeper.getButtons()[i + 1][j].callOnClick();
            }
        }

        if (i == matrix.length - 1 && j == 0 && matrix.length > 1) {// bottom left corner
            if (!sweeper.getButtons()[i - 1][j].isPressed()) {
                sweeper.getButtons()[i - 1][j].callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j + 1].isPressed()) {
                sweeper.getButtons()[i - 1][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j + 1].isPressed()) {
                sweeper.getButtons()[i][j + 1].callOnClick();
            }
        }
        if (j > 0 && j < matrix.length - 1 && i == matrix.length - 1 && matrix.length > 1) {// bottom row not corners
            if (!sweeper.getButtons()[i - 1][j].isPressed()) {
                sweeper.getButtons()[i - 1][j].callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j + 1].isPressed()) {
                sweeper.getButtons()[i - 1][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j + 1].isPressed()) {
                sweeper.getButtons()[i][j + 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j - 1].isPressed()) {
                sweeper.getButtons()[i][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j - 1].isPressed()) {
                sweeper.getButtons()[i - 1][j - 1].callOnClick();
            }
        }
        if (i == matrix.length - 1 && j == matrix.length - 1 && matrix.length > 1) {// bottom right corner
            if (!sweeper.getButtons()[i - 1][j].isPressed()) {
                sweeper.getButtons()[i - 1][j].callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j - 1].isPressed()) {
                sweeper.getButtons()[i - 1][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j - 1].isPressed()) {
                sweeper.getButtons()[i][j - 1].callOnClick();
            }
        }
        if (j == matrix[0].length - 1 && i != 0 && i < matrix.length - 1 && matrix.length > 1) {// right col not corners
            if (!sweeper.getButtons()[i - 1][j].isPressed()) {
                sweeper.getButtons()[i - 1][j].callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j - 1].isPressed()) {
                sweeper.getButtons()[i - 1][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i][j - 1].isPressed()) {
                sweeper.getButtons()[i][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j - 1].isPressed()) {
                sweeper.getButtons()[i + 1][j - 1].callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j].isPressed()) {
                sweeper.getButtons()[i + 1][j].callOnClick();
            }
        }
        if (j == matrix.length - 1 && i == 0 && matrix.length > 1) {// top right corner
            if (!sweeper.getButtons()[i][j - 1].isPressed()) {
                (sweeper.getButtons()[i][j - 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j].isPressed()) {
                (sweeper.getButtons()[i + 1][j]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j - 1].isPressed()) {
                (sweeper.getButtons()[i + 1][j - 1]).callOnClick();
            }
        }

        if (i < matrix.length - 1 && j < matrix[0].length - 1 && i != 0 && j != 0 && matrix.length > 1) {//middle
            if (!sweeper.getButtons()[i][j - 1].isPressed()) {
                (sweeper.getButtons()[i][j - 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j].isPressed()) {
                (sweeper.getButtons()[i + 1][j]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j - 1].isPressed()) {
                (sweeper.getButtons()[i + 1][j - 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i + 1][j + 1].isPressed()) {
                (sweeper.getButtons()[i + 1][j + 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i][j + 1].isPressed()) {
                (sweeper.getButtons()[i][j + 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j + 1].isPressed()) {
                (sweeper.getButtons()[i - 1][j + 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j + 1].isPressed()) {
                (sweeper.getButtons()[i - 1][j + 1]).callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j].isPressed()) {
                (sweeper.getButtons()[i - 1][j]).callOnClick();
            }
            if (!sweeper.getButtons()[i - 1][j - 1].isPressed()) {
                (sweeper.getButtons()[i - 1][j - 1]).callOnClick();
            }
        }
    }
}
