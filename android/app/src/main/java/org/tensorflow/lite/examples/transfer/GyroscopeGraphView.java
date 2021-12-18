package org.tensorflow.lite.examples.transfer;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import android.app.Activity;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

public class GyroscopeGraphView extends Activity  {
  Button closeButton;
  XYPlot xPlot;
  XYPlot yPlot;
  XYPlot zPlot;
  Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gyroscope_graph_view);
        closeButton = (Button) findViewById(R.id.closeButton);

        ActivityRecognitionApplication app = (ActivityRecognitionApplication) getApplication();
        SensorData sensorData = app.loadSensorData("data.csv");

        TextView label = (TextView) findViewById(R.id.sampleLabelGyroTextView);
        label.setText("Sample Label: " + sensorData.getLabelName());

        // initialize our XYPlot reference:
        xPlot = (XYPlot) findViewById(R.id.xGyroscopePlot);
        yPlot = (XYPlot) findViewById(R.id.yGyroscopePlot);
        zPlot = (XYPlot) findViewById(R.id.zGyroscopePlot);

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        List<Long> gyroTimeInMilliseconds = sensorData.getElapsedGyroscopeTimeInDeciseconds ();
        XYSeries series4 = new SimpleXYSeries (gyroTimeInMilliseconds, sensorData.getXGyroscopeList (), "gyroscope x data");
        XYSeries series5 = new SimpleXYSeries (gyroTimeInMilliseconds, sensorData.getYGyroscopeList (), "gyroscope y data");
        XYSeries series6 = new SimpleXYSeries (gyroTimeInMilliseconds, sensorData.getZGyroscopeList (), "gyroscope z data");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
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

        xPlot.addSeries(series4, series4Format);
        yPlot.addSeries(series5, series5Format);
        zPlot.addSeries(series6, series6Format);

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

    closeButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        intent = new Intent (GyroscopeGraphView.this, MainActivity.class);
        startActivity (intent);
        finish ();
      }
    });
    }
}