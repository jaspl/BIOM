package com.example.jan.recognitionusingsensordata.Controllers;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.example.jan.recognitionusingsensordata.MainActivity;

public class GravitySensorController {
    String movementSign;
    MotionWatcher watcher=MotionWatcher.getInstance();
    boolean wasSet1;


    public void getGravityGesture(SensorEvent event, boolean canMakeAnotherMovement) {
        float x = event.values[0];
        float y = event.values[1];
        if (canMakeAnotherMovement) {
            detectPosition(x, y);
        }
    }

    public void detectPosition(float x,float y){

        if ((x>7||x<-7)&&wasSet1){
            wasSet1 = false;
            watcher.setActualMovement("HORIZONTAL");
        }
        if ((y>7||y<-7)&&wasSet1){
            wasSet1=false;
            watcher.setActualMovement("VERTICAL");
        }
        if((x<7&&x>-7)&&(y<7&&y>-7))  {
            movementSign= null;
            wasSet1 = true;

        }
    }


}
