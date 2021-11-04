package org.tensorflow.lite.examples.transfer;
import org.tensorflow.lite.examples.transfer.api.TransferLearningModel;
import java.math.BigDecimal;
import android.hardware.SensorManager;
import java.io.*;

/*
The fundemental data for the app
these include: model name, vibrate threshold, running flag, transfer learning model wrapper, 
class ID for determining which class is currently selected, sensor event delay speed, and mode enum
The event delay time is specified in micro seconds, the largest delay time that still meets the models needs is recommended for power consumption purposes
This includes a flag variable that lets the main activity know that a graphing activity should be launched, along with its getter and setter
This class has load and save methods for writing all data out to disc save the transfer learning model wrapper object
*/

enum Mode {
	Data_Collection,
	Inference,
	Training
}

public class ActivityRecognizer {
private String model_name;
private double threshold;
private boolean is_running;
private boolean is_graphing;
private TransferLearningModelWrapper tl_model;
private String class_id;
private Mode mode;
private int event_delay_time;

public ActivityRecognizer (String mn, double t, int edt) {
	this.model_name = mn;
	this.threshold = t;
	this.is_running = false;
	this.is_graphing = false;
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

public void setIsGraphing (boolean ig) {
	this.is_graphing = ig;
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

public boolean getIsGraphing () {
	return this.is_graphing;
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

// methods for loading from disc and saving to disc

public void load (String filePath) {
	try {
		FileInputStream fin = new FileInputStream(filePath);
		ObjectInputStream inputStream = new ObjectInputStream(fin);
		this.model_name = (String)inputStream.readObject();
		this.threshold = (Float)inputStream.readObject();
		this.is_running = (Boolean)inputStream.readObject();
		this.is_graphing = (Boolean)inputStream.readObject();
		this.class_id = (String)inputStream.readObject();
		this.event_delay_time = (Integer)inputStream.readObject();
		switch ((String)inputStream.readObject()) {
			case "Data_Collection": {
				this.mode = Mode.Data_Collection;
			}
			case "Inference": {
				this.mode = Mode.Inference;
			}
			case "Training": {
				this.mode = Mode.Training;
			}
		}
		inputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	} catch (ClassNotFoundException ex)
	{
		System.err.println(ex);
	}
}

public void save (String filePath) {
	try {
		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		outputStream.writeObject(this.model_name);
		outputStream.writeObject(this.threshold);
		outputStream.writeObject(this.is_running);
		outputStream.writeObject(this.is_graphing);
		outputStream.writeObject(this.class_id);
		outputStream.writeObject(this.event_delay_time);
		switch (this.mode) {
			case Data_Collection: {
				outputStream.writeObject("Data_Collection");
			}
			case Inference: {
				outputStream.writeObject("Inference");
			}
			case Training: {
				outputStream.writeObject("Training");
			}
		}
		outputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	}
	return;
}

// may require more methods later
}