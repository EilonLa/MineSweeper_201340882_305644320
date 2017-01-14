package logic;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import activities.GamePlayActivity;
import ui_enablers.GamePlay_UI_Enabler;

/**
 * Created by eilon & dvir on 20/11/2016.
 */


public class MineButton extends Button {
    private final int flagId;
    private final int explodedId;
    private int hiddenIconId;
    private int showIconId;
    private int row;
    private int col;
    private int[][] matrix = null;
    private MineSweeper_Logic logic;
    private GamePlayActivity activity;
    private GamePlay_UI_Enabler ui;
    private boolean pressed = false;
    private boolean longPressed = false;
    private boolean explodedMine = false;

    public MineButton(final GamePlay_UI_Enabler ui, final GamePlayActivity activity, final MineSweeper_Logic logic, int iconId, final int[][]matrix, final int row, final int col) {
        super(activity);
        this.activity = activity;
        this.logic = logic;
        this.showIconId   = R.mipmap.hidden;
        this.hiddenIconId = iconId;
        explodedId = R.mipmap.explode;
        flagId = R.mipmap.flag;
        setIcon(showIconId);
        setVisibility(VISIBLE);
        this.row    = row;
        this.col    = col;
        this.matrix = matrix;
        this.ui = ui;
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getEventTime() - motionEvent.getDownTime() < 500)
                        callOnClick();
                    else
                        performLongClick();
                }
                return true;
            }
        });

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!pressed){
                    if (longPressed) {
                        setIcon(showIconId);
                        longPressed = false;
                        pressed     = false;
                    }
                    else {
                        setIcon(flagId);
                        logic.changeNumOfMines(-1);
                        ui.setNumOfMinesTextView();
                        longPressed = true;
                        pressed = true;
                    }
                    if (logic.checkWin()){
                        activity.win();
                    }
                }
                return true;
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!longPressed) {
                    if (matrix[row][col] != logic.getMine()) {
                        setIcon(hiddenIconId);
                        pressed = true;
                        ui.completeZeros(matrix, row, col, logic);
                    } else {
                        explodedMine = true;
                        ui.showAllMatrix();
                        ui.explodedMine(row,col);
                        activity.getGameTimer().getTimerTask().cancel();
                    }
                }
                else {
                    setIcon(showIconId);
                    logic.changeNumOfMines(1);
                    activity.getUi().setNumOfMinesTextView();
                    longPressed = false;
                    pressed     = false;
                }
            }

        });
    }

    public void setHiddenIcon(){
        if (!explodedMine)
            setIcon(hiddenIconId);
        else
            setIcon(explodedId);
    }

    public void setIcon(final int IconId){
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBackgroundResource(IconId);
            }
        });
    }

    public void setHiddenIconId(int id){
        this.hiddenIconId = id;
    }

    public boolean isPressed(){
        return pressed;
    }

    public void setIsPressed(boolean bool){
        this.pressed = bool;
    }

    public boolean isLongPressed(){
        return longPressed;
    }

}
