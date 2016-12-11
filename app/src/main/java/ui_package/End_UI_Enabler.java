package ui_package;

import android.widget.TableLayout;
import android.widget.TextView;

import db.DBOperator;
import db.DataRow;
import activities.EndActivity;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import java.util.ArrayList;

/**
 * Created by אילון on 30/11/2016.
 */

public class End_UI_Enabler {
    private TextView fRowSCell;
    private TextView fRowTCell;
    private TextView sRowSCell;
    private TextView sRowTCell;
    private TextView tRowSCell;
    private TextView tRowTCell;
    private TableLayout table;
    private int level;
    private String playerName;
    private TextView titledText;
    private TextView scoreText;
    private EndActivity activity;
    private DBOperator DB;
    private int score;
    public String scoreToShow;
    private boolean newHighScore;

    public End_UI_Enabler (final EndActivity activity, boolean newHighScore, int score){
        this.activity     = activity;
        table             = (TableLayout) activity.findViewById(R.id.scoreTable);
        this.level        = activity.getLevel();
        this.playerName   = activity.getPlayerName();
        this.scoreText    = (TextView) activity.findViewById(R.id.finalScore);
        DB                = new DBOperator(activity);
        this.newHighScore = newHighScore;
        this.score        = score;
        setScoreToShow(score);
        showScore();
        setTable();
        setTitle();
    }

    public void showScore (){
        if(newHighScore)
            scoreText.setText(playerName+" you made a new high score! your score is: " + setScoreToShow(score)+"!");

        else
            scoreText.setText(playerName+" you should try harder... your score is: " + setScoreToShow(score));
    }

    public static String setScoreToShow(int score) {
        int temp = score;
        StringBuilder stringBuilder = new StringBuilder("");
        if (score >= 1000) {
            temp = score;
            stringBuilder.append(temp/1000);
            stringBuilder.append(temp/100);
            stringBuilder.append(":");
            stringBuilder.append((int)(temp/10)%10);
            stringBuilder.append(temp&10);
        }
        if (score >=100 && score <1000){
            temp = score;
            stringBuilder.append('0');
            stringBuilder.append(temp/100);
            stringBuilder.append(":");
            stringBuilder.append((int)(temp/10)%10);
            stringBuilder.append(temp&10);
        }
        if (score <100 && score >=10){
            stringBuilder.append("00:"+score);
        }
        if (score < 10)
            stringBuilder.append("00:0"+score);

        return stringBuilder.toString();
    }

    public void setTable() {
        fRowSCell = (TextView) activity.findViewById(R.id.firstRowSecondCell_win);
        fRowTCell = (TextView) activity.findViewById(R.id.firstRowThirdCell_win);
        sRowSCell = (TextView) activity.findViewById(R.id.secondRowSecondCell_win);
        sRowTCell = (TextView) activity.findViewById(R.id.secondRowthirdCellCell_win);
        tRowSCell = (TextView) activity.findViewById(R.id.thirdRowSecondCell_win);
        tRowTCell = (TextView) activity.findViewById(R.id.thirdRowThirdCell_win);
        int rowNum = 0;
        ArrayList<DataRow> rows = DB.getTopThree(level);
        for (DataRow row : rows){
            if (rowNum == 0){
                fRowSCell.setText(row.getmName());
                fRowTCell.setText(String.valueOf(setScoreToShow(row.getScore())));
            }
            if (rowNum == 1){
                sRowSCell.setText(row.getmName());
                sRowTCell.setText(String.valueOf(setScoreToShow(row.getScore())));
            }
            if (rowNum == 2){
                tRowSCell.setText(row.getmName());
                tRowTCell.setText(String.valueOf(setScoreToShow(row.getScore())));
            }
            rowNum++;
        }

    }

    public void setTitle(){
        this.titledText = (TextView) activity.findViewById(R.id.endTitle);
        titledText.setText("Win! all mines cleared");
    }
}
