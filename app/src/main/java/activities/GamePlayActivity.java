package activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import java.util.Arrays;

import logic.GameTimer;
import logic.MineSweeper_Logic;
import service.MyService;
import ui_enablers.GamePlay_UI_Enabler;

/**
 * Created by eilon & dvir on 26/11/2016.
 */

public class GamePlayActivity extends AppCompatActivity implements MyService.MyServiceListener {

    private static final String TAG = GamePlayActivity.class.getSimpleName();
    public static GameTimer gameTimer;
    private final int defualtGameLevel = 2;
    private MineSweeper_Logic logic;
    private int level;
    private GamePlay_UI_Enabler ui;
    private String playerName;
    private MyService myService;
    private ServiceConnection serviceConnection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GamePlayActivity", "onCreate:");
        setContentView(R.layout.activity_game);

        this.level = getIntent().getIntExtra("level", defualtGameLevel);
        this.playerName = getIntent().getStringExtra("name");
        this.logic = new MineSweeper_Logic(this, level, ui);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
                if (serviceBinder instanceof MyService.ServiceBinder) {
                    setService(((MyService.ServiceBinder) serviceBinder).getService());
                }
                Log.d(TAG, "onServiceConnected: " + name);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                setService(null);
                Log.d(TAG, "onServiceDisconnected: " + name);
            }
        };
        boolean bindingSucceeded = bindService(new Intent(this, MyService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: " + (bindingSucceeded ? "the binding succeeded..." : "the binding failed!"));
    }

    @Override
    public void onSensorEvent(float[] values) {
        Log.d(TAG, "onSensorEvent: " + Arrays.toString(values));
    }

    public void setService(MyService service) {
        if (service != null) {
            this.myService = service;
            service.setListener(this);
            service.startListening();
        } else {
            if (this.myService != null) {
                this.myService.setListener(null);
            }
            this.myService = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gameTimer.pause(true);
        if (myService != null) {
            myService.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameTimer.pause(false);
        if (myService != null) {
            myService.startListening();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("GamePlayActivity", "onBackPressed:");
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
        gameTimer.pause(true);
        this.finish();
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        switch (sensorEvent.sensor.getType()) {
//            //first get device orientation
//            case Sensor.TYPE_ACCELEROMETER:
//                float x = sensorEvent.values[0];
//                float y = sensorEvent.values[1];
//                float z = sensorEvent.values[2];
//
//                if (x > 5) {//right side of phone is lifting
//                    gameTimer.setRightIsTilted(true);
//                } else
//
//                    gameTimer.setRightIsTilted(false);
//                if (x < -5) {// left side of phone is lifting
//                    gameTimer.setLeftIsTilted(true);
//                } else
//                    gameTimer.setLeftIsTilted(false);
//                if (y > 5) {// farthest side of phone is lifting
//                    gameTimer.setFrontIsTilted(true);
//                } else
//                    gameTimer.setFrontIsTilted(false);
//                if (y < -5) {// near side of phone is lifting
//                    gameTimer.setBackIsTilted(true);
//                } else
//                    gameTimer.setBackIsTilted(false);
//        }
//    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }
}
