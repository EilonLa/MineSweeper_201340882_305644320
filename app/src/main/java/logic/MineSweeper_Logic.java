package logic;

import android.util.Log;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import activities.GamePlayActivity;
import ui_enablers.GamePlay_UI_Enabler;

public class MineSweeper_Logic {
    private static final int MAX_MATRIX_SIZE = 10;
    private static final int MAX_NUM_OF_MINES = 10;
    private static final int MINE = -1;
    private int[][] intMatrix;
    private int level;
    private int numOfMines;
    private int startNumOfMines;
    private GamePlayActivity activity;
    private GamePlay_UI_Enabler ui;

    public MineSweeper_Logic(final GamePlayActivity activity, final int level, GamePlay_UI_Enabler ui) {
        this.activity = activity;
        this.level = level;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createMatrix(level);
            }
        });
        this.ui = ui;
    }

    public void placeMine(int row, int col) {
        Log.i("Logic ", " placeMine");
        int temp = (int) (Math.random() * 2);
        if (ui == null) {
            intMatrix[row][col] = MINE;
            updateAroundTheButton(row, col);
            changeNumOfMines(1);
        } else {
            ui.setIsPressed(row,col,false);
            if (temp == 1) {
                intMatrix[row][col] = MINE;
                updateAroundTheButton(row, col);
                changeNumOfMines(1);
                ui.setNumOfMinesTextView();
                ui.updateHiddenIconIdForButton(row, col, R.mipmap.mine);
                ui.updateBackGroundIconForButton(row, col, R.mipmap.hidden);
            }
            ui.updateBackGroundIconForButton(row, col, R.mipmap.hidden);
        }
    }


    public void updateAroundTheButton(int row, int col) {
        if (row != 0 && col != 0 && intMatrix[row - 1][col - 1] != MINE) {
            intMatrix[row - 1][col - 1]++;
            if (ui != null) {
                ui.updateHiddenIconIdForButton(row - 1, col - 1, ui.getNumbersIcons()[intMatrix[row - 1][col - 1]]);
                if (ui.getIsPressed(row - 1, col - 1))
                    ui.updateBackGroundIconForButton(row - 1, col - 1, ui.getNumbersIcons()[intMatrix[row - 1][col - 1]]);
            }
        }
        if (row != 0 && intMatrix[row - 1][col] != MINE) {
            intMatrix[row - 1][col]++;
            if (ui != null && row > 0 ) {
                ui.updateHiddenIconIdForButton(row - 1, col, ui.getNumbersIcons()[intMatrix[row - 1][col]]);
                if (ui.getIsPressed(row - 1, col))
                    ui.updateBackGroundIconForButton(row - 1, col, ui.getNumbersIcons()[intMatrix[row - 1][col]]);
            }
        }
        if (col != intMatrix.length - 1 && row != 0 && intMatrix[row - 1][col + 1] != MINE) {
            intMatrix[row - 1][col + 1]++;
            if (ui != null && intMatrix[row - 1][col + 1] < ui.getNumbersIcons().length) {
                ui.updateHiddenIconIdForButton(row - 1, col + 1, ui.getNumbersIcons()[intMatrix[row - 1][col + 1]]);
                if (ui.getIsPressed(row - 1, col + 1))
                    ui.updateBackGroundIconForButton(row - 1, col + 1, ui.getNumbersIcons()[intMatrix[row - 1][col + 1]]);
            }
        }
        if (col != intMatrix.length - 1 && intMatrix[row][col + 1] != MINE) {
            intMatrix[row][col + 1]++;
            if (ui != null && intMatrix[row][col + 1] < ui.getNumbersIcons().length) {
                ui.updateHiddenIconIdForButton(row, col + 1, ui.getNumbersIcons()[intMatrix[row][col + 1]]);
                if (ui.getIsPressed(row, col + 1))
                    ui.updateBackGroundIconForButton(row, col + 1, ui.getNumbersIcons()[intMatrix[row][col + 1]]);
            }
        }
        if (row != intMatrix.length - 1 && intMatrix[row + 1][col] != MINE) {
            intMatrix[row + 1][col]++;
            if (ui != null && intMatrix[row + 1][col] < ui.getNumbersIcons().length) {
                ui.updateHiddenIconIdForButton(row + 1, col, ui.getNumbersIcons()[intMatrix[row + 1][col]]);
                if (ui.getIsPressed(row + 1, col))
                    ui.updateBackGroundIconForButton(row + 1, col, ui.getNumbersIcons()[intMatrix[row + 1][col]]);
            }
        }
        if (col != 0 && row != intMatrix.length - 1 && intMatrix[row + 1][col - 1] != MINE) {
            intMatrix[row + 1][col - 1]++;
            if (ui != null && intMatrix[row + 1][col - 1] < ui.getNumbersIcons().length ) {
                ui.updateHiddenIconIdForButton(row + 1, col - 1, ui.getNumbersIcons()[intMatrix[row + 1][col - 1]]);
                if (ui.getIsPressed(row + 1, col - 1))
                    ui.updateBackGroundIconForButton(row + 1, col - 1, ui.getNumbersIcons()[intMatrix[row + 1][col - 1]]);
            }
        }
        if (col != intMatrix.length - 1 && row != intMatrix.length - 1 && intMatrix[row + 1][col + 1] != MINE) {
            intMatrix[row + 1][col + 1]++;
            if (ui != null && intMatrix[row + 1][col + 1] < ui.getNumbersIcons().length && intMatrix[row + 1][col + 1] < ui.getNumbersIcons().length) {
                ui.updateHiddenIconIdForButton(row + 1, col + 1, ui.getNumbersIcons()[intMatrix[row + 1][col + 1]]);
                if (ui.getIsPressed(row + 1, col + 1))
                    ui.updateBackGroundIconForButton(row + 1, col + 1, ui.getNumbersIcons()[intMatrix[row + 1][col + 1]]);
            }
        }
        if (col != 0 && intMatrix[row][col - 1] != MINE) {
            intMatrix[row][col - 1]++;
            if (ui != null) {
                ui.updateHiddenIconIdForButton(row, col - 1, ui.getNumbersIcons()[intMatrix[row][col - 1]]);
                if (ui.getIsPressed(row, col - 1))
                    ui.updateBackGroundIconForButton(row, col - 1, ui.getNumbersIcons()[intMatrix[row][col - 1]]);
            }
        }
    }

    public int getLevel() {
        return this.level;
    }

    public int[][] getMatrix() {
        return this.intMatrix;
    }

    public void setUi(GamePlay_UI_Enabler ui) {
        this.ui = ui;
    }

    public void createMatrix(int level) {
        int size;
        int tempNumOfMines;
        numOfMines = 0;
        switch (level) {
            case 1:
                size = MAX_MATRIX_SIZE;
                tempNumOfMines = MAX_NUM_OF_MINES / 2;
                break;
            case 2:
                size = MAX_MATRIX_SIZE;
                tempNumOfMines = MAX_NUM_OF_MINES;
                break;
            case 3:
                size = MAX_MATRIX_SIZE / 2;
                tempNumOfMines = MAX_NUM_OF_MINES;
                break;
            default:
                return;
        }
        startNumOfMines = tempNumOfMines;
        intMatrix = initMat(intMatrix, size);//places zeroes
        int indexToPutMine1;
        int indexToPutMine2;
        for (int i = 0; i < tempNumOfMines; i++) {
            do {
                indexToPutMine1 = (int) (Math.random() * (size));
                indexToPutMine2 = (int) (Math.random() * (size));
            } while (intMatrix[indexToPutMine1][indexToPutMine2] == MINE);

            placeMine(indexToPutMine1, indexToPutMine2);
        }
    }

    public int[][] initMat(int[][] mat, int size) {
        mat = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mat[i][j] = 0;
            }
        }
        return mat;
    }

    public int getMine() {
        return MINE;
    }

    public void changeNumOfMines(int num) {
        this.numOfMines += num;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public int countFinalNumOfMines() {
        int counter = 0;
        for (int i = 0; i < intMatrix.length; i++) {
            for (int j = 0; j < intMatrix.length; j++) {
                if (intMatrix[i][j] == MINE)
                    counter++;
            }
        }
        return counter;
    }

    public boolean checkWin() {
        if (numOfMines > 0)
            return false;

        int minesMarked = 0;
        int totalNumOfMines = countFinalNumOfMines();
        for (int i = 0; i < GamePlay_UI_Enabler.getButtons().length; i++) {
            for (int j = 0; j < GamePlay_UI_Enabler.getButtons().length; j++) {
                if (GamePlay_UI_Enabler.getButtons()[i][j].isLongPressed() && intMatrix[i][j] == MINE)
                    minesMarked++;
            }
        }
        if (minesMarked == totalNumOfMines)
            return true;

        return false;
    }
}
