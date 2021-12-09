package org.tensorflow.lite.examples.transfer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.lang.*;

/*
handles all sensor data for x, y, and z, as well as for the over all input list
The desired sample size will also be stored here since it determines how many samples should be collected before the sensor data should be processed as a batch
Lists with times in nanoseconds will also be populated and stored here for producing graphs
This class implements serializable interface for the sake of passing sensor data between activities
This class also has direct save and load methods for reading and writing to disc
Keep in mind that all time values are stored in List objects as Long objects, and that all times stored in instances of this class are initially 
stored in nanoseconds
Methods are provided to return Lists of Elapsed times stored in Long objects for time units other than nanoseconds
*/

public class SensorData implements Serializable {
	private String label_name;
private int sample_number;
private List<Float> x_accel;
private List<Float> y_accel;
private List<Float> z_accel;
private List<Float> x_gyro;
private List<Float> y_gyro;
private List<Float> z_gyro;
private List<Long> accel_times;
private List<Long> gyro_times;

private static List<Float> input_signal = new ArrayList<Float>();

public SensorData (String label, int samples) {
	this.label_name = label;
	this.sample_number = samples;
	this.x_accel = new ArrayList<Float>();
	this.y_accel = new ArrayList<Float>();
	this.z_accel = new ArrayList<Float>();
	this.x_gyro = new ArrayList<Float>();
	this.y_gyro = new ArrayList<Float>();
	this.z_gyro = new ArrayList<Float>();
	this.accel_times = new ArrayList<Long>();
	this.gyro_times = new ArrayList<Long>();
	this.input_signal.clear ();
}

public SensorData (String label) {
	this(label, 400);
}

public String getLabelName()
{
	return label_name;
}

public void setLabelName(String label)
{
	label_name = label;
}

// This method returns true if the number of samples collected across all of the lists has hit the sample number

public boolean checkSampleCount () {
	if (this.x_accel.size() >= this.sample_number && this.y_accel.size() >= this.sample_number &&
	    this.z_accel.size() >= this.sample_number && this.x_gyro.size() >= this.sample_number &&
	    this.y_gyro.size() >= this.sample_number && this.z_gyro.size() >= this.sample_number) {
		return true;
	} else {
		return false;
	}
}

// getter for input_signal list

public List<Float> getInputSignal () {
	return this.input_signal;
}

// add the x, y, z, and time values from an accelerometer event to the accelerometer list

public void addAccelEvent (Float x, Float y, Float z, Long time) {
	if (this.x_accel.size() < this.sample_number && this.y_accel.size() < this.sample_number &&
	    this.z_accel.size() < this.sample_number) {
		this.x_accel.add (x);
		this.y_accel.add (y); 
		this.z_accel.add (z);
		this.accel_times.add (time);
	}
	return;
}

// add the x, y, z, and time values from a gyroscope event to the gyroscope list

public void addGyroEvent (Float x, Float y, Float z, Long time) {
	if (this.x_gyro.size() <= this.sample_number &&
	    this.y_gyro.size() <= this.sample_number && this.z_gyro.size() <= this.sample_number) {
		this.x_gyro.add (x); 
		this.y_gyro.add (y); 
		this.z_gyro.add (z);
		this.gyro_times.add (time);
	}
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
	this.accel_times.clear();
	this.gyro_times.clear();
	this.input_signal.clear();
}

// setter for the sample number

public void setSampleNumber (int samples) {
	this.sample_number = samples;
}

public int getSampleNumber () {
	return this.sample_number;
}

// getters for each sensor data list

public List<Float> getXAccelerometerList () {
	return this.x_accel;
}

public List<Float> getYAccelerometerList () {
	return this.y_accel;
}

public List<Float> getZAccelerometerList () {
	return this.z_accel;
}

public List<Float> getXGyroscopeList () {
	return this.x_gyro;
}

public List<Float> getYGyroscopeList () {
	return this.y_gyro;
}

public List<Float> getZGyroscopeList () {
	return this.z_gyro;
}

// getters for accelerometer and gyroscope time lists

public List<Long> getGyroscopeTimeList () {
	return this.gyro_times;
}

public List<Long> getAccelerometerTimeList () {
	return this.accel_times;
}

// Save and load methods for non formated non-human readable storage

public void save (String filePath) {
	try {
		FileOutputStream fos = new FileOutputStream(filePath, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		outputStream.writeObject(this.sample_number);
		outputStream.writeObject(this.x_accel);
		outputStream.writeObject(this.y_accel);
		outputStream.writeObject(this.z_accel);
		outputStream.writeObject(this.x_gyro);
		outputStream.writeObject(this.y_gyro);
		outputStream.writeObject(this.z_gyro);
		outputStream.writeObject(this.accel_times);
		outputStream.writeObject(this.gyro_times);
		outputStream.writeObject(this.input_signal);
		outputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	}
	return;
}

public void load (String filePath) {
	try {
		FileInputStream fin = new FileInputStream(filePath);
		ObjectInputStream inputStream = new ObjectInputStream(fin);
		Integer sample_number = (Integer)inputStream.readObject();
		this.sample_number = sample_number.intValue();
		this.x_accel = (List<Float>)inputStream.readObject();
		this.y_accel = (List<Float>)inputStream.readObject();
		this.z_accel = (List<Float>)inputStream.readObject();
		this.x_gyro = (List<Float>)inputStream.readObject();
		this.y_gyro = (List<Float>)inputStream.readObject();
		this.z_gyro = (List<Float>)inputStream.readObject();
		this.accel_times = (List<Long>)inputStream.readObject();
		this.gyro_times = (List<Long>)inputStream.readObject();
		this.input_signal = (List<Float>)inputStream.readObject();
		inputStream.close();
	} catch (IOException ex) {
		System.err.println(ex);
	} catch (ClassNotFoundException ex) {
		System.err.println(ex);
	}
}

// saving and loading sensor data with existing output stream/input stream objects, useful for writing and reading from disc with other data objects


public void save (ObjectOutputStream outputStream) {
	try {
		outputStream.writeObject(this.sample_number);
		outputStream.writeObject(this.x_accel);
		outputStream.writeObject(this.y_accel);
		outputStream.writeObject(this.z_accel);
		outputStream.writeObject(this.x_gyro);
		outputStream.writeObject(this.y_gyro);
		outputStream.writeObject(this.z_gyro);
		outputStream.writeObject(this.accel_times);
		outputStream.writeObject(this.gyro_times);
		outputStream.writeObject(this.input_signal);
	} catch (IOException ex) {
		System.err.println(ex);
	}
	return;
}

public void load (ObjectInputStream inputStream) {
	try {
		Integer sample_number = (Integer)inputStream.readObject();
		this.sample_number = sample_number.intValue();
		this.x_accel = (List<Float>)inputStream.readObject();
		this.y_accel = (List<Float>)inputStream.readObject();
		this.z_accel = (List<Float>)inputStream.readObject();
		this.x_gyro = (List<Float>)inputStream.readObject();
		this.y_gyro = (List<Float>)inputStream.readObject();
		this.z_gyro = (List<Float>)inputStream.readObject();
		this.accel_times = (List<Long>)inputStream.readObject();
		this.gyro_times = (List<Long>)inputStream.readObject();
		this.input_signal = (List<Float>)inputStream.readObject();
	} catch (IOException ex) {
		System.err.println(ex);
	} catch (ClassNotFoundException ex) {
		System.err.println(ex);
	}
}

// Methods for saving and loading from CSV files
public boolean saveToCSV (String filePath) {
	try {
		FileOutputStream fos = new FileOutputStream(filePath, false);
		String charSet = "US-ASCII";
		fos.write(this.label_name.getBytes(charSet));
		fos.write(',');
		fos.write('\n');
		fos.write(Integer.valueOf(this.sample_number).toString().getBytes(charSet));
		fos.write(',');
		fos.write('\n');
		String columnHeaders = "row,x_accel,y_accel,z_accel,accel_time,x_gyro,y_gyro,z_gyro,gyro_time,\n";
		fos.write(columnHeaders.toString().getBytes(charSet));
		for (int i = 0; i < this.sample_number; ++i)
		{
			fos.write(Integer.valueOf(i+1).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.x_accel.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.y_accel.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.z_accel.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.accel_times.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.x_gyro.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.y_gyro.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.z_gyro.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write(this.gyro_times.get(i).toString().getBytes(charSet));
			fos.write(',');
			fos.write('\n');
		}
	} catch (IOException ex) {
		System.err.println(ex);
		return false;
	}
	return true;
}

public boolean loadFromCSV (String filePath) {
	try {
		Scanner scanner = new Scanner (new File(filePath));
		scanner.useDelimiter (",");

		String label = scanner.next();
		this.label_name = label;

		String sample_number = scanner.next ();
		Integer number = Integer.parseInt (sample_number.trim());
		this.sample_number = number.intValue ();

		String str;
		Float value;
		Long large;
		this.x_accel = new ArrayList<Float> ();
		this.y_accel = new ArrayList<Float> ();
		this.z_accel = new ArrayList<Float> ();
		this.accel_times = new ArrayList<Long> ();
		this.x_gyro = new ArrayList<Float> ();
		this.y_gyro = new ArrayList<Float> ();
		this.z_gyro = new ArrayList<Float> ();
		this.gyro_times = new ArrayList<Long> ();

		// Skip column headers
		for (int i = 0; i < 9; i++) {
			str = scanner.next();
		}
		for (int i = 0; i < number; i++) {
			str = scanner.next ();
			int row_num = Integer.parseInt (str.trim());
			if (row_num -1 != i)
				return false;
			str = scanner.next ();
			value = Float.parseFloat (str);
			this.x_accel.add (value);
			str = scanner.next ();
			value = Float.parseFloat (str);
			this.y_accel.add (value);
			str = scanner.next ();
			value = Float.parseFloat (str);
			this.z_accel.add (value);
			str = scanner.next ();
			large = Long.parseLong (str);
			this.accel_times.add (large);
			str = scanner.next ();
			value = Float.parseFloat (str);
			this.x_gyro.add (value);
			str = scanner.next ();
			value = Float.parseFloat (str);
			this.y_gyro.add (value);
			str = scanner.next ();
			value = Float.parseFloat (str);
			this.z_gyro.add (value);
			str = scanner.next ();
			large = Long.parseLong (str.trim());
			this.gyro_times.add (large);
		}
		this.input_signal = new ArrayList<Float> ();
		scanner.close ();
	}   catch (FileNotFoundException ex) {
		System.err.println(ex);
		return false;
	}
	return true;
}


// methods for getting lists of elapsed times in different time units depending on what units are best for graph visualisation
// All times start in units of nanoseconds, and each of these following methods convert to elapsed time since collection and also convert to larger
// time units before return the new list of Long values

public List<Long> getElapsedAccelerometerTimeInMicroseconds () {
	long start_time = this.accel_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 1000;
	for (int i = 1; i < this.accel_times.size(); i++) {
		temp_time = (this.accel_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

public List<Long> getElapsedGyroscopeTimeInMicroseconds () {
	long start_time = this.gyro_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 1000;
	for (int i = 1; i < this.gyro_times.size(); i++) {
		temp_time = (this.gyro_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

// get elapsed time in thousandths of seconds

public List<Long> getElapsedAccelerometerTimeInMilliseconds () {
	long start_time = this.accel_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 1000000;
	for (int i = 1; i < this.accel_times.size(); i++) {
		temp_time = (this.accel_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

public List<Long> getElapsedGyroscopeTimeInMilliseconds () {
	long start_time = this.gyro_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 1000000;
	for (int i = 1; i < this.gyro_times.size(); i++) {
		temp_time = (this.gyro_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

// get elapsed time in hundreths of seconds

public List<Long> getElapsedAccelerometerTimeInCentiseconds () {
	long start_time = this.accel_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 10000000;
	for (int i = 1; i < this.accel_times.size(); i++) {
		temp_time = (this.accel_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

public List<Long> getElapsedGyroscopeTimeInCentiseconds () {
	long start_time = this.gyro_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 10000000;
	for (int i = 1; i < this.gyro_times.size(); i++) {
		temp_time = (this.gyro_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

// get elapsed time in tenths of seconds

public List<Long> getElapsedAccelerometerTimeInDeciseconds () {
	long start_time = this.accel_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 100000000;
	for (int i = 1; i < this.accel_times.size(); i++) {
		temp_time = (this.accel_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

public List<Long> getElapsedGyroscopeTimeInDeciseconds () {
	long start_time = this.gyro_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 100000000;
	for (int i = 1; i < this.gyro_times.size(); i++) {
		temp_time = (this.gyro_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

// get elapsed time in seconds

public List<Long> getElapsedAccelerometerTimeInSeconds () {
	long start_time = this.accel_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 1000000000;
	for (int i = 1; i < this.accel_times.size(); i++) {
		temp_time = (this.accel_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

public List<Long> getElapsedGyroscopeTimeInSeconds () {
	long start_time = this.gyro_times.get (0);
	List<Long> converted_times = new ArrayList<Long> ();
	converted_times.add (new Long (0));
	long temp_time;
	long conversion_factor = 1000000000;
	for (int i = 1; i < this.gyro_times.size(); i++) {
		temp_time = (this.gyro_times.get (i) - start_time);
		converted_times.add (new Long ((long) (temp_time / conversion_factor)));
	}
	return converted_times;
}

// May need to add more methods later as features are introduced 
}