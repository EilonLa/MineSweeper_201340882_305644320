package ui_enablers;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import activities.GamePlayActivity;
import logic.MineButton;
import logic.MineSweeper_Logic;

/**
 * Created by אילון on 20/11/2016.
 */

public class GamePlay_UI_Enabler {

    public static GridLayout grid;
    private static LinearLayout board;
    private static RelativeLayout upperRow;
    private static MineButton[][] buttons;
    private static int[] numbersIcons = {R.mipmap.zero, R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four,
            R.mipmap.five, R.mipmap.six, R.mipmap.seven, R.mipmap.eight};
    public boolean initiated = false;
    private GamePlayActivity activity;
    private MineSweeper_Logic logic;
    private TextView textView_timer = null;
    private TextView textView_numOfMines = null;
    private TextView textView_playerName = null;
    private String playerName;
    private Button restartButton;
    private ObjectAnimator rotator;
    private Handler handler;
    private boolean gameEnded = false;
    private int startingRowToFillMines = 0;
    private int startingColToFillMines = 0;
    private int startingRowToFillMinesFromBottom = 0;
    private int startingColToFillMinesFromTheRight = 0;
    private boolean showedAll = false;

    public GamePlay_UI_Enabler(final GamePlayActivity activity, final MineSweeper_Logic logic) {
        Log.d("GamePlay_UI_Enabler", "constructor:");
        this.logic = logic;
        this.activity = activity;
        handler = new Handler();
        board = (LinearLayout) activity.findViewById(R.id.board);
        grid = (GridLayout) activity.findViewById(R.id.panel);
        upperRow = (RelativeLayout) this.activity.findViewById(R.id.upperRow);
        textView_timer = (TextView) this.activity.findViewById(R.id.textTimer);
        textView_playerName = (TextView) this.activity.findViewById(R.id.playerName);
        playerName = this.activity.getPlayerName();
        textView_playerName.setText("Player: " + playerName);
        this.restartButton = (Button) this.activity.findViewById(R.id.restartButton);
        textView_numOfMines = (TextView) this.activity.findViewById(R.id.numOfMines);
        setRestartButton();
    }

    public static MineButton[][] getButtons() {
        return buttons;
    }

    public void setNumOfMinesTextView() {
        Log.d("GamePlay_UI_Enabler", "setNumOfMinesTextView:");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView_numOfMines != null) {
                    ((ViewGroup) textView_numOfMines.getParent()).removeView(textView_numOfMines);
                    upperRow.removeView(textView_numOfMines);
                }
                upperRow.addView(textView_numOfMines);
                textView_numOfMines.setText("Mines left: " + logic.getNumOfMines());
            }
        });

    }

    public void showStartGame() {
        Log.d("GamePlay_UI_Enabler", "showStartGame:");
        grid.setPadding(0, 0, 0, 0);
        board.setPadding(0, 0, 0, 0);
        grid.setColumnCount(logic.getMatrix().length);
        grid.setRowCount(logic.getMatrix().length);
        if (grid != null) {
            board.removeView(grid);
        }
        board.addView(grid, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public GridLayout createNewGrid(GridLayout grid) {
        Log.d("GamePlay_UI_Enabler", "createNewGrid:");
        buttons = new MineButton[logic.getMatrix().length][logic.getMatrix().length];
        for (int i = 0; i < logic.getMatrix().length; i++) {
            for (int j = 0; j < logic.getMatrix()[0].length; j++) {

                if (logic.getMatrix()[i][j] == logic.getMine())
                    buttons[i][j] = new MineButton(this, activity, logic, R.mipmap.mine, logic.getMatrix(), i, j);
                else
                    buttons[i][j] = new MineButton(this, activity, logic, numbersIcons[logic.getMatrix()[i][j]], logic.getMatrix(), i, j);

                addButtonToGrid(grid, buttons[i][j], i, j);
            }
        }
        return grid;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public void updateHiddenIconIdForButton(int row, int col, int iconId) {
        Log.d("GamePlay_UI_Enabler", "updateHiddenIconIdForButton:");
        if ((buttons == null || buttons[row][col] == null))
            return;

        else {
            buttons[row][col].setHiddenIconId(iconId);
        }
    }

    public void updateBackGroundIconForButton(int row, int col, int iconId) {
        Log.d("GamePlay_UI_Enabler", "updateBackGroundIconForButton:");
        if ((buttons == null || buttons[row][col] == null))
            return;
        else {
            if (buttons[row][col].isPressed())
                buttons[row][col].setIcon(iconId);
            else
                buttons[row][col].setIcon(R.mipmap.hidden);
        }
    }

    public int[] getNumbersIcons() {
        return numbersIcons;
    }

    public void addButtonToGrid(GridLayout grid, MineButton button, int i, int j) {
        Log.d("GamePlay_UI_Enabler", "addButtonToGrid:");
        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        param.width = size.x / logic.getMatrix().length;
        if (activity.getLevel() != 3) {
            param.height = 100;
        } else
            param.height = 200;
        param.setMargins(0, 0, 0, 0);
        param.setGravity(0);
        grid.addView(button, param);
    }

    public void showAllMatrix() {
        Log.d("GamePlay_UI_Enabler", "showAllMatrix:");
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                buttons[i][j].setHiddenIcon();
            }
        }
    }
    public void explodedMine(int row, int col) {
        Log.d("GamePlay_UI_Enabler", "explodedMine:");
        buttons[row][col].setIcon(R.drawable.blast);
        AnimationDrawable blast;
        blast = (AnimationDrawable) buttons[row][col].getBackground();
        blast.start();
        activity.getGameTimer().pause(true);
        dropAll(buttons[row][col]);
    }

    public void dropAll(final MineButton button) {
        Log.d("GamePlay_UI_Enabler", "dropAll:");
        Display d = activity.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        d.getSize(size);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < buttons.length; i++) {
                    for (int j = 0; j < buttons.length; j++) {
                        if (buttons[i][j] == null || buttons[i][j] != button) {
                            rotator = ObjectAnimator.ofFloat(buttons[i][j], "rotation", 360);
                            rotator.setDuration((int) (Math.random() * 500));
                            rotator.setRepeatCount(ObjectAnimator.INFINITE);
                            rotator.setInterpolator(new LinearInterpolator());
                            rotator.start();
                            ObjectAnimator oy = ObjectAnimator.ofFloat(buttons[i][j], "y", buttons[i][j].getY(), size.y);
                            oy.setDuration((int) (Math.random() * 5000));
                            oy.setInterpolator(new LinearInterpolator());
                            oy.start();
                        }
                    }
                }
            }
        }, 1500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                restartButton.callOnClick();
            }
        }, 5000);
        if (button != null) {
            ObjectAnimator oy = ObjectAnimator.ofFloat(button, "y", button.getY(), size.y);
            oy.setDuration((3000));
            oy.setInterpolator(new LinearInterpolator());
            oy.start();
        }
    }


    public void startToPlaceMines(boolean horizontal, boolean upToDown) {
        Log.d("GamePlay_UI_Enabler", "startToPlaceMines:");
        if (startingRowToFillMinesFromBottom == 0 && !initiated)
            startingRowToFillMinesFromBottom = buttons.length - 1;
        if (startingColToFillMinesFromTheRight == 0 && !initiated)
            startingColToFillMinesFromTheRight = buttons.length - 1;
        initiated = true;
        if (startingColToFillMines > startingColToFillMinesFromTheRight || startingRowToFillMines > startingRowToFillMinesFromBottom) {
            return;

        } else {
            if (horizontal) {
                if (upToDown) {//right to left
                    Snackbar.make(board,
                            "Placing mines from the right!!",
                            Snackbar.LENGTH_LONG).show();

                    for (int i = 0; i < buttons.length; i++) {
                        logic.placeMine(i, startingColToFillMinesFromTheRight);
                        updateBackGroundIconForButton(i, startingColToFillMinesFromTheRight, R.mipmap.hidden);
                    }
                    startingColToFillMinesFromTheRight--;
                } else {//left To right
                    Snackbar.make(board,
                            "Placing mines from the left!!",
                            Snackbar.LENGTH_LONG).show();
                    for (int i = 0; i < buttons.length; i++) {
                        logic.placeMine(i, startingColToFillMines);
                        updateBackGroundIconForButton(i, startingColToFillMines, R.mipmap.hidden);
                    }
                    startingColToFillMines++;
                }
            } else {
                if (upToDown) {
                    Snackbar.make(board,
                            "Placing mines from the top!!",
                            Snackbar.LENGTH_LONG).show();
                    for (int i = 0; i < buttons.length; i++) {
                        if (Math.random() * 2 % 2 == 0)
                            logic.placeMine(startingRowToFillMines, i);
                        updateBackGroundIconForButton(startingRowToFillMines, i, R.mipmap.hidden);
                    }
                    startingRowToFillMines++;
                } else {
                    Snackbar.make(board,
                            "Placing mines from the bottom!!",
                            Snackbar.LENGTH_LONG).show();
                    for (int i = 0; i < buttons.length; i++) {
                        logic.placeMine(startingRowToFillMinesFromBottom, i);
                        updateBackGroundIconForButton(startingRowToFillMinesFromBottom, i, R.mipmap.hidden);
                    }
                    startingRowToFillMinesFromBottom--;
                }
            }
        }
    }

    public void setIsPressed (int row,int col, boolean bool){
        buttons[row][col].setIsPressed(bool);
    }
    public boolean getIsPressed (int row,int col){
        return buttons[row][col].isPressed();
    }

    public void setTextForTimer(String textForTimer) {
        textView_timer.setText(textForTimer);
    }

    public void setRestartButton() {
        Log.d("GamePlay_UI_Enabler", "setRestartButton:");
        restartButton.setBackgroundResource(R.mipmap.restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid.removeAllViews();
                board.removeAllViews();
                activity.recreate();
            }
        });
    }

    public void completeZeros(int[][] matrix, int i, int j, MineSweeper_Logic logic) {
        Log.d("GamePlay_UI_Enabler", "completeZeros:");
        if (matrix[i][j] == logic.getMine() || matrix[i][j] > 0) {
            return;
        }
        if (i == 0 && j == 0 && matrix.length > 1) { // top left corner
            if (!buttons[i][j + 1].isPressed()) {
                (buttons[i][j + 1]).callOnClick();
            }
            if (!buttons[i + 1][j].isPressed()) {
                (buttons[i + 1][j]).callOnClick();
            }
            if (!buttons[i + 1][j + 1].isPressed()) {
                (buttons[i + 1][j + 1]).callOnClick();
            }
        }

        if (i == 0 && j > 0 && j < matrix.length - 1 && matrix.length > 1) {// top row not corners
            if (!buttons[i][j - 1].isPressed()) {
                buttons[i][j - 1].callOnClick();
            }
            if (!buttons[i + 1][j - 1].isPressed()) {
                buttons[i + 1][j - 1].callOnClick();
            }
            if (!buttons[i + 1][j].isPressed()) {
                buttons[i + 1][j].callOnClick();
            }
            if (!buttons[i + 1][j + 1].isPressed()) {
                buttons[i + 1][j + 1].callOnClick();
            }
            if (!buttons[i][j + 1].isPressed()) {
                buttons[i][j + 1].callOnClick();
            }
        }

        if (j == 0 && i > 0 && i < matrix.length - 1 && matrix.length > 1) {// left col not corners
            if (!buttons[i - 1][j].isPressed()) {
                buttons[i - 1][j].callOnClick();
            }
            if (!buttons[i - 1][j + 1].isPressed()) {
                buttons[i - 1][j + 1].callOnClick();
            }
            if (!buttons[i][j + 1].isPressed()) {
                buttons[i][j + 1].callOnClick();
            }
            if (!buttons[i + 1][j + 1].isPressed()) {
                buttons[i + 1][j + 1].callOnClick();
            }
            if (!buttons[i + 1][j].isPressed()) {
                buttons[i + 1][j].callOnClick();
            }
        }

        if (i == matrix.length - 1 && j == 0 && matrix.length > 1) {// bottom left corner
            if (!buttons[i - 1][j].isPressed()) {
                buttons[i - 1][j].callOnClick();
            }
            if (!buttons[i - 1][j + 1].isPressed()) {
                buttons[i - 1][j + 1].callOnClick();
            }
            if (!buttons[i][j + 1].isPressed()) {
                buttons[i][j + 1].callOnClick();
            }
        }
        if (j > 0 && j < matrix.length - 1 && i == matrix.length - 1 && matrix.length > 1) {// bottom row not corners
            if (!buttons[i - 1][j].isPressed()) {
                buttons[i - 1][j].callOnClick();
            }
            if (!buttons[i - 1][j + 1].isPressed()) {
                buttons[i - 1][j + 1].callOnClick();
            }
            if (!buttons[i][j + 1].isPressed()) {
                buttons[i][j + 1].callOnClick();
            }
            if (!buttons[i][j - 1].isPressed()) {
                buttons[i][j - 1].callOnClick();
            }
            if (!buttons[i - 1][j - 1].isPressed()) {
                buttons[i - 1][j - 1].callOnClick();
            }
        }
        if (i == matrix.length - 1 && j == matrix.length - 1 && matrix.length > 1) {// bottom right corner
            if (!buttons[i - 1][j].isPressed()) {
                buttons[i - 1][j].callOnClick();
            }
            if (!buttons[i - 1][j - 1].isPressed()) {
                buttons[i - 1][j - 1].callOnClick();
            }
            if (!buttons[i][j - 1].isPressed()) {
                buttons[i][j - 1].callOnClick();
            }
        }
        if (j == matrix[0].length - 1 && i != 0 && i < matrix.length - 1 && matrix.length > 1) {// right col not corners
            if (!buttons[i - 1][j].isPressed()) {
                buttons[i - 1][j].callOnClick();
            }
            if (!buttons[i - 1][j - 1].isPressed()) {
                buttons[i - 1][j - 1].callOnClick();
            }
            if (!buttons[i][j - 1].isPressed()) {
                buttons[i][j - 1].callOnClick();
            }
            if (!buttons[i + 1][j - 1].isPressed()) {
                buttons[i + 1][j - 1].callOnClick();
            }
            if (!buttons[i + 1][j].isPressed()) {
                buttons[i + 1][j].callOnClick();
            }
        }
        if (j == matrix.length - 1 && i == 0 && matrix.length > 1) {// top right corner
            if (!buttons[i][j - 1].isPressed()) {
                (buttons[i][j - 1]).callOnClick();
            }
            if (!buttons[i + 1][j].isPressed()) {
                (buttons[i + 1][j]).callOnClick();
            }
            if (!buttons[i + 1][j - 1].isPressed()) {
                (buttons[i + 1][j - 1]).callOnClick();
            }
        }

        if (i < matrix.length - 1 && j < matrix[0].length - 1 && i != 0 && j != 0 && matrix.length > 1) {//middle
            if (!buttons[i][j - 1].isPressed()) {
                (buttons[i][j - 1]).callOnClick();
            }
            if (!buttons[i + 1][j].isPressed()) {
                (buttons[i + 1][j]).callOnClick();
            }
            if (!buttons[i + 1][j - 1].isPressed()) {
                (buttons[i + 1][j - 1]).callOnClick();
            }
            if (!buttons[i + 1][j + 1].isPressed()) {
                (buttons[i + 1][j + 1]).callOnClick();
            }
            if (!buttons[i][j + 1].isPressed()) {
                (buttons[i][j + 1]).callOnClick();
            }
            if (!buttons[i - 1][j + 1].isPressed()) {
                (buttons[i - 1][j + 1]).callOnClick();
            }
            if (!buttons[i - 1][j + 1].isPressed()) {
                (buttons[i - 1][j + 1]).callOnClick();
            }
            if (!buttons[i - 1][j].isPressed()) {
                (buttons[i - 1][j]).callOnClick();
            }
            if (!buttons[i - 1][j - 1].isPressed()) {
                (buttons[i - 1][j - 1]).callOnClick();
            }
        }
    }
}
