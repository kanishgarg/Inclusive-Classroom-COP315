# import the necessary packages
import numpy as np
import argparse
import cv2
import time

start = time.time()
# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required=True,
                help="path to input image")
# ap.add_argument("-p", "--prototxt", required=True,
#                 help="path to Caffe 'deploy' prototxt file")
# ap.add_argument("-m", "--model", required=True,
#                 help="path to Caffe pre-trained model")
# ap.add_argument("-c", "--confidence", type=float, default=0.2,
#                 help="minimum probability to filter weak detections")
ap.add_argument("-n", "--cycle", required=True,
                help="process cycle")
args = vars(ap.parse_args())

# initialize the list of class labels MobileNet SSD was trained to
# detect, then generate a set of bounding box colors for each class
CLASSES = ["background", "aeroplane", "bicycle", "bird", "boat",
           "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
           "dog", "horse", "motorbike", "person", "pottedplant", "sheep",
           "sofa", "train", "tvmonitor"]
COLORS = np.random.uniform(0, 255, size=(len(CLASSES), 3))

# load our serialized model from disk
# print("[INFO] loading model...")
net = cv2.dnn.readNetFromCaffe("./pyimagesearch/MobileNetSSD_deploy.prototxt.txt", "./pyimagesearch/MobileNetSSD_deploy.caffemodel") #############

# load the input image and construct an input blob for the image
# by resizing to a fixed 300x300 pixels and then normalizing it
# (note: normalization is done via the authors of the MobileNet SSD
# implementation)

# cv2.namedWindow("Output", cv2.WINDOW_NORMAL)
# cv2.namedWindow("Cropped", cv2.WINDOW_NORMAL)
image = cv2.imread(args["image"])
(h, w) = image.shape[:2]
blob = cv2.dnn.blobFromImage(cv2.resize(image, (300, 300)), 0.007843, (300, 300), 127.5)

# pass the blob through the network and obtain the detections and
# predictions
# print("[INFO] computing object detections...")
net.setInput(blob)
detections = net.forward()

# loop over the detections
for i in np.arange(0, detections.shape[2]):
    # extract the confidence (i.e., probability) associated with the
    # prediction
    confidence = detections[0, 0, i, 2]

    # filter out weak detections by ensuring the `confidence` is
    # greater than the minimum confidence
    if confidence > 0.2:
        # extract the index of the class label from the `detections`,
        # then compute the (x, y)-coordinates of the bounding box for
        # the object
        idx = int(detections[0, 0, i, 1])
        box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
        (startX, startY, endX, endY) = box.astype("int")
        # display the prediction
        label = "{}: {:.2f}%".format(CLASSES[idx], confidence * 100)
        # print("[INFO] {}".format(label))
        # cv2.rectangle(image, (startX, startY), (endX, endY),
        #               COLORS[idx], 2)
        # y = startY - 15 if startY - 15 > 15 else startY + 15
        # cv2.putText(image, label, (startX, y),
        #             cv2.FONT_HERSHEY_SIMPLEX, 0.5, COLORS[idx], 2)

if 'person' in label:
	startX = startX - 50
	startY = startY - 50
	endY = endY + 50
	endX = endX + 50

	if startX < 0:
	    startX = 0
	if endX > w:
	    endX = w - 1
	if startY < 0:
	    startY = 0
	if endY > h:
	    endY = h - 1

	I = []
	
	print(startX, endX, startY, endY, h, w)
	print("cycle: " + args["cycle"])
	if args["cycle"] == "0":
    	for i in range(startX, endX):
    	    for j in range(startY, endY):
    		image[j][i] = 255
        #cv2.imwrite("./pics/imagewhite_" + args["cycle"] + ".jpg", image)
	if args["cycle"] != 0:
	    prev_image_path = "./pics/image" + str(int(args["cycle"]) -1) + ".jpg" #prev merged image path
	    curr_image_path = args["image"]

	    prev_image = cv2.imread(prev_image_path)
	    curr_image = cv2.imread(curr_image_path)

	    roi = prev_image[startY:endY, startX:endX]
	    image[startY:endY, startX:endX] = roi

	# cv2.imshow('image', image)

grayImage = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
bwImage2 = cv2.adaptiveThreshold(grayImage,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,7)

object = open("rec_coord.txt", "r");
startX = int(object.readline());
endX = int(object.readline());
startY = int(object.readline());
endY = int(object.readline());
object.close();
# image = image[startY:endY, startX:endX];
bwimage2 = bwImage2[startY:endY, startX:endX];
cv2.imwrite(args["image"], image)
cv2.imwrite("./pics/image_bw_" + args["cycle"] + ".jpg", bwImage2)
# cv2.imshow(image)
# send this image
object = open("./pics/file.txt", 'w+')
str = "http://192.168.10.1:7000/" + "image_bw_" + args["cycle"] + ".jpg"
object.write(str)
object.close()

'''for i in range(2,4):
    I.append(cv2.imread("image" + str(i) + ".jpg"))

for i in range(startX, endX):
    for j in range(startY, endY):
        for k in range(0,2):
            if not np.array_equal(I[k][j][i], [255,255,255]):
                image[j][i] = I[k][j][i]
                break
'''
# if startX < w - endX :
# 	cropped = image[0:h, endX:w]
# else :
# 	cropped = image[0:h, 0:startX]

# cv2.imshow("Output", image)
# cv2.imwrite("image.jpg", image)
print(time.time() - start)
# cv2.imshow("Cropped", cropped)
# cv2.imwrite("crop.jpg", cropped)
