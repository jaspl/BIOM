package com.example.jan.recognitionusingsensordata.Controllers;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.example.jan.recognitionusingsensordata.MainActivity;

/**
 * Singleton measures time between gestures
 */

public class Timer {

    private static Timer instance;
    TextView timerText;
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
                timerText.setText(l / 1000 + "." + l % 1000);
            }
        }

        @Override
        public void onFinish() {
            stopTimer();
        }
    };

    /**
     * Method starts timer
     */
    public void startTimer() {
        if (canMakeAnotherMovement) {
            timer.start();
            canMakeAnotherMovement = false;
        }
    }

    /**
     * Method gets TevtView
     * @param timerText
     */
    public void getTimerTextView(TextView timerText) {
        this.timerText = timerText;
    }

    /**
     * Method stops timer
     */
    public void stopTimer() {
        timer.cancel();
        canMakeAnotherMovement = true;
        timerText.setText("Make another move!");
    }

}
