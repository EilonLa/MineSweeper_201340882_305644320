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
    private static Timer sensorTimer;
    private static TimerTask sensorTimerTask;
    private static int sensorTimeLeft = 0;
    private static int sensorTimeRight = 0;
    private static int sensorTimeFront = 0;
    private static int sensorTimeback = 0;
    private boolean rightIsTilted  = false;
    private boolean leftIsTilted = false;
    private boolean frontIsTilted  = false;
    private boolean backIsTilted = false;
    private static boolean paused = false;
    private static int rightSeconds = 0;
    private static int leftSeconds = 0;
    private static int rightMin = 0;
    private static int leftMin = 0;
    private static final Handler handler = new Handler();
    private static final Handler sensorHandler = new Handler();
    public GameTimer(GamePlayActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, 1000);
        sensorTimer = new Timer();
        initializeSensorTimer();
        sensorTimer.schedule(sensorTimerTask,5000,1000);
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

    public void initializeSensorTimer(){
        if (sensorTimerTask != null) {
            sensorTimerTask.cancel();
            sensorTimeLeft = 0;
            sensorTimeRight = 0;
            sensorTimeFront = 0;
            sensorTimeback = 0;
        }
        sensorTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!paused || !activity.getUi().isGameEnded()){
                    if (rightIsTilted)
                        sensorTimeRight++;
                    else
                        sensorTimeRight = 0;
                    if (leftIsTilted)
                        sensorTimeLeft++;
                    else
                        sensorTimeLeft = 0;
                    if (frontIsTilted)
                        sensorTimeFront++;
                    else
                        sensorTimeFront = 0;
                    if (backIsTilted)
                        sensorTimeback++;
                    else
                        sensorTimeback = 0;

                    if (sensorTimeRight > 4)
                        activity.getUi().startToPlaceMines(true,true);

                    if (sensorTimeLeft > 4)
                        activity.getUi().startToPlaceMines(true,false);

                    if (sensorTimeFront > 4)
                        activity.getUi().startToPlaceMines(false,false);
                    if (sensorTimeback > 4)
                        activity.getUi().startToPlaceMines(false,true);
                }
            }
        };
    }

    public void setLeftIsTilted(boolean leftIsTilted) {
        this.leftIsTilted = leftIsTilted;
        if (!leftIsTilted)
            sensorTimeLeft = 0;
    }

    public void setRightIsTilted(boolean rightIsTilted) {
        this.rightIsTilted = rightIsTilted;
        if (!rightIsTilted)
            sensorTimeRight = 0;
    }

    public void setFrontIsTilted(boolean frontIsTilted) {
        this.frontIsTilted = frontIsTilted;
        if (!frontIsTilted)
            sensorTimeFront = 0;
    }

    public void setBackIsTilted(boolean backIsTilted) {
        this.backIsTilted = backIsTilted;
        if (!backIsTilted)
            sensorTimeback = 0;
    }
}
