package com.example.jan.recognitionusingsensordata.Controllers;

public class MotionWatcher {
    private MovementListener listener;
    String actualMovement;
    private static MotionWatcher instance;

    public static MotionWatcher getInstance(){
        if (instance==null){
            instance= new MotionWatcher();
        }
        return instance;
    }
    public String getActualMovement() {
        return actualMovement;
    }

    public void setActualMovement(String actualMovement) {
        this.actualMovement = actualMovement;
        if (listener!=null){
            listener.onMovementChanged(actualMovement);
        }
    }
    public void setMotionListener(MovementListener listener){
        this.listener = listener;
    }
}
