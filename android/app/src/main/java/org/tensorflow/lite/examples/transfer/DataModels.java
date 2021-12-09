package org.tensorflow.lite.examples.transfer;

import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.concurrent.*;

/*
Holds all collection sample names of sensor data for raw collection, training, and inference
Stores the current state of the activity recognizer object
Manages removing and deleting samples of sensor data that are either no longer needed or are unwanted
Manages renaming of data samples and moving them from collection to training or inference
Allows samples to also be changed between training and inference 
samples will be saved to disc as dat files with the sample name as the file name
When the samples are loaded from disc they will use the file name as their sample name
Sample names will be generated using the mode enum, the activity label, and a number
However, the user can change the name of a given sample at any time as long as another sample does not already have the same name
This object also determines what file extension is used to save the data
*/

public class DataModels implements Serializable {
private ActivityRecognizer activity_recognizer; // The activity recognizer object that holds the current state of the over all app
private List <String> sample_keys; // These are the file names the samples are saved under as well as the names of the samples that will be displayed in the sample view
private String file_extension; // The file extension that will be used for saving and loading all samples to and from disc

public DataModels (ActivityRecognizer ar, String fe) {
	this.activity_recognizer = ar;
	this.sample_keys = new ArrayList <String> ();
	this.file_extension = fe;
}

public DataModels (String fe) {
	this (new ActivityRecognizer ("ar_model", 0.75), fe);
}

public DataModels () {
	this (new ActivityRecognizer ("ar_model", 0.75), ".csv");
}

public ActivityRecognizer getActivityRecognizer () {
	return this.activity_recognizer;
}

// Private inner method for adding data sample
// Might want to add more robust exception handling for file operations later
// For now it works well enough

private String addSensorData (SensorData sd, String mode) {
	String type = new String (this.activity_recognizer.getClassID () + "_" + mode + "_");
	type = type.replace(' ', '_');
	int i = 1;
	String key = new String (type.toLowerCase() + "_" + i);
	while (this.sample_keys.contains (key)) {
		++i;
		key = new String (type.toLowerCase() + "_" + i);
	}
	return key;
}

// Public wrapper method for adding data sample
// Currently uses the saveToCSV method but that method can be swapped out for another one later if the data needs to be written to disc differently
// returns a boolean because it is useful for error handling 
// returns true if it saves successfully, otherwise it will return false

public boolean addDataSample (SensorData sd, String file_path) {
	String key = "";
	switch (this.activity_recognizer.getMode ()) {
		case Data_Collection: {
			key = this.addSensorData (sd, "collection");
			break;
		}
		case Training: {
			key = this.addSensorData (sd, "training");
			break;
		}
		case Inference: {
			key = this.addSensorData (sd, "inference");
			break;
		}
	}
	boolean saved = sd.saveToCSV (new String (file_path + key + this.file_extension));
	if (saved) {
		this.addSampleName (key);
		return true;
	} else {
		return false;
	}
}

// method just for adding a sample key / the samples name

public void addSampleName (String key) {
	key = key.replace (' ', '_');
	key = key.toLowerCase ();
	if ((!this.sample_keys.contains (key)) && (!key.contains ("."))) {
		this.sample_keys.add (key);
		return;
	} else {
		return;
	}
}

// method for getting the file extension

public String getFileExtension () {
	return this.file_extension;
}

// method for getting the number of over all samples

public int getSize () {
	return this.sample_keys.size ();
}

// Useful for loading any sample from memory by name within an activity if the DataModels object has already been loaded from memory

public SensorData getSampleByName (String key, String file_path) {
	SensorData sd = new SensorData ("temp");
	sd.loadFromCSV (new String (file_path + key + this.file_extension));
	return sd;
}

// retrieves the full list of sample names

public List <String> getSampleNames () {
	return this.sample_keys;
}

// retrieves a single sample name by index of that sample

public String getSampleNameByIndex (int index) {
	return this.sample_keys.get (index);
}

// Performs the same load operation only it takes the index as arguement instead of the sample name

public SensorData getSampleByIndex (int index, String file_path) {
	String key = this.sample_keys.get (index);
	return this.getSampleByName (key, file_path);
}

// Deletes a single sample file, and also removes that samples entries from the two lists
// This call will handle some exception cases that creation will not

public boolean deleteSampleByName (String key, String file_path) {
	File file = new File (new String (file_path + key + this.file_extension));
	boolean deleted = file.delete ();
	if (deleted || file.exists() == false) {
		this.sample_keys.remove (key);
		return true;
	} else {
		return false;
	}
}

public boolean deleteSampleByIndex (int index, String file_path) {
	String key = this.sample_keys.get (index);
	File file = new File (new String (file_path + key + this.file_extension));
	boolean deleted = file.delete ();
	if (deleted || file.exists() == false) {
		this.sample_keys.remove (key);
		return true;
	} else {
		return false;
	}
}

public void deleteAll (String file_path, String file_name) {
	String key;
	File file;
	boolean deleted;
	for (int i = 0; i < this.sample_keys.size (); i++) {
		key = this.sample_keys.get (i);
		file = new File (new String (file_path + "/" + key + this.file_extension));
		deleted = file.delete ();
		if (deleted || file.exists() == false) {
			this.sample_keys.remove (key);
			i--;
		}
	}
	save(file_path + "/" + file_name);
}

// Will change the samples name along with the name of the file that the sample is saved in

public boolean renameSampleByName (String key, String new_key, String file_path) {
	new_key = new_key.replace (' ', '_');
	new_key = new_key.toLowerCase ();
	if (this.sample_keys.contains (new_key)) {
		int i = 1;
		while (this.sample_keys.contains (new_key)) {
			new_key = new String (new_key + i);
			i++;
		}
	}
	int index = this.sample_keys.indexOf (key);
	if (file_path.charAt(file_path.length()-1) != '/')
		file_path += "//";
	File old_file = new File (new String (file_path + key + this.file_extension));
	File new_file = new File (new String (file_path + new_key + this.file_extension));
	boolean renamed = old_file.renameTo (new_file);
	if (renamed) {
		this.sample_keys.set (index, new_key);
		return true;
	} else {
		return false;
	}
}

public void renameSampleByIndex (int index, String new_key, String file_path) {
	String key = this.sample_keys.get (index);
	File old_file = new File (new String (file_path + key + this.file_extension));
	File new_file = new File (new String (file_path + new_key + this.file_extension));
	boolean renamed = old_file.renameTo (new_file);
	if (renamed) {
		this.sample_keys.set (index, new_key);
		return;
	} else {
		return;
	}
}

// Performs a sample name change that shows the sample is a training sample instead of being for raw collection or inference

public boolean designateForTrainingByName (String key, String file_path) {
	String new_key;
	if (key.contains ("collection")) {
		new_key = key.replace ("collection", "training");
		return this.renameSampleByName (key, new_key, file_path);
	} else if (key.contains ("inference")) {
		new_key = key.replace ("inference", "training");
		return this.renameSampleByName (key, new_key, file_path);
	} else if (key.contains ("collect")) {
		new_key = key.replace ("collect", "train");
		return this.renameSampleByName (key, new_key, file_path);
	} else if (key.contains ("infer")) {
		new_key = key.replace ("infer", "train");
		return this.renameSampleByName (key, new_key, file_path);
	} else {
		return false;
	}
}

public void designateForTrainingByIndex (int index, String file_path) {
	String new_key;
	String key = this.sample_keys.get (index);
	if (key.contains ("collection") || key.contains ("inference")) {
		new_key = key.replace ("collection", "training");
		new_key = key.replace ("inference", "training");
		this.renameSampleByIndex (index, new_key, file_path);
	} else {
		return;
	}
}

// Uses string replace to change a name from having train or collect to having inference instead

public boolean designateForInferenceByName (String key, String file_path) {
	String new_key;
	if (key.contains ("collection")) {
		new_key = key.replace ("collection", "inference");
		return this.renameSampleByName (key, new_key, file_path);
	} else if (key.contains ("training")) {
		new_key = key.replace ("training", "inference");
		return this.renameSampleByName (key, new_key, file_path);
	} else if (key.contains ("collect")) {
		new_key = key.replace ("collect", "infer");
		return this.renameSampleByName (key, new_key, file_path);
	} else if (key.contains ("train")) {
		new_key = key.replace ("train", "infer");
		return this.renameSampleByName (key, new_key, file_path);
	} else {
		return false;
	}
}

public void designateForInferenceByIndex (int index, String file_path) {
	String new_key;
	String key = this.sample_keys.get (index);
	if (key.contains ("collection") || key.contains ("training")) {
		new_key = key.replace ("collection", "inference");
		new_key = key.replace ("training", "inference");
		this.renameSampleByIndex (index, new_key, file_path);
	} else {
		return;
	}
}

// methods for saving and loading the DataModels object itself

public void save (String file_path) {
	try {
		String key;
		FileOutputStream fos = new FileOutputStream(file_path, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		this.activity_recognizer.save (outputStream);
		outputStream.writeObject(new Integer (this.sample_keys.size ()));
		for (int i = 0; i < this.sample_keys.size (); i++) {
			key = this.sample_keys.get (i);
			outputStream.writeObject(key);
		}
		outputStream.writeObject(this.file_extension);
		outputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	}
	return;
}

public void load (String file_path) {
	try {
		String key;
		FileInputStream fin = new FileInputStream(file_path);
		ObjectInputStream inputStream = new ObjectInputStream(fin);
//		if (inputStream.available () == 0) {
//			inputStream.close ();
//			return;
//		}
		this.activity_recognizer.load (inputStream);
//		if (inputStream.available () == 0) {
//			inputStream.close ();
//			return;
//		}
		Integer count = (Integer)inputStream.readObject();
		int size = count.intValue ();
		this.sample_keys.clear ();
		for (int i = 0; i < size; i++) {
//			if (inputStream.available () == 0) {
//				break;
//			}
			key = (String)inputStream.readObject();
			this.sample_keys.add (key);
		}
		if (inputStream.available () == 0) {
			inputStream.close ();
			return;
		}
		this.file_extension = (String)inputStream.readObject();
		inputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	} catch (ClassNotFoundException ex) {
		System.err.println(ex);
	}
	
	return;
}

// may need to add more methods later

}