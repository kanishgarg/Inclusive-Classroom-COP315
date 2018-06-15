import os
import time
import cv2
import argparse

# construct the argument parser and parse the arguments
''' ap = argparse.ArgumentParser()
ap.add_argument("-n", "--frames", required=True,
                help="no. of frames to be captured")
args = vars(ap.parse_args())

FRAMES = int(args["frames"])
TIMEBETWEEN = 10
print(FRAMES)
frameCount = 0
while frameCount < FRAMES:
    imageNumber = str(frameCount).zfill(2) '''

os.system("fswebcam -r 1280x768 ./pics/image1.jpg");
os.system("killall fswebcam");
I = cv2.imread("./pics/image1.jpg");
os.system("python processImage.py --image ./pics/image1.jpg");
i = 2

'''object = open("rec_coord.txt", 'r');
startX = int(object.readline());
endX = int(object.readline());
startY = int(object.readline());
endY = int(object.readline());
object.close();
edit = I[startY:endY, startX:endX]
'''
# cv2.imwrite("./pics/image1.jpg", I)

os.system("python detectObject.py --image ./pics/image1.jpg --cycle 0")


while(1):
    os.system("fswebcam -r 1280x768 ./pics/image" + str(i) + ".jpg")
    os.system("killall fswebcam")
    # I = cv2.imread("./pics/image1.jpg")
  #  edit = I[startY:endY, startX:endX]
    # cv2.imwrite("./pics/image1.jpg", I)
    os.system("python detectObject.py --image ./pics/image" + str(i) + ".jpg --cycle " + str(i))
    if i>5:
        os.system("rm ./pics/image"+str(i-5)+".jpg")
    i = i + 1


