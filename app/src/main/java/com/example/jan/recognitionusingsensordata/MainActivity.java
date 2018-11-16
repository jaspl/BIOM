package com.example.jan.recognitionusingsensordata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jan.recognitionusingsensordata.Controllers.AccelerometrController;
import com.example.jan.recognitionusingsensordata.Controllers.GravitySensorController;
import com.example.jan.recognitionusingsensordata.Controllers.MotionWatcher;
import com.example.jan.recognitionusingsensordata.Controllers.MovementListener;
import com.example.jan.recognitionusingsensordata.Controllers.Timer;
import com.example.jan.recognitionusingsensordata.Controllers.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    AccelerometrController accelerometrController;
    GravitySensorController gravitySensorController;
    SensorManager sensorManager;
    Sensor accelerometr;
    Sensor magnetometr;
    Sensor lightSeosor;
    Sensor gravitySensor;
    Button button;
    EditText usernameEditText;
    public  String username;
    public  TextView usernameText;
    boolean newUser = false;
    public TextView textView;
     int count =0;
     Timer timer =Timer.getInstance();
    MotionWatcher watcher = MotionWatcher.getInstance();
    ArrayList<String>seqence =  new ArrayList();
    ArrayList<User>userList =  new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.movementSign);
        usernameText = findViewById(R.id.usernameText);
        button = findViewById(R.id.newUser);
        usernameEditText = findViewById(R.id.username);
        accelerometrController = new AccelerometrController();
        gravitySensorController = new GravitySensorController();
        timer.getAccelAndGraavity(accelerometrController,gravitySensorController);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initialazeSensors();
        button.setText("dodaj nowego użytkownika");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( newUser == false){

                    String sUsername = usernameEditText.getText().toString();
                    if (sUsername.matches("")) {
                        usernameEditText.setHint("podaj nazwe uzytkownika");
                    } else {
                        newUser = true;

                        button.setText("zakończ swoją sekwencje");

                    }
                }else if (newUser==true){
                    newUser=false;
                    username= String.valueOf(usernameEditText.getText());
                    User user= new User(username,seqence);
                    userList.add(user);
                    button.setText("dodaj nowego użytkownika");
                    textView.setText(String.valueOf(seqence)); //TODO add list to database sequence
                }

            }
        });

        watcher.setMotionListener(new MovementListener() {
            @Override
            public void onMovementChanged(String newMovement) {
                if (newUser){
                    count++;
                    textView.setText(newMovement+count);
                    timer.startTimer();
                    seqence.add(newMovement);
                }else {
                    //TODO make check user algoritm
                    textView.setText(newMovement);
                    timer.startTimer();
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            accelerometrController.getAccelerometr(sensorEvent);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravitySensorController.getGravityGesture(sensorEvent);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void initialazeSensors() {
        accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometr, SensorManager.SENSOR_DELAY_FASTEST);
        lightSeosor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSeosor, SensorManager.SENSOR_DELAY_FASTEST);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
        magnetometr = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magnetometr, SensorManager.SENSOR_DELAY_FASTEST);
    }


}
