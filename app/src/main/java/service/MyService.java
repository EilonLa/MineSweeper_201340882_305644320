package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import activities.GamePlayActivity;

/**
 * Created by אילון on 15/01/2017.
 */

public class MyService extends Service implements SensorEventListener {
    private ServiceBinder serviceBinder;
    private MyServiceListener listener;
    private SensorManager sensorManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        serviceBinder = new ServiceBinder();
        return serviceBinder;
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
                    GamePlayActivity.gameTimer.setRightIsTilted(true);
                } else

                    GamePlayActivity.gameTimer.setRightIsTilted(false);
                if (x < -5) {// left side of phone is lifting
                    GamePlayActivity.gameTimer.setLeftIsTilted(true);
                } else
                    GamePlayActivity.gameTimer.setLeftIsTilted(false);
                if (y > 5) {// farthest side of phone is lifting
                    GamePlayActivity.gameTimer.setFrontIsTilted(true);
                } else
                    GamePlayActivity.gameTimer.setFrontIsTilted(false);
                if (y < -5) {// near side of phone is lifting
                    GamePlayActivity.gameTimer.setBackIsTilted(true);
                } else
                    GamePlayActivity.gameTimer.setBackIsTilted(false);
        }
    }

    public void setListener(MyServiceListener listener) {
        this.listener = listener;
    }

    public void startListening() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface MyServiceListener {
        void onSensorEvent(float[] values);
    }

    public class ServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
