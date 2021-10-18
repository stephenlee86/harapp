package org.tensorflow.lite.examples.transfer;
import org.tensorflow.lite.examples.transfer.api.TransferLearningModel;
import java.math.BigDecimal;
import android.hardware.SensorManager;

/*
The fundemental data for the app
these include: model name, vibrate threshold, running flag, transfer learning model wrapper, 
class ID for determining which class is currently selected, sensor event delay speed, and mode enum
The event delay time is specified in micro seconds, the largest delay time that still meets the models needs is recommended for power consumption purposes
*/

enum Mode {
	Data_Collection,
	Inference,
	Training
}

public class ActivityRecognizer {
String model_name;
double threshold;
boolean is_running;
TransferLearningModelWrapper tl_model;
String class_id;
Mode mode;
int event_delay_time;

public ActivityRecognizer (String mn, double t, int edt) {
	this.model_name = mn;
	this.threshold = t;
	this.is_running = false;
	this.tl_model = null;
	this.class_id = null;
	this.mode = null;
	this.event_delay_time = edt;
}

public ActivityRecognizer (String mn, double t) {
	this (mn, t, SensorManager.SENSOR_DELAY_FASTEST);
}

// setters

public void setModelName (String name) {
	this.model_name = name;
	return;
}

public void setThreshold (double thresh) {
	this.threshold = thresh;
	return;
}

public void setIsRunning (boolean ir) {
	this.is_running = ir;
	return;
}

public void setTLMW (TransferLearningModelWrapper tlmw) {
	this.tl_model = tlmw;
	return;
}

public void setMode (Mode m) {
	this.mode = m;
	return;
}

public void setClassID (String cid) {
	this.class_id = cid;
	return;
}

// sets the delay time of sensor events in micro seconds; however, this is only a soft estimate of what the actual delay time will be
// only way to know for sure will be to check the time stamps of sensor events and take the average of the differences
// The delay time will most likely tend towards being shorter than what is specified

public void setSensorEDT (int edt) {
	this.event_delay_time = edt;
	return;
}

// getters

public String getModelName () {
	return this.model_name;
}

public double getThreshold () {
	return this.threshold;
}

public boolean getIsRunning () {
	return this.is_running;
}

public TransferLearningModelWrapper getTLMW () {
	return this.tl_model;
}

public Mode getMode () {
	return this.mode;
}

public String getClassID () {
	return this.class_id;
}

// gets the delay time of sensor events in micro seconds

public int getSensorEDT () {
	return this.event_delay_time;
}

// might need to add additional methods later, but for now this covers the functionality

}