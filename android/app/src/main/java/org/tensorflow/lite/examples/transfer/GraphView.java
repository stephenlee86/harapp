package org.tensorflow.lite.examples.transfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;

import android.graphics.*;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;

public class GraphView extends Activity  {
  Button closeButton;
  XYPlot plot;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_xy_plot_example);
        //closeButton = (Button) findViewById(R.id.buttonClose);

        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        SensorData sensorData = app.loadSensorData("sensor.dat");

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        List<Long> accelTimeInMilliseconds = sensorData.getElapsedAccelerometerTimeInDeciseconds ();
        XYSeries series1 = new SimpleXYSeries (accelTimeInMilliseconds, sensorData.getXAccelerometerList (),  "accelerometer x data");
        XYSeries series2 = new SimpleXYSeries (accelTimeInMilliseconds, sensorData.getYAccelerometerList (),  "accelerometer y data");
        XYSeries series3 = new SimpleXYSeries (accelTimeInMilliseconds, sensorData.getZAccelerometerList (),  "accelerometer z data");
        List<Long> gyroTimeInMilliseconds = sensorData.getElapsedGyroscopeTimeInDeciseconds ();
        XYSeries series4 = new SimpleXYSeries (gyroTimeInMilliseconds, sensorData.getXGyroscopeList (), "gyroscope x data");
        XYSeries series5 = new SimpleXYSeries (gyroTimeInMilliseconds, sensorData.getYGyroscopeList (), "gyroscope y data");
        XYSeries series6 = new SimpleXYSeries (gyroTimeInMilliseconds, sensorData.getZGyroscopeList (), "gyroscope z data");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter (this, R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels2);

        LineAndPointFormatter series3Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels3);

        LineAndPointFormatter series4Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels4);

        LineAndPointFormatter series5Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels5);

        LineAndPointFormatter series6Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels6);

//                // always use DP when specifying pixel sizes, to keep things consistent across devices:
//                PixelUtils.dpToPix(20),
//                PixelUtils.dpToPix(15)}, 0));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);
        plot.addSeries(series3, series3Format);
        plot.addSeries(series4, series4Format);
        plot.addSeries(series5, series5Format);
        plot.addSeries(series6, series6Format);

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

//    closeButton.setOnClickListener( new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        finish ();
//      }
//    });
    }
}