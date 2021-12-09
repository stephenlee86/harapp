package org.tensorflow.lite.examples.transfer;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import android.content.Intent;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

public class AccelerometerGraphView extends Activity  {
  static final String log_tag = "AccelerometerGraphViewClass";
  static int log_count = 1;
  Button nextButton;
  XYPlot xPlot;
  XYPlot yPlot;
  XYPlot zPlot;
  Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(log_tag + log_count, "executing onCreate method");
        log_count += 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_graph_view);
        nextButton = (Button) findViewById(R.id.nextButton);

        Log.d(log_tag + log_count, "About to load in sensor data that was saved to data.dat during the data sample view activity.");
        log_count += 1;
        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        SensorData sensorData = app.loadSensorData("data.csv");

        TextView label = (TextView) findViewById(R.id.sampleLabelAccelTextView);
        label.setText("Sample Label: " + sensorData.getLabelName());

        // initialize our XYPlot reference:
        Log.d(log_tag + log_count, "About to Creating the plot objects from the sensor data.");
        log_count += 1;
        xPlot = (XYPlot) findViewById(R.id.xAccelerometerPlot);
        yPlot = (XYPlot) findViewById(R.id.yAccelerometerPlot);
        zPlot = (XYPlot) findViewById(R.id.zAccelerometerPlot);

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        List<Long> accelTimeInMilliseconds = sensorData.getElapsedAccelerometerTimeInDeciseconds ();
        XYSeries series1 = new SimpleXYSeries (accelTimeInMilliseconds, sensorData.getXAccelerometerList (),  "accelerometer x data");
        XYSeries series2 = new SimpleXYSeries (accelTimeInMilliseconds, sensorData.getYAccelerometerList (),  "accelerometer y data");
        XYSeries series3 = new SimpleXYSeries (accelTimeInMilliseconds, sensorData.getZAccelerometerList (),  "accelerometer z data");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter (this, R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels2);

        LineAndPointFormatter series3Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels3);

//                // always use DP when specifying pixel sizes, to keep things consistent across devices:
//                PixelUtils.dpToPix(20),
//                PixelUtils.dpToPix(15)}, 0));

        // add a new series' to the xyplot:
        Log.d(log_tag + log_count, "About to add the series objects to the plot objects");
        log_count += 1;
        xPlot.addSeries(series1, series1Format);
        yPlot.addSeries(series2, series2Format);
        zPlot.addSeries(series3, series3Format);

        Log.d(log_tag + log_count, "Plots have been successfully created!");
        log_count += 1;

//        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
//            @Override
//            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//                int i = Math.round(((Number) obj).floatValue());
//                return toAppendTo.append(domainLabels[i]);
//            }
//            @Override
//            public Object parseObject(String source, ParsePosition pos) {
//                return null;
//            }
//        });

    nextButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(log_tag + log_count, "executing onClick method for going to gyroscope activity");
        log_count += 1;
        intent = new Intent (AccelerometerGraphView.this, GyroscopeGraphView.class);
        startActivity (intent);
        finish ();
      }
    });
    }
}