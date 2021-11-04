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

	public void saveSensorData (SensorData sensor_data, String file_name) {
		String file_path = new String (getFilesDir () + "/" + file_name);
		if (sensor_data != null) {
			sensor_data.save (file_path);
		}
		return;
	}

	public SensorData loadSensorData (String file_name) {
		String file_path = new String (getFilesDir () + "/" + file_name);
		SensorData sensor_data = new SensorData (400);
		sensor_data.load (file_path);
		return sensor_data;
	}

	// will add more methods later
}