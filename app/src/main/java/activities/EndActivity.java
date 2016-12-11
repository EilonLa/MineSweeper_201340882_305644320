package activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import db.DBOperator;
import db.DataRow;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import ui_package.End_UI_Enabler;

/**
 * Created by אילון on 28/11/2016.
 */

public class EndActivity extends AppCompatActivity {
    private DataRow newDr;
    private DBOperator dataBase;
    private String name;
    private int level;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);
        dataBase = new DBOperator(getApplicationContext());
        this.name = getIntent().getStringExtra("player_name");
        this.level= getIntent().getIntExtra("Level" ,2);
        this.score = getIntent().getIntExtra("Score" ,1);
        insertIntoDataBase();
        checkHighScore();
    }

    public void insertIntoDataBase() {
        DataRow lastGame = dataBase.getLastGameFromDB();
        newDr = new DataRow(lastGame );
        if (newDr.getLevel() == 0){//first game on this level
            newDr.setmName(name);
            newDr.setScore(score);
            newDr.setLevel(level);
        }

        dataBase.addRow(newDr);
    }
    public void checkHighScore(){
        DataRow row = dataBase.getHighScoreFromDB(level);
        if (row != null && row.getLevel() != 0){
            if (row.getScore() > score)
                new End_UI_Enabler(this, true,score);
            else
                new End_UI_Enabler(this, false,score);
        }
        else
            new End_UI_Enabler(this, true,score);
    }

    public String getPlayerName(){return name;}

    public int getLevel(){return level;}
}
