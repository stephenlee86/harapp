package org.tensorflow.lite.examples.transfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.tensorflow.lite.examples.transfer.api.TransferLearningModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.os.Vibrator;
import android.widget.Toast;
import android.util.Log;

import android.graphics.*;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener  {
  static final String log_tag = "MainActivityClass";
  static int log_count = 1;
  SensorData sensorData;
  ActivityRecognizer activityRecognizer;
  Classification classA = new Classification ("class A", 0);
  Classification classB = new Classification ("class B", 0);

  SensorManager mSensorManager;
  Sensor mAccelerometer;
  Sensor mGyroscope;

  Button viewprivacyButton;
  Button editprivacyButton;
  Button startButton;
  Button stopButton;
  Button displayGraphButton;
  TextView classATextView;
  TextView classBTextView;
  TextView classAInstanceCountTextView;
  TextView classBInstanceCountTextView;
  TextView lossValueTextView;
  Spinner optionSpinner;
  Spinner classSpinner;
  Vibrator vibrator;
  Intent intent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(log_tag + log_count, "executing onCreate method");
    log_count += 1;
    super.onCreate(savedInstanceState);
    activityRecognizer = new ActivityRecognizer ("ar_model", 0.75);
    setContentView(R.layout.activity_main);


    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    intent = new Intent (MainActivity.this, GraphView.class);
    startButton = (Button) findViewById(R.id.buttonStart);
    stopButton = (Button) findViewById(R.id.buttonStop);
    viewprivacyButton = (Button) findViewById(R.id.viewPrivacyConcerns);
    editprivacyButton = (Button) findViewById(R.id.editPrivacyData);
    displayGraphButton = (Button) findViewById(R.id.buttonGraph);
    stopButton.setEnabled(false);

    SharedPreferences settings = getSharedPreferences("DataPreferences", 0);
    String age_data = settings.getString("AgePreference", "DefaultValueIfNotExists");
    String gender_data = settings.getString("GenderPreference", "DefaultValueIfNotExists");
    String height_data = settings.getString("HeightPreference", "DefaultValueIfNotExists");
    String weight_data = settings.getString("WeightPreference", "DefaultValueIfNotExists");



    classATextView = (TextView)findViewById(R.id.classAOutputValueTextView);
    classBTextView = (TextView)findViewById(R.id.classBOutputValueTextView);
    classAInstanceCountTextView = (TextView)findViewById(R.id.classACountValueTextView);
    classBInstanceCountTextView = (TextView)findViewById(R.id.classBCountValueTextView);
    lossValueTextView = (TextView)findViewById(R.id.lossValueTextView);

    optionSpinner = (Spinner) findViewById(R.id.optionSpinner);
    classSpinner = (Spinner) findViewById(R.id.classSpinner);

    ArrayAdapter<CharSequence> optionAdapter = ArrayAdapter
            .createFromResource(this, R.array.options_array,
                    R.layout.spinner_item);
    optionAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    optionSpinner.setAdapter(optionAdapter);

    ArrayAdapter<CharSequence> classAdapter = ArrayAdapter
            .createFromResource(this, R.array.class_array,
                    R.layout.spinner_item);
    classAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    classSpinner.setAdapter(classAdapter);

    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    mSensorManager.registerListener(this, mAccelerometer, activityRecognizer.getSensorEDT ());
    mSensorManager.registerListener(this, mGyroscope, activityRecognizer.getSensorEDT ());
    try{
      activityRecognizer.setTLMW (null); //new TransferLearningModelWrapper(getApplicationContext());
    } catch (IllegalStateException e) {
      Log.e (e.toString (), e.getMessage ());
    }

    ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
    sensorData = app.loadSensorData("sensor.dat");

    optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view,
                                 int position, long id) {
        String option = (String) parent.getItemAtPosition(position);
        Log.d(log_tag + log_count, "executing onItemSelected method on " + option);
        log_count += 1;
        switch (option){
          case "Data Collection":
            activityRecognizer.setMode (Mode.Data_Collection);
            break;
          case "Training":
            activityRecognizer.setMode (Mode.Training);
            break;
          case "Inference":
            activityRecognizer.setMode (Mode.Inference);
            break;
          default:
            throw new IllegalArgumentException("Invalid app mode.");
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        Log.d(log_tag + log_count, "executing onNothingSelected method");
        log_count += 1;
        // TODO Auto-generated method stub
      }
    });

    classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view,
                                 int position, long id) {
        activityRecognizer.setClassID ((String) parent.getItemAtPosition(position));
        Log.d(log_tag + log_count, "executing onItemSelected method with class ID of " + activityRecognizer.getClassID () + " assigned");
        log_count += 1;
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        Log.d(log_tag + log_count, "executing onNothingSelected method with no class ID assigned");
        log_count += 1;
        // TODO Auto-generated method stub
      }
    });

    viewprivacyButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopUp popUpClass = new PopUp();
        popUpClass.showPopupWindow(view);
      }

    });


    editprivacyButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(),ChangePrivacy.class);
        startActivity(i);
      }
    });

    startButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for start of collection");
        log_count += 1;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        optionSpinner.setEnabled(false);
        activityRecognizer.setIsRunning (true);
        // Uncomment following lines to load an existing model.
       /*     if(activityRecognizer.getMode () == Mode.Inference){
              File modelPath = getApplicationContext().getFilesDir();
              File modelFile = new File(modelPath, activityRecognizer.getModelName ());
              if(modelFile.exists()){
                activityRecognizer.getTLMW ().loadModel(modelFile);
                Toast.makeText(getApplicationContext(), "Model loaded.", Toast.LENGTH_SHORT).show();
              }
            }
       */
      }
    });

    stopButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for end of collection");
        log_count += 1;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        optionSpinner.setEnabled(true);
        activityRecognizer.setIsRunning (false);
        if(activityRecognizer.getMode () == Mode.Training){
          if (activityRecognizer.getTLMW () != null) {
            activityRecognizer.getTLMW ().disableTraining();
          }
          // Uncomment following lines to save the model.
          /*
            File modelPath = getApplicationContext().getFilesDir();
            File modelFile = new File(modelPath, activityRecognizer.getModelName ());
            activityRecognizer.getTLMW ().saveModel(modelFile);
            Toast.makeText(getApplicationContext(), "Model saved.", Toast.LENGTH_SHORT).show();
           */
        }
      }
    });

    displayGraphButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for display of graph");
        log_count += 1;
        displayGraphButton.setEnabled(false);
        activityRecognizer.setIsGraphing (true);
      }
    });
  }

  protected void onPause() {
    Log.d (log_tag + log_count, "executing onPause method");
    log_count += 1;
    super.onPause();
    mSensorManager.unregisterListener(this);
  }

  protected void onResume() {
    Log.d (log_tag + log_count, "executing onResume method");
    log_count += 1;
    super.onResume();
    mSensorManager.registerListener(this, mAccelerometer, activityRecognizer.getSensorEDT ());
    mSensorManager.registerListener(this, mGyroscope, activityRecognizer.getSensorEDT ());
  }

  protected void onDestroy() {
    Log.d (log_tag + log_count, "executing onDestroy method");
    log_count += 1;
    super.onDestroy();
    if (activityRecognizer.getTLMW () != null) {
      activityRecognizer.getTLMW ().close();
    }
    activityRecognizer.setTLMW (null);
    mSensorManager = null;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
// commented out these logs for now so that the log does not get junked up
//    Log.d (log_tag + log_count, "executing onSensorChanged method");
//    log_count += 1;
    switch (event.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER:
        sensorData.addAccelEvent (event.values[0], event.values[1], event.values[2], event.timestamp);
        break;
      case Sensor.TYPE_GYROSCOPE:
        sensorData.addGyroEvent (event.values[0], event.values[1], event.values[2], event.timestamp);
        break;
    }

    //Check if we have desired number of samples for sensors, if yes, the process input.
    if(sensorData.checkSampleCount ()) {
      if (activityRecognizer.getIsGraphing ()) {
        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        app.saveSensorData(sensorData, "sensor.dat");
        startActivity (intent);
// leaving out the finish call for now just in case calling finish is not necessary here and just in case calling finish loses the current state of the main activity
//        finish ();
      }
      processInput();
      activityRecognizer.setIsGraphing (false);
      displayGraphButton.setEnabled(true);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {
    Log.d (log_tag + log_count, "executing onAccuracyChanged method");
    log_count += 1;

  }

  private void processInput()
  {
      Log.d (log_tag + log_count, "executing processInput method");
      log_count += 1;
      sensorData.populateInputSignal ();

      float[] input = toFloatArray(sensorData.getInputSignal ());

      if (activityRecognizer.getIsRunning ()){
        if(activityRecognizer.getMode () == Mode.Training){
          int batchSize = 0;
          if (activityRecognizer.getTLMW () != null) {
            batchSize = activityRecognizer.getTLMW ().getTrainBatchSize();
          }
          if(classA.getInstanceCount () >= batchSize && classB.getInstanceCount () >= batchSize){
            if (activityRecognizer.getTLMW () != null) {
              activityRecognizer.getTLMW ().enableTraining((epoch, loss) -> runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  Log.d(log_tag + log_count, "executing run method");
                  log_count += 1;
                  lossValueTextView.setText(Float.toString(loss));
                }
              }));
            }
          }
          else{
            String message = batchSize + " instances per class are required for training.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            stopButton.callOnClick();
          }

        }
        else if (activityRecognizer.getMode () == Mode.Data_Collection){
          if (activityRecognizer.getTLMW () != null) {
            activityRecognizer.getTLMW ().addSample(input, activityRecognizer.getClassID ());
          }
          if (activityRecognizer.getClassID ().equals(classA.getName ())) classA.incrementInstanceCount (1);
          else if(activityRecognizer.getClassID ().equals(classB.getName ())) classB.incrementInstanceCount (1);

          classAInstanceCountTextView.setText(Integer.toString(classA.getInstanceCount ()));
          classBInstanceCountTextView.setText(Integer.toString(classB.getInstanceCount ()));
        }
        else if (activityRecognizer.getMode () == Mode.Inference) {
          TransferLearningModel.Prediction[] predictions = null;
          if (activityRecognizer.getTLMW () != null) {
            predictions = activityRecognizer.getTLMW ().predict(input);
          }
          // Vibrate the phone if Class B is detected.
          if(predictions[1].getConfidence() > activityRecognizer.getThreshold ())
            vibrator.vibrate(VibrationEffect.createOneShot(200,
                    VibrationEffect.DEFAULT_AMPLITUDE));

          String classAOutput = Float.toString(round(predictions[0].getConfidence(), 4));
          String classBOutput = Float.toString(round(predictions[1].getConfidence(), 4));

          classATextView.setText(classAOutput);
          classBTextView.setText(classBOutput);
        }
      }

      // Clear all the values
      sensorData.clear ();
  }

  private float[] toFloatArray(List<Float> list)
  {
    int i = 0;
    float[] array = new float[list.size()];

    for (Float f : list) {
      array[i++] = (f != null ? f : Float.NaN);
    }
    return array;
  }

  private static float round(float d, int decimalPlace) {
    BigDecimal bd = new BigDecimal(Float.toString(d));
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    return bd.floatValue();
  }
}
