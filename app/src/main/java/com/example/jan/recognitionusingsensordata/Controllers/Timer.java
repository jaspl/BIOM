package com.example.jan.recognitionusingsensordata.Controllers;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.example.jan.recognitionusingsensordata.MainActivity;

public class Timer {

    private static Timer instance;
    TextView timerText;
    float count = 0;
    public boolean canMakeAnotherMovement = true;

    private Timer() {

    }

    public static Timer getInstance() {
        if (instance == null) {
            instance = new Timer();
        }
        return instance;
    }

    CountDownTimer timer = new CountDownTimer(1500, 10) {
        @Override
        public void onTick(long l) {
            if (timerText != null) {
                count += 0.01f;
                timerText.setText("0." + l);
            }
        }

        @Override
        public void onFinish() {
            stopTimer();
        }
    };

    public void startTimer() {
        if (canMakeAnotherMovement) {
            timer.start();
            canMakeAnotherMovement = false;
        }
    }

    public void getAccelAndGraavity(TextView timerText) {
        this.timerText = timerText;
    }

    public void stopTimer() {
        timer.cancel();
        canMakeAnotherMovement = true;
        timerText.setText("wykonaj kolejny ruch");
    }

}
