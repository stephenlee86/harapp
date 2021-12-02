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
There is currently commented out depricated code in case the design needs to be changed later on
*/

public class DataModels implements Serializable {
private ActivityRecognizer activity_recognizer;
private List <String> sample_keys;
private String file_extension;

public DataModels (ActivityRecognizer ar, String fe) {
	this.activity_recognizer = ar;
	this.sample_keys = new ArrayList <String> ();
	this.file_extension = fe;
}

public DataModels (String fe) {
	this (new ActivityRecognizer ("ar_model", 0.75), fe);
}

public DataModels () {
	this (new ActivityRecognizer ("ar_model", 0.75), ".dat");
}

/*

This is old code for managing individual hash tables for collection, training, and inference
Left this in commented out in case at some point the design plan changed
Can be deleted later once design is finalized

public void addCollectionSensorData (SensorData sd) {
	if (this.removed_collection_keys.size () == 0) {
		this.collection_keys.add ("collection " + (this.collection_data_samples.size () + 1));
		this.collection_data_samples.put ("collection " + (this.collection_data_samples.size () + 1), sd);
	} else {
		Integer index = this.removed_collection_keys.firstKey ();
		int i = index.intValue ();
		this.collection_keys.set (i, this.removed_collection_keys.get (index));
		this.collection_data_samples.put (this.removed_collection_keys.get (index), sd);
		this.removed_collection_keys.remove (index);
	}
	return;
}

public void addTrainingSensorData (SensorData sd) {
	if (this.removed_training_keys.size () == 0) {
		this.training_keys.add ("training " + (this.training_data_samples.size () + 1));
		this.training_data_samples.put ("training " + (this.training_data_samples.size () + 1), sd);
	} else {
		Integer index = this.removed_training_keys.firstKey ();
		int i = index.intValue ();
		this.training_keys.set (i, this.removed_training_keys.get (index));
		this.training_data_samples.put (this.removed_training_keys.get (index), sd);
		this.removed_training_keys.remove (index);
	}
	return;
}

public void addInferenceSensorData (SensorData sd) {
	if (this.removed_inference_keys.size () == 0) {
		this.inference_keys.add ("inference " + (this.inference_data_samples.size () + 1));
		this.inference_data_samples.put ("inference " + (this.inference_data_samples.size () + 1), sd);
	} else {
		Integer index = this.removed_inference_keys.firstKey ();
		int i = index.intValue ();
		this.inference_keys.set (i, this.removed_inference_keys.get (index));
		this.inference_data_samples.put (this.removed_inference_keys.get (index), sd);
		this.removed_inference_keys.remove (index);
	}
	return;
}

public int getCollectionSize () {
	return this.collection_data_samples.size ();
}

public int getTrainingSize () {
	return this.training_data_samples.size ();
}

public int getInferenceSize () {
	return this.inference_data_samples.size ();
}


Will use an activity recognizer object to add a sensor data object to the correct hash table
This is also old code that uses multiple hash tables

public void addDataSample (SensorData sd, ActivityRecognizer ar) {
	switch (ar.getMode ()) {
		case Data_Collection: {
			this.addCollectionSensorData (sd);
			break;
		}
		case Training: {
			this.addTrainingSensorData (sd);
			break;
		}
		case Inference: {
			this.addInferenceSensorData (sd);
			break;
		}
	}
	return;
}
*/

public ActivityRecognizer getActivityRecognizer () {
	return this.activity_recognizer;
}

// Private inner method for adding data sample

private String addSensorData (SensorData sd, String mode) {
	String type = new String (this.activity_recognizer.getClassID () + "_" + mode + "_");
	int i = 1;
	String key = new String (type.toLowerCase() + "_" + i);
	while (this.sample_keys.contains (key)) {
		++i;
		key = new String (type.toLowerCase() + "_" + i);
	}
	this.sample_keys.add (key);
	return key;
}

// Public wrapper method for adding data sample

public void addDataSample (SensorData sd, String file_path) {
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
	sd.save (new String (file_path + key + this.file_extension));
	return;
}

// method just for adding a sample key if a given sample has already been saved to disc under a given name

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

/*

More old method versions
Methods for retrieving data samples by their string key

public SensorData getCollectionSensorData (String key) {
	return this.collection_data_samples.get (key);
}

public SensorData getTrainingSensorData (String key) {
	return this.training_data_samples.get (key);
}

public SensorData getInferenceSensorData (String key) {
	return this.inference_data_samples.get (key);
}

public List <String> getCollectionKeys () {
	return this.collection_keys;
}

public List <String> getTrainingKeys () {
	return this.training_keys;
}

public List <String> getInferenceKeys () {
	return this.inference_keys;
}

public String getCollectionKeyByIndex (int i) {
	return this.collection_keys.get (i);
}

public String getTrainingKeyByIndex (int i) {
	return this.training_keys.get (i);
}

public String getInferenceKeyByIndex (int i) {
	return this.inference_keys.get (i);
}

public int collectionSize () {
	return this.collection_keys.size ();
}

public int trainingSize () {
	return this.training_keys.size ();
}

public int inferenceSize () {
	return this.inference_keys.size ();
}

public void deleteCollectionDataSampleByIndex (int index) {
	String key = this.collection_keys.get (i);
	this.collection_data_samples.remove (key);
	this.removed_collection_keys.put (new Integer (index), key);
	this.collection_keys.set (index, "deleted");
	return;
}

public void deleteTrainingDataSampleByIndex (int index) {
	String key = this.training_keys.get (i);
	this.training_data_samples.remove (key);
	this.removed_training_keys.put (new Integer (index), key);
	this.training_keys.set (index, "deleted");
	return;
}

public void deleteInferenceDataSampleByIndex (int index) {
	String key = this.inference_keys.get (i);
	this.inference_data_samples.remove (key);
	this.removed_inference_keys.put (new Integer (index), key);
	this.inference_keys.set (index, "deleted");
	return;
}
*/

public SensorData getSampleByName (String key, String file_path) {
	SensorData sd = new SensorData ();
	sd.load (new String (file_path + key + this.file_extension));
	return sd;
}

public List <String> getSampleNames () {
	return this.sample_keys;
}

public String getSampleNameByIndex (int index) {
	return this.sample_keys.get (index);
}

public SensorData getSampleByIndex (int index, String file_path) {
	String key = this.sample_keys.get (index);
	return this.getSampleByName (key, file_path);
}

public void deleteSampleByName (String key, String file_path) {
	File file = new File (new String (file_path + key + this.file_extension));
	boolean deleted = file.delete ();
	if (deleted || file.exists() == false) {
		this.sample_keys.remove (key);
	}
}

public void deleteSampleByIndex (int index, String file_path) {
	String key = this.sample_keys.get (index);
	File file = new File (new String (file_path + key + this.file_extension));
	boolean deleted = file.delete ();
	if (deleted || file.exists() == false) {
		this.sample_keys.remove (key);
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

/*

Methods for saving and loading DataModels out to disc and back
Previous versions

public void save (String filePath) {
	try {
		String key;
		SensorData sd;
		FileOutputStream fos = new FileOutputStream(filePath, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		this.activity_recognizer.save (outputStream);
		for (TreeMap.Entry <String, String> entry : this.collection_data_samples.entrySet ()) {
			key = entry.getKey ();
			sd = entry.getValue ();
			outputStream.writeObject(key);
			sd.save (outputStream);
		}
		for (TreeMap.Entry <String, String> entry : this.training_data_samples.entrySet ()) {
			key = entry.getKey ();
			sd = entry.getValue ();
			outputStream.writeObject(key);
			sd.save (outputStream);
		}
		for (TreeMap.Entry <String, String> entry : this.inference_data_samples.entrySet ()) {
			key = entry.getKey ();
			sd = entry.getValue ();
			outputStream.writeObject(key);
			sd.save (outputStream);
		}
		outputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	}
	return;
}

public void load (String filePath) {
	try {
		String key;
		SensorData sd = new SensorData ();
		FileInputStream fin = new FileInputStream(filePath, false);
		ObjectInputStream inputStream = new ObjectInputStream(fin);
		if (inputStream.available () == 0) {
			inputStream.close ();
			return;
		}
		this.activity_recognizer.load (inputStream);
		if (inputStream.available () == 0) {
			inputStream.close ();
			return;
		}
		key = inputStream.readObject();
		sd.load (inputStream);
		while (key.contains ("collection")) {
			this.collection_data_samples.put (key, sd);
			this.collection_keys.add (key);
			if (inputStream.available () == 0) {
				break;
			}
			key = inputStream.readObject();
			sd = new SensorData ();
			sd.load (inputStream);
		}
		while (key.contains ("training")) {
			this.training_data_samples.put (key, sd);
			this.training_keys.add (key);
			if (inputStream.available () == 0) {
				break;
			}
			key = inputStream.readObject();
			sd = new SensorData ();
			sd.load (inputStream);
		}
		while (key.contains ("inference")) {
			this.inference_data_samples.put (key, sd);
			this.inference_keys.add (key);
			if (inputStream.available () == 0) {
				break;
			}
			key = inputStream.readObject();
			sd = new SensorData ();
			sd.load (inputStream);
		}
		inputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	}
	return;
}
*/

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