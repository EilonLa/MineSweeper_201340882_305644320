package activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import db.DBOperator;
import logic.GameTimer;
import logic.MineSweeper_Logic;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import ui_package.GamePlay_UI_Enabler;

/**
 * Created by אילון on 26/11/2016.
 */

public class GamePlayActivity extends AppCompatActivity  {

    private MineSweeper_Logic gameLogic;
    private int level;
    private GameTimer gameTimer;
    private GamePlay_UI_Enabler ui;
    private String playerName;

    private static final Handler handler = new Handler();

    private DBOperator db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GamePlayActivity", "onCreate:");
        setContentView(R.layout.game);
        db = new DBOperator(getApplicationContext());
        this.level      = getIntent().getIntExtra("level",2);
        this.playerName = getIntent().getStringExtra("name");
        this.gameLogic  = new MineSweeper_Logic(this,level,ui);
        this.ui         = new GamePlay_UI_Enabler(this,gameLogic);
        gameLogic.setUi(ui);
        startGame();
    }

    @Override
    public void onPause(){
        super.onPause();
        gameTimer.pause(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        gameTimer.pause(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void startGame (){
        gameTimer = new GameTimer(this);
        gameTimer.run();
        gameLogic.createMatrix(level);
        ui.showStartGame();
        ui.setRestartButton();
        ui.setNumOfMinesTextView();
    }

    public GamePlay_UI_Enabler getUi(){
        return this.ui;
    }

    public Handler getHandler(){
        return handler;
    }

    public GameTimer getGameTimer(){
        return gameTimer;
    }

    public MineSweeper_Logic getGameLogic(){
        return gameLogic;
    }

    public String getPlayerName(){return playerName;}

    public int getLevel(){return level;}

    public void win(){
        SQLiteDatabase sqLiteOpenHelper = db.getWritableDatabase();
        Intent i = new Intent (getApplicationContext(), EndActivity.class);
        i.putExtra("time", gameTimer.getTime());
        i.putExtra("Level", level);
        i.putExtra("Score",gameTimer.getScore());
        i.putExtra("player_name",playerName);
        startActivity(i);
    }
}
