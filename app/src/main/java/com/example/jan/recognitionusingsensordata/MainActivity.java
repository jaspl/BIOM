package com.example.jan.recognitionusingsensordata;

import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jan.recognitionusingsensordata.Controllers.AccelerometrController;
import com.example.jan.recognitionusingsensordata.Controllers.DbAdapter;
import com.example.jan.recognitionusingsensordata.Controllers.GravitySensorController;
import com.example.jan.recognitionusingsensordata.Controllers.MotionWatcher;
import com.example.jan.recognitionusingsensordata.Controllers.MovementListener;
import com.example.jan.recognitionusingsensordata.Controllers.Timer;
import com.example.jan.recognitionusingsensordata.Controllers.User;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {

    TextView timerTextView;
    SensorManager sensorManager;
    EditText usernameEditText;
    public TextView usernameText,textView;
    boolean newUser = false;
    DbAdapter dbAdapter;
    Timer timer = Timer.getInstance();
    MotionWatcher watcher = MotionWatcher.getInstance();
    ArrayList<String> seqence = new ArrayList();
    ArrayList<String> checkSeqence = new ArrayList();
    ArrayList<User> userList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DbAdapter(this); // TODO this ll make new sqlite database
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.movementSign);
        usernameText = findViewById(R.id.usernameText);
        timerTextView = findViewById(R.id.timer);
        usernameEditText = findViewById(R.id.username);
        timer.getTimerTextView(timerTextView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        buttonOnClickSetter();
        getData();
        sensors();

        watcher.setMotionListener(new MovementListener() {
            @Override
            public void onMovementChanged(String newMovement) {
                Log.d("userListCount", String.valueOf(userList.size()));
                if (newUser) {
                    if (seqence.size() < 5) {
                        timer.startTimer();
                        seqence.add(newMovement);
                        textView.setText(newMovement + "\n" + seqence.size());
                    } else {
                        Toast.makeText(MainActivity.this,"You have made all 5 movements!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (checkSeqence.size() < 5) {
                        checkSeqence.add(newMovement);
                    } else if (checkSeqence.size() == 5) {
                        checkSeqence.remove(0);
                        checkSeqence.add(newMovement);
                    }
                    if (checkSeqence.size() == 5) {
                        analyze();
                    }
                    Log.d("", String.valueOf(checkSeqence));
                    textView.setText(newMovement);
                    timer.startTimer();
                }
            }
        });
    }

    /**
     * Method initialize sensors
     * @param sensorEventListener
     */

    private void initializeSensors(SensorEventListener sensorEventListener) {
        Sensor accelerometr, gravitySensor;
        accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener( sensorEventListener, accelerometr, SensorManager.SENSOR_DELAY_FASTEST);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Method deletes last movement made by user
     */
    public void deleteLastMovement() {
        if (newUser && seqence.size() > 1) {
            seqence.remove(seqence.size() - 1);
            textView.setText(seqence.get(seqence.size() - 1) + "\n" + seqence.size());
        }else if(newUser && seqence.size() == 1){
            seqence.remove(seqence.size() - 1);
            textView.setText("No movement!");
        }
    }

    /**
     * Method analyze sequence made by user and checks if it match to any of users
     */
    public void analyze() {
        String[] userData = listToTable(checkSeqence);
        for (int i = 0; i < userList.size(); i++) {
            Log.d("userSequence2", Arrays.toString(userList.get(i).sequence) + String.valueOf(userList.get(i).username));
            Log.d("chceckSequence", Arrays.toString(userData));
            if (Arrays.equals(userList.get(i).sequence, userData)) {
                usernameText.setText(userList.get(i).username);
                Log.d("username", userList.get(i).username);
            }
        }
    }

    /**
     * Method inserts user to database
     * @param seq
     * @param username
     */
    public void addUser(ArrayList<String> seq, String username) {
        String[] userData = listToTable(seq);
        boolean isInsert = dbAdapter.insertData(username, userData[0], userData[1], userData[2], userData[3], userData[4]);
        getData();
        Log.d("tab", Arrays.toString(userData));
    }

    /**
     * Method makes table from list
     * @param seq
     * @return
     */
    private String[] listToTable(ArrayList<String> seq) {
        String[] listData = new String[5];
        for (int i = 0; i < seq.size(); i++) {
            listData[i] = seq.get(i);
        }
        return listData;
    }

    /**
     * Method gets all data from database
     */
    private void getData() {

        Cursor res = dbAdapter.getData();
        if (res.getCount() == 0) {
            Log.d("show data", "nie ma danych");
        }
        userList.clear();
        while (res.moveToNext()) {
            String username = res.getString(0);
            String[] seqence = new String[5];
            for (int i = 1; i <= 5; i++) {
                seqence[i - 1] = res.getString(i);
            }
            userList.add(new User(username, seqence));

        }
        Log.d("Users in DB", String.valueOf(userList.size()));
    }
    public void sensors(){
        final AccelerometrController accelerometrController = new AccelerometrController();
        final GravitySensorController gravitySensorController = new GravitySensorController();
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                    accelerometrController.getAccelerometr(event, timer.canMakeAnotherMovement);
                } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                    gravitySensorController.getGravityGesture(event, timer.canMakeAnotherMovement);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        initializeSensors(sensorEventListener);

    }

    /**
     * Method sets all buttons functionality
     */
    private void buttonOnClickSetter(){
        final Button button, delete;
        button = findViewById(R.id.newUser);
        delete = findViewById(R.id.delete);
        button.setText("New user");


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastMovement();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!newUser) {
                    seqence.clear();
                    String sUsername = usernameEditText.getText().toString();
                    if (sUsername.matches("")) {
                        usernameEditText.setHint("You need to fill up username");
                        usernameEditText.setHintTextColor(Color.RED);
                    } else {
                        newUser = true;
                        button.setText("Finish your sequence");
                    }
                } else if (newUser && seqence.size() == 5) {
                    ArrayList<String> seq = seqence;
                    String username;
                    newUser = false;
                    username = String.valueOf(usernameEditText.getText());
                    addUser(seq,username);
                    Log.d("userListCount", String.valueOf(userList.size()));
                    button.setText("New user");
                    textView.setText(String.valueOf(seqence));
                } else {
                    Toast.makeText(MainActivity.this, "You need to make 5 movements", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

