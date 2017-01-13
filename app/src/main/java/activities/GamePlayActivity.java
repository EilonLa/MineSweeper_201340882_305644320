package activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import logic.Compass;
import logic.GameTimer;
import logic.MineSweeper_Logic;
import ui_enablers.GamePlay_UI_Enabler;

/**
 * Created by אילון on 26/11/2016.
 */

public class GamePlayActivity extends AppCompatActivity implements SensorEventListener {

    private final int defualtGameLevel = 2;
    private MineSweeper_Logic logic;
    private int level;
    private GameTimer gameTimer;
    private GamePlay_UI_Enabler ui;
    private String playerName;
    private Sensor sensor;
    private SensorManager sensorManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GamePlayActivity", "onCreate:");
        setContentView(R.layout.activity_game);
        this.level = getIntent().getIntExtra("level", defualtGameLevel);
        this.playerName = getIntent().getStringExtra("name");
        this.logic = new MineSweeper_Logic(this, level, ui);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        gameTimer.pause(true);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameTimer.pause(false);
        if (sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size() != 0) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void startGame() {
        Log.d("GamePlayActivity", "startGame:");
        gameTimer = new GameTimer(this);
        gameTimer.run();
        logic.createMatrix(level);
        this.ui = new GamePlay_UI_Enabler(this, logic);
        logic.setUi(ui);
        ui.createNewGrid(GamePlay_UI_Enabler.grid);
        ui.showStartGame();
        ui.setNumOfMinesTextView();
    }

    public GamePlay_UI_Enabler getUi() {
        return this.ui;
    }

    public MineSweeper_Logic getLogic() {
        return logic;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLevel() {
        return level;
    }

    public void win() {
        Log.d("GamePlayActivity", "win:");
        Intent i = new Intent(getApplicationContext(), EndActivity.class);
        i.putExtra("time", gameTimer.getTime());
        i.putExtra("Level", level);
        i.putExtra("Score", gameTimer.getScore());
        i.putExtra("player_name", playerName);
        startActivity(i);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            //first get device orientation
            case Sensor.TYPE_ACCELEROMETER:
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                if (x > 5) {//right side of phone is lifting
                    gameTimer.setRightIsTilted(true);
                } else

                    gameTimer.setRightIsTilted(false);
                if (x < -5) {// left side of phone is lifting
                    gameTimer.setLeftIsTilted(true);
                } else
                    gameTimer.setLeftIsTilted(false);
                if (y > 5) {// farthest side of phone is lifting
                    gameTimer.setFrontIsTilted(true);
                } else
                    gameTimer.setFrontIsTilted(false);
                if (y < -5) {// near side of phone is lifting
                    gameTimer.setBackIsTilted(true);
                } else
                    gameTimer.setBackIsTilted(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }
}
