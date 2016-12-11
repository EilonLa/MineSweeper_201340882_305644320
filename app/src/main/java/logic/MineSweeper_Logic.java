package logic;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import android.net.Uri;
import android.widget.GridLayout;

import activities.GamePlayActivity;
import ui_package.GamePlay_UI_Enabler;

public class MineSweeper_Logic {
    private static final int MAX_MATRIX_SIZE = 10;
    private static final int MAX_NUM_OF_MINES = 10;
    private static final int MINE = -1;
    private int[][] matrix;
    private static int[] numbersIcons = {R.mipmap.zero,R.mipmap.one,R.mipmap.two,R.mipmap.three,R.mipmap.four,
            R.mipmap.five,R.mipmap.six, R.mipmap.seven,R.mipmap.eight};

    private static MineButton[][] buttons;
    private int level;
    private int numOfMines;
    private int startNumOfMines;

    private GamePlay_UI_Enabler ui;


    public MineSweeper_Logic(final GamePlayActivity activity, int level, GamePlay_UI_Enabler ui){
        this.level = level;
        createMatrix(level);
        this.ui = ui;
    }

    public int getLevel() {
        return this.level;
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public GridLayout createNewGrid(GridLayout grid, final GamePlayActivity context) {
        MineButton tempButton=null;
        buttons = new MineButton[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {

                if (matrix[i][j] == getMine())
                    tempButton = new MineButton(context,this, R.mipmap.mine, matrix, i, j);
                else
                    tempButton = new MineButton(context,this, numbersIcons[matrix[i][j]], matrix, i, j);

                buttons[i][j] = tempButton;//so we can press the zeros when a zero is clicked;
                ui.addButtonToGrid(grid,tempButton,i,j);
            }
        }
        return grid;
    }

    public void setUi (GamePlay_UI_Enabler ui){
        this.ui = ui;
    }

    public void createMatrix(int level) {
        int size;
        switch (level) {
            case 1:
                size = MAX_MATRIX_SIZE;
                numOfMines =  MAX_NUM_OF_MINES / 2;
                break;
            case 2:
                size       = MAX_MATRIX_SIZE;
                numOfMines = MAX_NUM_OF_MINES;
                break;
            case 3:
                size = MAX_MATRIX_SIZE / 2;
                numOfMines = MAX_NUM_OF_MINES;
                break;
            default:
                return;
        }

        startNumOfMines = numOfMines;
        matrix = initMat(matrix, size);
        int indexToPutMine1;
        int indexToPutMine2;
        for (int i = 0; i < numOfMines; i++) {
            do {
                indexToPutMine1 = (int) (Math.random() * (size));
                indexToPutMine2 = (int) (Math.random() * (size));
            } while (matrix[indexToPutMine1][indexToPutMine2] == MINE);

            matrix[indexToPutMine1][indexToPutMine2] = MINE;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0 && j == 0 && size > 1 && matrix[i][j] == MINE) { // top left corner
                    if (matrix[i + 1][j] != MINE)
                        matrix[i + 1][j]++;
                    if (matrix[i][j + 1] != MINE)
                        matrix[i][j + 1]++;
                    if (matrix[i + 1][j + 1] != MINE)
                        matrix[i + 1][j + 1]++;

                    continue;
                }
                if (i == size - 1 && j == 0 && size > 1 && matrix[i][j] == MINE) {// bottom left corner
                    if (matrix[i - 1][j] != MINE)
                        matrix[i - 1][j]++;
                    if (matrix[i - 1][j + 1] != MINE)
                        matrix[i - 1][j + 1]++;
                    if (matrix[i][j + 1] != MINE)
                        matrix[i][j + 1]++;
                    continue;

                }
                if (i == size - 1 && j == size - 1 && size > 1 && matrix[i][j] == MINE) {// bottom right corner
                    if (matrix[i][j - 1] != MINE)
                        matrix[i][j - 1]++;
                    if (matrix[i - 1][j - 1] != MINE)
                        matrix[i - 1][j - 1]++;
                    if (matrix[i - 1][j] != MINE)
                        matrix[i - 1][j]++;
                    continue;

                }
                if (j == size - 1 && i == 0 && size > 1 && matrix[i][j] == MINE) {// top right corner
                    if (matrix[i][j - 1] != MINE)
                        matrix[i][j - 1]++;
                    if (matrix[i + 1][j - 1] != MINE)
                        matrix[i + 1][j - 1]++;
                    if (matrix[i + 1][j] != MINE)
                        matrix[i + 1][j]++;
                    continue;

                }
                if (i == 0 && size > 1 && matrix[i][j] == MINE) {// top row not corners
                    if (matrix[i][j - 1] != MINE)
                        matrix[i][j - 1]++;
                    if (matrix[i + 1][j - 1] != MINE)
                        matrix[i + 1][j - 1]++;
                    if (matrix[i + 1][j] != MINE)
                        matrix[i + 1][j]++;
                    if (matrix[i + 1][j + 1] != MINE)
                        matrix[i + 1][j + 1]++;
                    if (matrix[i][j + 1] != MINE)
                        matrix[i][j + 1]++;
                    continue;

                }
                if (j == 0 && size > 1 && matrix[i][j] == MINE) {// left col not corners
                    if (matrix[i - 1][j] != MINE)
                        matrix[i - 1][j]++;
                    if (matrix[i - 1][j + 1] != MINE)
                        matrix[i - 1][j + 1]++;
                    if (matrix[i][j + 1] != MINE)
                        matrix[i][j + 1]++;
                    if (matrix[i + 1][j + 1] != MINE)
                        matrix[i + 1][j + 1]++;
                    if (matrix[i + 1][j] != MINE)
                        matrix[i + 1][j]++;
                    continue;

                }
                if (j > 0  && j < matrix.length-1 && i == matrix.length-1 && size > 1 && matrix[i][j] == MINE) {// bottom row not corners
                    if (matrix[i][j - 1] != MINE)
                        matrix[i][j - 1]++;
                    if (matrix[i - 1][j - 1] != MINE)
                        matrix[i - 1][j - 1]++;
                    if (matrix[i - 1][j] != MINE)
                        matrix[i - 1][j]++;
                    if (matrix[i - 1][j + 1] != MINE)
                        matrix[i - 1][j + 1]++;
                    if (matrix[i][j + 1] != MINE)
                        matrix[i][j + 1]++;
                    continue;

                }
                if (j == matrix[0].length - 1 && i != 0 && i < matrix.length - 1 && size > 1 && matrix[i][j] == MINE) {// right col not corners
                    if (matrix[i - 1][j] != MINE)
                        matrix[i - 1][j]++;
                    if (matrix[i - 1][j - 1] != MINE)
                        matrix[i - 1][j - 1]++;
                    if (matrix[i][j - 1] != MINE)
                        matrix[i][j - 1]++;
                    if (matrix[i + 1][j - 1] != MINE)
                        matrix[i + 1][j - 1]++;
                    if (matrix[i + 1][j] != MINE)
                        matrix[i + 1][j]++;
                    continue;

                }
                if (i < matrix.length - 1 && j < matrix[0].length && i != 0 && j != 0 && size > 1 && matrix[i][j] == MINE) {//middle
                    if (matrix[i - 1][j - 1] != MINE)
                        matrix[i - 1][j - 1]++;
                    if (matrix[i - 1][j] != MINE)
                        matrix[i - 1][j]++;
                    if (matrix[i - 1][j + 1] != MINE)
                        matrix[i - 1][j + 1]++;
                    if (matrix[i][j + 1] != MINE)
                        matrix[i][j + 1]++;
                    if (matrix[i + 1][j + 1] != MINE)
                        matrix[i + 1][j + 1]++;
                    if (matrix[i + 1][j] != MINE)
                        matrix[i + 1][j]++;
                    if (matrix[i + 1][j - 1] != MINE)
                        matrix[i + 1][j - 1]++;
                    if (matrix[i][j - 1] != MINE)
                        matrix[i][j - 1]++;
                    continue;

                }
            }
        }
    }

    public int[][] initMat ( int[][] mat, int size){
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

    public MineButton[][] getButtons(){
        return this.buttons;
    }

    public void changeNumOfMines(int num){
        this.numOfMines += num;
    }

    public int getNumOfMines(){
        return numOfMines;
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MineSweeper_Logic Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public boolean checkWin(){
        if (numOfMines > 0)
            return false;

        int minesMarked = 0;
        for (int i=0; i<buttons.length;i++){
            for (int j=0;j<buttons.length;j++){
                if (buttons[i][j].isLongPressed() && matrix[i][j] == MINE)
                    minesMarked++;
            }
        }
        if (minesMarked == startNumOfMines)
            return true;

        return false;
    }
}
