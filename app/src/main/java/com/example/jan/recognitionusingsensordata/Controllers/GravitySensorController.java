package com.example.jan.recognitionusingsensordata.Controllers;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.example.jan.recognitionusingsensordata.MainActivity;

public class GravitySensorController {
    String movementSign;
    MotionWatcher watcher=MotionWatcher.getInstance();

    public void setWasSet(boolean wasSet) {
        this.wasSet = wasSet;
    }

    boolean wasSet;


    public void getGravityGesture(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        detectPosition(x,y);
        Log.d("info",String.valueOf(wasSet));

    }
    public void detectPosition(float x,float y){

        if ((x>7||x<-7)&&!wasSet){
            wasSet = true;
            watcher.setActualMovement("HORIZONTAL");
        }else if ((y>7||y<-7)&&!wasSet){
            wasSet=true;
            watcher.setActualMovement("VERTICAL");
        }else {
            movementSign= null;

        }
    }


}
