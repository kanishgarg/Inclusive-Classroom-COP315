# Inclusive-Classroom

### Requirements on pi -
- Ubuntu Mate
- fswebcam
- Python - 2.7
- numpy
- scikit-learn
- imutils
- OpenCV - 3.4.1 (Yes, you have to build it from source)

## Setting up OS in Raspberry Pi
First, we installed Raspbian OS but to meet the 802.1x security requirements and other minor issues, we later switched to Ubuntu MATE.

## Attaching camera to Raspberry pi
We are using USB Webcam to capture the images. Currently, it's resolution is quite low, so we are looking to upgrade to a better camera.

## Issues -

1) **Internet connection required to download the libraries**

~~Need to connect to the internet to download the required dependencies to operate the camera. We are facing a problem in this since the hostel LAN connection uses 802.1X authentication. To configure the Raspberry Pi, we need the config files from the institute.~~

We switched from pi-2 to pi-3 as wifi functionality is inbuilt in pi-3.

2) **OpenCV Installation**

We couldn't find compiled files for opencv so we had to build it from source.

3) **Contour Detection problem**

Using canny edge detection, the result was not satisfactory so we had to switch to Otsu's Threshold Algorithm for finding the threshold to convert grayscale to binary image.

4) **Image Size** 

First, we planned to use JPEG standard algorithm as it used efficient encoding techniques but on searching the web, we learned that jpeg uses 8 bits per pixel even for saving black and white image while png uses a single bit per pixel.

5)**Professor Detection** 

One of the most interesting part of our project was to detect the professor blocking and to extract the info that the professor is blocking. We used the mobilenet SSD model developed by google for this problem. The model to detects the professor and after detection we create a bounding box around the area which the professor blocks.


6)**Obstructed data Recovery**

After drawing the bounding box that area is cropped out of the picture. After that, we look back to a few previous picture if the area blocked in this picture was not blocked before. Then basically, we merge the contents of that obstructed data in the current pic with the contents available in the previous pic. This merged pic is the image which has to a certain extent recovered data.

7) **Image converted to Binary**

The image after this is converted to binary so that it can be transmitted to the android devices.

8) **HTTP Server**

Our initial plan was to broadcast the images using UDP. But, on testing we found that the packet loss was too high to be acceptable. The next option we looked at was Multicast, but it was too slow on a wireless network. Finally we decided to run python's ***SimpleHTTPServer***,
since it required minimal effort to be setup and was reliable.


## Android App Development 

We had to develop a very user friendly Android App. The purpose of the android app is to receive the prcocessed pictures of the board and display it to the user. The UI of the app is simple so that any person can use it easily.
Moreover, the app is equipped with various functionalities such as -
1. There  is an option to change the colour of the text and bachground and hence  change the contrast of the picture. This functionality is of great use for people with eye defects like colour blindness who, otherwise might not have been able to interpret what was written.
2. We can zoom in and zoom out of the pictures.
3. We can save the images for future reference. 

## Tasks
- [x] Hardware procurement - (Webcam, Raspberry pi, MicroSD card ..) - 22/01/2018
- [x] Setup OS in Raspberry - 23/01/2018
- [x] Setup Camera to capture photos - 12/02/2018
- [x] Image Processing - 01/03/2018
- [x] Detecting Professor - 14/03/2018
- [x] Setting up the network to share images - 26/02/2018
- [x] Layout and Activities of the app - 15/02/2018
- [x] Incorporating functionalities to view image in different color spaces, add annotations etc. - 16/03/2018
- [x] BackEnd part including the directory structure and all of the images. - 28/03/2018
- [x] Recieving images from the pi and viewing them. - 04/04/2018
- [x] Setting up the power supply for the pi. - 12/04/2018
- [x] Final module case and mounting that case in a classroom. - 16/04/2018


## Scope for Improvements
1. Multiple person detection - Our device fails when multiple people are present in a scene. We can extend our program to detect multiple people and do process as required.

2. Calibration -   Currently we are just doing edge detection. The results can be really improved by doing perspective transformation as well. But the problem with it is it requires more computation power.

3. Installation - The camera body is not rigid at hinges. The angle contained by the hinge changes and as a consequence the view of field of the camera changes. This can be avoided by using a more rigid and robust camera body.

4. Faster processor than that provided by Raspberry pi - Currently, RPi takes about 10 seconds to process the image completely. By using a faster computing device, we can significantly reduce the time.

5. Master Android App - This is an android app which can be made in future so that the professor has more control of the system. For example he can regulate what students can view, save on their devices. Moreover sometimes the camera gets disturbed because of which recalibration needs to be done. This app can then have a program which can recalibrate the system. 

## References : 

[Raspberry Pi](https://www.raspberrypi.org)                                        
[OpenCV](https://opencv.org/)                                               
[Image Processing](https://www.pyimagesearch.com/2014/09/01/build-kick-ass-mobile-document-scanner-just-5-minutes/)
