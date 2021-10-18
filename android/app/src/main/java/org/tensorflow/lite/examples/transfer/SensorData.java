package org.tensorflow.lite.examples.transfer;
import java.util.*;

/*
handles all sensor data for x, y, and z, as well as for the over all input list
The desired sample size will also be stored here since it determines how many samples should be collected before the sensor data should be processed as a batch
*/

public class SensorData {
int sample_number;
List<Float> x_accel;
List<Float> y_accel;
List<Float> z_accel;
List<Float> x_gyro;
List<Float> y_gyro;
List<Float> z_gyro;

List<Float> input_signal;

public SensorData (int samples) {
	this.sample_number = samples;
	this.x_accel = new ArrayList<Float>();
	this.y_accel = new ArrayList<Float>();
	this.z_accel = new ArrayList<Float>();
	this.x_gyro = new ArrayList<Float>();
	this.y_gyro = new ArrayList<Float>();
	this.z_gyro = new ArrayList<Float>();
	this.input_signal = new ArrayList<Float>();
}

// This method returns true if the number of samples collected across all of the lists has hit the sample number

public boolean checkSampleCount () {
	if(this.x_accel.size() == this.sample_number && this.y_accel.size() == this.sample_number &&
	this.z_accel.size() == this.sample_number && this.x_gyro.size() == this.sample_number &&
	this.y_gyro.size() == this.sample_number && this.z_gyro.size() == this.sample_number) {
		return true;
	} else {
		return false;
	}
}

// getter for input_signal list

public List<Float> getInputSignal () {
	return this.input_signal;
}

// add the x, y, and z values from an accelerometer event to the accelerometer list

public void addAccelEvent (Float x, Float y, Float z) {
	this.x_accel.add (x); 
	this.y_accel.add (y); 
	this.z_accel.add (z);
	return;
}

// add the x, y, and z values from a gyroscope event to the accelerometer list

public void addGyroEvent (Float x, Float y, Float z) {
	this.x_gyro.add (x); 
	this.y_gyro.add (y); 
	this.z_gyro.add (z);
	return;
}

// populate input signal list with items from accelerometer list and gyroscope list

public void populateInputSignal () {
	int i = 0;
	while (i < this.sample_number) {
		this.input_signal.add(this.x_accel.get(i));
		this.input_signal.add(this.y_accel.get(i));
		this.input_signal.add(this.z_accel.get(i));
		this.input_signal.add(this.x_gyro.get(i));
		this.input_signal.add(this.y_gyro.get(i));
		this.input_signal.add(this.z_gyro.get(i));
		i++;
	}
	return;
}

// clear all seven lists

public void clear () {
	this.x_accel.clear(); 
	this.y_accel.clear(); 
	this.z_accel.clear();
	this.x_gyro.clear(); 
	this.y_gyro.clear(); 
	this.z_gyro.clear();
	this.input_signal.clear();
}

// setter for the sample number

public void setSampleNumber (int samples) {
	this.sample_number = samples;
}

public int getSampleNumber () {
	return this.sample_number;
}
}