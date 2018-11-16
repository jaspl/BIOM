package com.example.jan.recognitionusingsensordata.Controllers;

import android.os.CountDownTimer;

import com.example.jan.recognitionusingsensordata.MainActivity;

public class Timer {
    AccelerometrController accel;
    GravitySensorController gravitySensorControlle;
    private static  Timer instance;

    private Timer(){

    }
    public static Timer getInstance(){
        if (instance==null){
            instance= new Timer();
        }
        return instance;
    }
    CountDownTimer timer = new CountDownTimer(1000,10) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {

            stopTimer();
        }
    };
    public void startTimer(){
        timer.start();
    }
    public void getAccelAndGraavity(AccelerometrController accel,GravitySensorController gravitySensorController){
        this.gravitySensorControlle=gravitySensorController;
        this.accel=accel;
    }
    public void stopTimer(){
        if (gravitySensorControlle!=null)
        gravitySensorControlle.setWasSet(false);
        timer.cancel();
    }

}
