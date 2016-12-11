package ui_package;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import db.DBOperator;
import db.DataRow;
import activities.HighScores;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import java.util.ArrayList;

/**
 * Created by אילון on 01/12/2016.
 */

public class HighScores_UI_Enabler {
    private TextView fRowSCell;
    private TextView fRowTCell;
    private TextView sRowSCell;
    private TextView sRowTCell;
    private TextView tRowSCell;
    private TextView tRowTCell;
    private HighScores activity;
    private DBOperator dataBase;
    private DataRow dataRow;
    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;
    private Button back;
    private ArrayList<DataRow> topThreeRows ;

    public HighScores_UI_Enabler (final HighScores activity){
        this.activity = activity;
        this.easy = (CheckBox) activity.findViewById(R.id.checkbox_easy_scores);
        this.medium = (CheckBox) activity.findViewById(R.id.checkbox_medium_scores);
        this.hard = (CheckBox) activity.findViewById(R.id.checkbox_hard_scores);
        this.back =  (Button) activity.findViewById(R.id.btn_back);
        dataBase = new DBOperator(activity);


        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medium.setChecked(false);
                hard.setChecked(false);
                easy.setChecked(true);
                topThreeRows = dataBase.getTopThree(1);
                setTable(topThreeRows);
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setChecked(false);
                hard.setChecked(false);
                medium.setChecked(true);
                topThreeRows = dataBase.getTopThree(2);
                setTable(topThreeRows);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setChecked(false);
                medium.setChecked(false);
                hard.setChecked(true);
                topThreeRows = dataBase.getTopThree(3);
                setTable(topThreeRows);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        easy.callOnClick();
    }
    public void setTable(ArrayList<DataRow> rows){
        fRowSCell = (TextView) activity.findViewById(R.id.firstRowSecondCell);
        fRowSCell.setText("Player1 name");
        fRowTCell = (TextView) activity.findViewById(R.id.firstRowThirdCell);
        fRowTCell.setText("score1");
        sRowSCell = (TextView) activity.findViewById(R.id.secondRowSecondCell);
        sRowSCell.setText("Player2 name");
        sRowTCell = (TextView) activity.findViewById(R.id.secondRowthirdCell);
        sRowTCell.setText("score2");
        tRowSCell = (TextView) activity.findViewById(R.id.thirdRowSecondCell);
        tRowSCell.setText("Player3 name");
        tRowTCell = (TextView) activity.findViewById(R.id.thirdRowThirdCell);
        tRowTCell.setText("score3");


        int rowNum = 0;
        for (DataRow row : rows){
            if (rowNum == 0){
                fRowSCell.setText(row.getmName());
                fRowTCell.setText(String.valueOf(End_UI_Enabler.setScoreToShow(row.getScore())));
            }
            if (rowNum == 1){
                sRowSCell.setText(row.getmName());
                sRowTCell.setText(String.valueOf(End_UI_Enabler.setScoreToShow(row.getScore())));
            }
            if (rowNum == 2){
                tRowSCell.setText(row.getmName());
                tRowTCell.setText(String.valueOf(End_UI_Enabler.setScoreToShow(row.getScore())));
            }
            rowNum++;
        }
    }
}
