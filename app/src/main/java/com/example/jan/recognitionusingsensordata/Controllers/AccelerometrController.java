package com.example.jan.recognitionusingsensordata.Controllers;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.jan.recognitionusingsensordata.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class AccelerometrController {
    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;
    boolean farward = false;
    boolean backward = false;
    float valueOfBegin;
    float valueOfMaxFarward;
    float valueOfMaxBackward;
    float valueOfEnd;
    long lastOrderListUpdate;
    long lastUpdate;
    List<Integer> gestureOrder = new ArrayList<>();
    MotionWatcher watcher= MotionWatcher.getInstance();

    public void getAccelerometr(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float border = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        accelerometrGestureRekognision(x, y, z);
        getGesture(border);
    }

    private void getGesture(float accelerometrActivity) {
        if (accelerometrActivity < 0.2) {
            if (System.currentTimeMillis() - lastOrderListUpdate > 1000 && gestureOrder.size() > 0) {
                resetFlags();
                gestureOrder.clear();
                Log.d("resetinfo", "time has past table is reseted");
            }
           gestureRekognition();
        }
        resetGestureOsrer();
    }


    private void accelerometrGestureRekognision(float x, float y, float z) {
        float rightLeftBorer;
        float upDownBorder;
        float farwardBackwardBorder;
        float lowerBorder = 1;
        float higherBorder = 5;

        if (gestureOrder.size() > 0) {
            if (gestureOrder.get(0) == 1 || gestureOrder.get(0) == 2) {
                rightLeftBorer = lowerBorder;
                upDownBorder = higherBorder;
                farwardBackwardBorder = higherBorder;
            } else if (gestureOrder.get(0) == 3 || gestureOrder.get(0) == 4) {
                rightLeftBorer = higherBorder;
                upDownBorder = higherBorder;
                farwardBackwardBorder = lowerBorder;
            } else if (gestureOrder.get(0) == 6 || gestureOrder.get(0) == 5) {
                rightLeftBorer = higherBorder;
                upDownBorder = lowerBorder;
                farwardBackwardBorder = higherBorder;
            } else {
                rightLeftBorer = lowerBorder;
                upDownBorder = lowerBorder;
                farwardBackwardBorder = lowerBorder;
            }
        } else {
            rightLeftBorer = higherBorder;
            upDownBorder = higherBorder;
            farwardBackwardBorder = higherBorder;
        }
        if (abs(x) > abs(y) && abs(x) > abs(z)) {
            if (x > rightLeftBorer&&!right) {
                    right = true;
                    gestureOrder.add(1);
                    lastOrderListUpdate = System.currentTimeMillis();
            } else if (x < -rightLeftBorer&&!left) {
                    left = true;
                    gestureOrder.add(2);
                    lastOrderListUpdate = System.currentTimeMillis();
            } else {
                return;
            }
        } else if (abs(y) > abs(x) && abs(y) > abs(z)) {
            if (y > farwardBackwardBorder&&!farward) {
                    farward = true;
                    gestureOrder.add(3);
                    lastOrderListUpdate = System.currentTimeMillis();
            } else if (y < -farwardBackwardBorder&&!backward) {
                    backward = true;
                    gestureOrder.add(4);
                    lastOrderListUpdate = System.currentTimeMillis();
            } else {
                return;
            }
        } else if (abs(z) > abs(x) && abs(z) > abs(y)) {
            if (z > upDownBorder&&!up) {
                    up = true;
                    gestureOrder.add(5);
                    lastOrderListUpdate = System.currentTimeMillis();
            } else if (z < -upDownBorder&&!down) {
                    down = true;
                    gestureOrder.add(6);
                    lastOrderListUpdate = System.currentTimeMillis();
            } else {
                return;
            }
        }

    }
    private void gestureRekognition(){
        if (gestureOrder.size() >= 2) {
            if (System.currentTimeMillis() - lastUpdate > 1000) {
                lastUpdate = System.currentTimeMillis();
                if (gestureOrder.contains(5) && gestureOrder.contains(6)) {
                    if (gestureOrder.get(0) == 5) {
                        watcher.setActualMovement("upMovement");
                    } else if (gestureOrder.get(0) == 6) {
                        watcher.setActualMovement("downMovement");
                    }
                } else if (gestureOrder.contains(1) && gestureOrder.contains(2)) {
                    if (gestureOrder.get(0) == 1) {
                        watcher.setActualMovement("rightMovement");
                    } else if (gestureOrder.get(0) == 2) {
                        watcher.setActualMovement("leftMovement");
                    }
                } else if (gestureOrder.contains(3) && gestureOrder.contains(4)) {
                    if (gestureOrder.get(0) == 3) {
                        watcher.setActualMovement("farwardMovement");

                    } else if (gestureOrder.get(0) == 4) {
                        watcher.setActualMovement("backwardMovement");
                    }
                }
                resetFlags();
                resetValues();
                gestureOrder.clear();

            } else {
                resetFlags();
                resetValues();
                gestureOrder.clear();
            }
        }
    }

    private void resetGestureOsrer() {
        if (gestureOrder.size() > 2) {
            resetFlags();
            gestureOrder.clear();
        }
    }

    private void resetValues() {
        valueOfMaxBackward = 0;
        valueOfMaxFarward = 0;
        valueOfEnd = 0;
        valueOfBegin = 0;
    }

    private void resetFlags() {
        up = false;
        down = false;
        farward = false;
        backward = false;
        left = false;
        right = false;
    }
}
