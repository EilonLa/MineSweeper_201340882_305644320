package logic;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import activities.GamePlayActivity;

/**
 * Created by אילון on 26/11/2016.
 */

public class GameTimer implements Runnable {

    private static Timer timer;
    private static TimerTask timerTask;
    private GamePlayActivity activity;


    private static boolean paused = false;
    private static int rightSeconds = 0;
    private static int leftSeconds = 0;
    private static int rightMin = 0;
    private static int leftMin = 0;

    private static final Handler handler = new Handler();

    public GameTimer(GamePlayActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, 1000);

    }

    public void pause(boolean paused) {
        this.paused = paused;
    }

    public void initializeTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
            rightSeconds = 0;
            leftSeconds = 0;
            rightMin = 0;
            leftMin = 0;
        }
        timerTask = new TimerTask() {
            public void run() {
                if (!paused)
                    handler.post(new Runnable() {

                        public void run() {
                            rightSeconds++;

                            if (rightSeconds == 10) {
                                rightSeconds = 0;
                                leftSeconds++;
                            }
                            if (leftSeconds == 6) {
                                leftSeconds = 0;
                                rightMin++;
                            }
                            if (rightMin == 10) {
                                leftSeconds = 0;
                                leftMin++;
                            }
                            if (leftMin == 10) {
                                leftMin = 9;
                                leftSeconds = 5;
                                rightMin = 9;
                                rightSeconds = 9;
                            }
                            activity.getUi().setTextForTimer(leftMin + rightMin + " : " + leftSeconds + rightSeconds);

                        }

                    });

            }

        };

    }

    public String getTime() {
        return leftMin + rightMin + " : " + leftSeconds + rightSeconds;
    }

    public TimerTask getTimerTask() {
        return timerTask;
    }

    public int getScore() {
        return leftMin * 1000 + rightMin * 100 + leftSeconds * 10 + rightSeconds;
    }
}
