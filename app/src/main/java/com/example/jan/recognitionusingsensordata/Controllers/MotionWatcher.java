package com.example.jan.recognitionusingsensordata.Controllers;

/**
 * Singleton Listener checking movement change
 */
public class MotionWatcher {
    private MovementListener listener;
    private static MotionWatcher instance;

    public static MotionWatcher getInstance(){
        if (instance==null){
            instance= new MotionWatcher();
        }
        return instance;
    }

    public void setActualMovement(String actualMovement) {
        if (listener!=null){
            listener.onMovementChanged(actualMovement);
        }
    }
    public void setMotionListener(MovementListener listener){
        this.listener = listener;
    }
}
