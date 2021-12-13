package org.tensorflow.lite.examples.transfer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.recyclerview.widget.*;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

public class DataSampleView extends Activity
        implements ItemClickListener
{
  static final String log_tag = "DataSampleViewClass";
  static int log_count = 1;
  private Button displayGraphButton;
  private Button renameSampleButton;
  private Button deleteSampleButton;
  private Button markAsTrainingButton;
  private Button markAsInferenceButton;
  private Button deleteAllButton;
  private Button backButton;
  private RecyclerView rv;
  private Intent intent;
  private DataModels dataModels;
  private SensorData sensorData;
  private SampleAdapter adapter;
  private String sample;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(log_tag + log_count, "executing onCreate method");
        log_count += 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_sample_view);
        displayGraphButton = (Button) findViewById(R.id.displayGraphButton);
        displayGraphButton.setEnabled (false);
        renameSampleButton = (Button) findViewById(R.id.renameSampleButton);
        renameSampleButton.setEnabled (false);
        deleteSampleButton = (Button) findViewById(R.id.deleteSampleButton);
        deleteSampleButton.setEnabled (false);
        markAsTrainingButton = (Button) findViewById(R.id.markAsTrainingButton);
        markAsTrainingButton.setEnabled (false);
        markAsInferenceButton = (Button) findViewById(R.id.markAsInferenceButton);
        markAsInferenceButton.setEnabled (false);
        deleteAllButton = (Button) findViewById(R.id.deleteAllSamplesButton);
        deleteAllButton.setEnabled (true);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setEnabled (true);

        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        dataModels = app.loadDataModels("samples.dat");
        Log.d(log_tag + log_count, "Loaded DataModels object from file successfully!");
        log_count += 1;

        adapter = new SampleAdapter(dataModels, R.layout.rv_sample_row, this);
        rv = (RecyclerView)findViewById(R.id.sample_recyclerview);
        rv.setLayoutManager (new LinearLayoutManager (this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize (true);
        rv.setAdapter(adapter);
        adapter.setClickListener (this); // Bind the listener
        Log.d(log_tag + log_count, "Successfully set up RecyclerView!");
        log_count += 1;

    displayGraphButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the display graph button.");
        log_count += 1;
        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        app.saveDataModels (dataModels, "samples.dat");
        Log.d(log_tag + log_count, "Successfully saved the DataModels object to disc before launching graph viewing activity!");
        log_count += 1;
        app.saveSensorData (sensorData, "data.csv");
        Log.d(log_tag + log_count, "Successfully saved the SensorData object to disc before launching graph viewing activity!");
        log_count += 1;
        intent = new Intent (DataSampleView.this, AccelerometerGraphView.class);
        startActivity (intent);
        Log.d(log_tag + log_count, "Successfully started AccelerometerGraphView activity!");
        log_count += 1;
        finish ();
        Log.d(log_tag + log_count, "Successfully finished DataSampleView activity!");
        log_count += 1;
      }
    });

    backButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the back button.");
        log_count += 1;
        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        app.saveDataModels (dataModels, "samples.dat");
        intent = new Intent (DataSampleView.this, MainActivity.class);
        startActivity (intent);
        finish ();
      }
    });

    renameSampleButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the rename button.");
        log_count += 1;
        RenameSamplePopUp popUpClass = new RenameSamplePopUp(dataModels, sample, getFilesDir ().toString());
        popUpClass.showPopupWindow(v);
        adapter.notifyDataSetChanged();
        sample = dataModels.getSampleNameByIndex (adapter.getSelectedRow ());
      }
    });

    deleteSampleButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the delete button.");
        log_count += 1;
        DeleteSamplePopUp popUpClass = new DeleteSamplePopUp(dataModels, sample, getFilesDir ().toString());
        popUpClass.showPopupWindow(v);

        sample = null;
        displayGraphButton.setEnabled (false);
        renameSampleButton.setEnabled (false);
        deleteSampleButton.setEnabled (false);
        markAsTrainingButton.setEnabled (false);
        markAsInferenceButton.setEnabled (false);

        rv.invalidate();
      }
    });

    deleteAllButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the delete all button.");
        log_count += 1;
        DeleteAllPopUp popUpClass = new DeleteAllPopUp(dataModels, getFilesDir ().toString(), "samples.dat");
        popUpClass.showPopupWindow(v);

        sample = null;
        displayGraphButton.setEnabled (false);
        renameSampleButton.setEnabled (false);
        deleteSampleButton.setEnabled (false);
        markAsTrainingButton.setEnabled (false);
        markAsInferenceButton.setEnabled (false);
      }
    });

    markAsTrainingButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the mark as training button.");
        log_count += 1;
        boolean renamed = dataModels.designateForTrainingByName (sample, new String (getFilesDir ().toString() + "/"));
        if (renamed) {
            adapter.notifyDataSetChanged();
          sample = dataModels.getSampleNameByIndex (adapter.getSelectedRow ());
        } else {
          Toast.makeText(v.getContext(), "The sample could not be renamed", Toast.LENGTH_SHORT).show();
        }
      }
    });

    markAsInferenceButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for the mark as inference button.");
        log_count += 1;
        boolean renamed = dataModels.designateForInferenceByName (sample, new String (getFilesDir ().toString() + "/"));
        if (renamed) {
            adapter.notifyDataSetChanged();
          sample = dataModels.getSampleNameByIndex (adapter.getSelectedRow ());
        } else {
          Toast.makeText(v.getContext(), "The sample could not be renamed", Toast.LENGTH_SHORT).show();
        }
      }
    });
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        Log.d(log_tag + log_count, "executing onClick method for the RecyclerView.");
        log_count += 1;
        adapter.notifyItemChanged(adapter.getSelectedRow());
        adapter.setSelectedRow(position);
        adapter.notifyItemChanged(position);

        sample = dataModels.getSampleNameByIndex (position);
        displayGraphButton.setEnabled (true);
        renameSampleButton.setEnabled (true);
        deleteSampleButton.setEnabled (true);
        markAsInferenceButton.setEnabled (true);
        markAsTrainingButton.setEnabled (true);

        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        sensorData = app.loadSensorData (dataModels, sample);
    }

    protected void onResume()
    {
        super.onResume();
        rv.invalidate();
    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            adapter.notifyDataSetChanged();
        }
    }
    private List<SampleViewModel> generateSampleList() {
        List<SampleViewModel> sampleViewModelList = new ArrayList<>();

        for (int i = 0; i < dataModels.getSize (); i++) {
            sampleViewModelList.add(new SampleViewModel(dataModels.getSampleNameByIndex (i)));
        }

        return sampleViewModelList;
    }
}