# On-device Learning of Activity Recognition Networks
#### Personalized machine learning on the smartphone

The aim of the project is to provide an end-to-end solution for on-device training, inference and data collection for activity recongition based on <a href="https://github.com/tensorflow/examples/tree/master/lite/examples/model_personalization">TFlite Transfer Learning Pipeline</a>. The corresponding blog post is available <a href="https://aqibsaeed.github.io/on-device-activity-recognition">here</a>. 

#### Tools Required
* [Python 3.5+](https://www.python.org)
* [Tensorflow 2.0.0rc0](https://www.tensorflow.org)
* [Numpy](https://numpy.org/)
* [Pillow](https://pypi.org/project/Pillow/)
* [Scipy](https://scipy.org)
* [Android Studio](https://developer.android.com/studio/install)

#### Getting Started

Begin by installing Android Studio for your device and download the source code from this repositority. For all other softwares and APIs, read appropriate documentation linked above to get a base level of understanding for this project.

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
1. Ensure that the phone's developers mode is activited
2. download the  appriopraite driver in your phone to allow UDB debugging
   *provide more link to specific manufactures, link to google sites, samsung package
3.*edit AVD envirmoent
Wirless debugging, provide links and provide commands to congigure over network

### Network data extraction
*give what the app provides

#### Project Tree Structure Overview

* All Java Classes should be placed in "harapp/android/app/src/main/java/org/tensorflow/lite/examples/transfer/"
* The Android Manifest in located in "harapp/android/app/src/main/"
* All files pertaining to TensorFlow Lite Tranfer are located within "harapp/tfltransfer/"
* All resources used for XML files (strings, styles, colors etc.) are located in their respective folders at "harapp/android/app/src/main/res/"

#### Dataset 
The <a href="https://archive.ics.uci.edu/ml/datasets/Heterogeneity+Activity+Recognition">Heterogeneity Activity Recognition dataset</a> is used for model pretraining. If you use this in your research, please cite their work and check the license. 

#### Citing
If you find this project usefuly, please cite it as:

<pre>@misc{saeed2020recognition, 
  author = {Saeed, Aaqib},
  title = {On-device Learning of Activity Recognition Networks},
  year = {2020},
  journal = {aqibsaeed.github.io},
  url = {\url{https://gitHub.com/aqibsaeed/on-device-activity-recognition}}
}</pre>
