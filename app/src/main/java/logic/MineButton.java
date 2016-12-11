package logic;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import activities.GamePlayActivity;
import ui_package.GamePlay_UI_Enabler;

/**
 * Created by אילון on 20/11/2016.
 */


public class MineButton extends Button {
    private int hiddenIconId;
    private int showIconId;
    private int row;
    private int col;
    private int[][] matrix = null;
    private final MineSweeper_Logic sweeper;
    private GamePlayActivity context;
    private final int flagId;
    private final int explodedId;
    private boolean pressed = false;
    private boolean longPressed = false;
    private boolean explodedMine = false;

    public MineButton(final GamePlayActivity context, final MineSweeper_Logic sweeper, int iconId, final int[][]matrix, final int row, final int col) {
        super(context);
        this.context = context;
        this.sweeper = sweeper;
        this.showIconId   = R.mipmap.hidden;
        this.hiddenIconId = iconId;
        explodedId = R.mipmap.explode;
        flagId = R.mipmap.flag;
        setBackgroundResource(showIconId);
        setVisibility(VISIBLE);
        this.row    = row;
        this.col    = col;
        this.matrix = matrix;

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
                        setBackgroundResource(showIconId);
                        longPressed = false;
                        pressed     = false;
                    }
                    else {
                        setBackgroundResource(flagId);
                        sweeper.changeNumOfMines(-1);
                        context.getUi().setNumOfMinesTextView();
                        longPressed = true;
                        pressed = true;
                    }
                    if (sweeper.checkWin()){
                        context.win();
                    }
                }
                return true;
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!longPressed) {
                    if (matrix[row][col] != sweeper.getMine()) {
                        setBackgroundResource(hiddenIconId);
                        pressed = true;
                        GamePlay_UI_Enabler d = new GamePlay_UI_Enabler(context,sweeper);
                        d.completeZeros(matrix, row, col, sweeper);
                    } else {
                        explodedMine = true;
                        GamePlay_UI_Enabler ui = new GamePlay_UI_Enabler(context,sweeper);
                        ui.showAllMatrix(sweeper.getButtons());
                        context.getGameTimer().getTimerTask().cancel();
                    }
                }
                else {
                    setBackgroundResource(showIconId);
                    sweeper.changeNumOfMines(1);
                    context.getUi().setNumOfMinesTextView();
                    longPressed = false;
                    pressed     = false;
                }

            }

        });
    }

    public void setHiddenIcon(){
        if (!explodedMine)
            setBackgroundResource(hiddenIconId);
        else
            setBackgroundResource(explodedId);
    }

    public boolean isPressed(){
        return pressed;
    }

    public boolean isLongPressed(){
        return longPressed;
    }

}
