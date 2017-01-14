package activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import db.DataRow;
import ui_enablers.End_UI_Enabler;

/**
 * Created by אילון on 28/11/2016.
 */

public class EndActivity extends FragmentActivity {
    private final int defaultGameLevel = 2;
    private final int defaultScore = 1;

    private String name;
    private int level;
    private int score;
    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("EndActivity", "onCreate:");
        setContentView(R.layout.activity_win);
        this.name = getIntent().getStringExtra("player_name");
        this.level = getIntent().getIntExtra("Level", defaultGameLevel);
        this.score = getIntent().getIntExtra("Score", defaultScore);
        this.lastLocation = LoginActivity.lastLocation;
        insertIntoDataBase();

        DataRow row = LoginActivity.database.getHighScoreFromDB(level);
        if (row != null) {
            if (row.getScore() >= score) {//win
                new End_UI_Enabler(this, true, score);
            } else {//lose
                new End_UI_Enabler(this, false, score);
            }
        } else//first game on this level
            new End_UI_Enabler(this, true, score);
    }

    public void insertIntoDataBase(){
        Log.d("EndActivity", "insertIntoDataBase:");
        if (lastLocation != null) {
            LoginActivity.database.addRow(new DataRow(name, level, score, lastLocation.getLatitude(), lastLocation.getLongitude()));
            Log.d("EndActivity", "location is valid:");
        }
        else {
            LoginActivity.database.addRow(new DataRow(name, level, score, 0, 0));
            Log.d("EndActivity", "location is null:");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Log.d("endActivity", "onBackPressed:");
        super.onBackPressed();
        this.finish();
    }

    public String getPlayerName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
