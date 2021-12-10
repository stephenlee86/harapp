package org.tensorflow.lite.examples.transfer;

import android.app.*;
import java.util.*;

public class ActivityRecognitionApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// Required initialization logic here!
	}

//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

// method for saving samples with a user specified file name

	public void saveSensorData (SensorData sensor_data, String file_name) {
		String name;
		String file_path;
		name = file_name;
		file_path = new String (getFilesDir () + "/" + name);
		if (sensor_data != null) {
			sensor_data.saveToCSV (file_path);
		}
		return;
	}

// method for saving sensor data with an automatically generated file name

	public void saveSensorData (DataModels data_models, SensorData sensor_data) {
		String file_path = new String (getFilesDir () + "/");
		if (sensor_data != null) {
			data_models.addDataSample (sensor_data, file_path);
		}
		return;
	}

// used to load sensor data by item index

	public SensorData loadSensorData (DataModels data_models, int index) {
		String file_path = new String (getFilesDir () + "/" + data_models.getSampleNameByIndex (index) + data_models.getFileExtension ());
		SensorData sensor_data = new SensorData ("UNKNOWN");
		sensor_data.loadFromCSV (file_path);
		return sensor_data;
	}

	public SensorData loadSensorData (DataModels data_models, String name) {
		String file_path = new String (getFilesDir () + "/" + name + data_models.getFileExtension ());
		SensorData sensor_data = new SensorData ("UNKNOWN");
		sensor_data.loadFromCSV (file_path);
		return sensor_data;
	}

	public SensorData loadSensorData (String name) {
		String file_path = new String (getFilesDir () + "/" + name);
		SensorData sensor_data = new SensorData ("UNKNOWN");
		sensor_data.loadFromCSV (file_path);
		return sensor_data;
	}


// method for loading a data models object from file using the file name with extension included

	public DataModels loadDataModels (String file_name) {
		String file_path = new String (getFilesDir () + "/" + file_name);
		DataModels data_models = new DataModels ();
		data_models.load (file_path);
		return data_models;
	}

// method for saving data models object

	public void saveDataModels (DataModels data_models, String file_name) {
		String file_path = new String (getFilesDir () + "/" + file_name);
		data_models.save (file_path);
	}

	// will add more methods later
}