# Inclusive-Classroom

### Requirements on pi -
- [Ubuntu Mate](https://www.raspberrypi.org/magpi/install-ubuntu-mate-raspberry-pi/)
- fswebcam
- Python - 2.7
- numpy
- scikit-learn
- imutils
- [OpenCV - 3.4.1](http://blog.youapp.co/raspberry/2018/03/11/Install-OpenCV-on-raspberry-pi/) (Yes, you have to build it from source)

### captureImage.py

Captures the images at regular intervals. The first image is processed to automatically detect the edges of the blackboard and save the coordinates of the rectangle for cropping images to get only the relevant part.


### detectObject.py

Detects the professor in each image and draws a bounding box around him and then gets the information of that part of the image from previous images so that the final image that is recieved by the client is an image that contains only the relevant part.

### processImage.py

Processes the first image to automatically detect the edges of the blackboard and then stores the rectangular coordinates of the contour in a text file for further use.

### Usage
- Clone the repository on pi and navigate to pi folder.
- Create a folder named pics in your pi folder and then type **python -m SimpleHTTPServer 7000** in the terminal. This will start a python server on port 7000.
- Type the command **python captureImage.py** in the pi directory and it will do all for you.
- Open the app in your mobile phone and you will start recieving the images.

##### Syntax

```
      git clone https://github.com/harshitjain1371999/Inclusive-Classroom.git
      cd Inclusive-Classroom/pi
      mkdir pics
      cd pics
      python -m SimpleHTTPServer 7000 &
      cd ..
      python captureImage.py
```
