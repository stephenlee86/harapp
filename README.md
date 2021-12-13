# On-device Learning of Activity Recognition Networks
#### Personalized machine learning on the smartphone

The aim of the project is to provide an end-to-end solution for on-device training, inference and data collection for activity recognition based on <a href="https://github.com/tensorflow/examples/tree/master/lite/examples/model_personalization">TFlite Transfer Learning Pipeline</a>. The corresponding blog post is available <a href="https://aqibsaeed.github.io/on-device-activity-recognition">here</a>. 

#### Tools Required
* [Python 3.5+](https://www.python.org)
* [Tensorflow 2.0.0rc0](https://www.tensorflow.org)
* [Numpy](https://numpy.org/)
* [Pillow](https://pypi.org/project/Pillow/)
* [Scipy](https://scipy.org)
* [Android Studio](https://developer.android.com/studio/install)

#### Getting Started

Begin by installing Android Studio for your device and download the source code from this repository. For all other softwares and APIs, read appropriate documentation linked above to get a base level of understanding for this project.

#### Using Android Studio

Below are steps to follow to set up Android Studio Environment
1. Download Android Studio
2. Launch Android Studio
3. Click "Open Folder"
    * Ensure you open the folder from "android" in the project tree 
4. To build the application, Navigate to Build -> Make Project
5. To run the application, Navigate to Run -> Run '$NAME'
    * Running the application can be accomplished through the use of an Android Virtual Device (AVD) or a physical device.

#### Establishing an AVD

1. Navigate to Tools -> AVD Manager
2. Select "Create Virtual Device"
3. Select desired Android Device and click "Next"
4. Select desired Android build for device and click "Next"
5. Modify advanced settings as necessary, then click "Finish"

#### Using App on mobile device Over USB
1. Ensure that the phone's developers mode is activated
2. download the  appropriate driver in your phone to allow UDB debugging
   *provide more link to specific manufactures, link to google sites, samsung package
3.*edit AVD environment
Wireless debugging, provide links and provide commands to configure over network

### Network data extraction
*give what the app provides

#### Project Tree Structure Overview

* All Java Classes should be placed in "harapp/android/app/src/main/java/org/tensorflow/lite/examples/transfer/"
* The Android Manifest is located in "harapp/android/app/src/main/"
* All files pertaining to TensorFlow Lite Transfer are located within "harapp/tfltransfer/"
* All resources used for XML files (strings, styles, colors etc.) are located in their respective folders at "harapp/android/app/src/main/res/"

### Features of current version

* The project consists of a main activity, sample view activity, two graph view activities, and a number of controls and popups. 
* The Main activity has a spinner object (commonly known as a dropdown), for selecting which label to use during sample collection as well as specifying which mode the main activity should be in. 
* The main activity has three modes: collection, training, and inference. 
* Collection only collects the samples, tracks the label for each sample,  and assigns an automatically generated name to those collected samples by label, by the mode the sample was collected during, and also by a generated number to distinguish many samples of the same type. 
* During training mode the same steps as collection are followed; however, the sample collected will also be used to train the activity recognition model on the device. 
* The inference mode also follows the same steps as collection when it collects samples; however, inference mode will take those collected samples and immediately perform inference with them. 
* In all three modes samples are written to disc under their sample names as soon as they are collected. 
* Samples are represented in the software as SensorData objects with sample sizes; meaning the number of x, y, z, and time values collected for accelerometer and gyroscope readings over some time series, where a complete sample will contain 400 readings (this is currently hardcoded).
* Each SensorData object has eight lists; x, y, z, and time readings from the accelerometer, and x, y, z, and time for the gyroscope.
* X, y, and z values are stored as Float objects, while times are stored as Long objects, since the Android API provides the time stamps of sensor reading events as amounts of time in microseconds. 
* The application currently uses the fastest rate of sensor reading possible by default. 
* All samples are stored in CSV files as of the current version. 
* The application metadata is written to a ‘.dat’ file on disc in order to capture what samples are currently written to disc, and their corresponding labels. 
* The metadata for the application is stored in a DataModels object, and information about the current configuration of the application is stored in ActivityRecognizer objects. 
* The two graph view activities support graphing of samples, one graph view displays accelerometer and the other displays gyroscope data plots.
* A recycler view is used in the sample view activity to view all collected samples that have been successfully written to disc and have not yet been deleted. 
* The sample view activity supports selecting samples for displaying graphs, deleting samples, deleting all samples, renaming samples, marking samples as inference, and marking samples as training. 
* Current limitation: the user cannot currently invoke training or inference on samples from the sample view screen. 
* This poses an issue since the user cannot perform training or inference after the samples were collected. So the user can only view graphs of samples and rename or throw out samples after training and inference has already been performed. 
* Training and inference is currently performed in the process input function of the main activity class. 
* The process input function should be move to the ActivityRecognitionApplication class so that it can be invoked from multiple activities and update the DataModels object which holds the metadata for the application. 
* Any method that needs to be invoked from multiple activities should be placed in the ActivityRecognitionApplication class. 
* The methods for saving and loading SensorData objects and DataModels objects to and from disk are in the ActivityRecognitionApplication class so that the applications data can be saved and loaded from disc as needed from any activity. 
* All CSVs and ‘.dat’ files should be able to be opened from the application’s directory on disc when run on an Android emulator or when run on an Android device. 
* The CSVs have the sample data in a human readable format with the sample size, label type, column names, and row numbers
* The ‘.dat’ files for the application metadata are not human readable
* Before running the application on your own machine you will want to personally reconvert the tensorflow lite on device model, to make sure that none of the tensorflow lite files are corrupted. 
* This can be done using an already trained tensorflow model that uses accel and gyro data for human activity recognition, or this can be done by training a tensorflow model from scratch using gyro and accel data from this project or from many other projects. 
* The python code available in this source tree can be used as a starting point for converting your own tensor flow lite models. 
* Once converted properly the tensor flow lite model can be accessed through the transfer learning model wrapper class. 
* The transfer learning model wrapper class allows for the model to be loaded, saved, trained, and to be used for predictions from the Android application code
* In order to add more dependencies, the Android manifest file will need to be modified.
* To make changes to future builds the build.gradle file will need to be updated (Gradle is the build configuration tool used by android studio).
* The proguard file may also need to be altered for adding certain future dependencies .
* The res directory holds all the XML for the project
* For any additional controls or views XML will need to be added, or already existing XML can be modified for adding those new objects
* Android studio supports generating XML through its UI; however, in certain situations its UI acts up, and it saves time to finish modifying the XML by hand. 
* If Tensor flow lite is seg faulting because of a corrupted tensor flow lite file, comment out the line in MainActivity.java that creates the transfer learning model wrapper object, and instead set the reference to null.
* This way the application will run until a new tensor flow lite model is able to be properly converted; however, the application will perform no training or inference on samples even if it is in training or inference mode. 

#### Dataset 
* This will be useful if you need to retrain a model from scratch for proper conversion to a tensor flow lite model
The <a href="https://archive.ics.uci.edu/ml/datasets/Heterogeneity+Activity+Recognition">Heterogeneity Activity Recognition dataset</a> is used for model pretraining. If you use this in your research, please cite their work and check the license. 
* Note that this data set will need some amount of preprocessing before the code provided in this project tree can be ran
* The Pandas documentation can be consulted for how to do this, as well as the Numpy documentation

#### Citing
If you find this project useful, please cite it as:

<pre>@misc{saeed2020recognition, 
  author = {Saeed, Aaqib},
  title = {On-device Learning of Activity Recognition Networks},
  year = {2020},
  journal = {aqibsaeed.github.io},
  url = {\url{https://gitHub.com/aqibsaeed/on-device-activity-recognition}}
}</pre>
