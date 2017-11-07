from PIL import Image
import json
import base64
import os



"""
Class      : ImageClass
This class starts the session tensorflow model and runs the session
"""


class ImageClass:
    def __init__(self, fileData):
        self.image = self.create_image_from_file(fileData)
        self.imageName = fileData.filename
        self.imageScore = None
        self.foodBoolean =  None

    def create_image_from_base64(self, stringBase64):
        self.image = self.create_image(stringBase64)

    def create_image(self, stringBase64):
        return base64.decodestring(stringBase64)

        # Not sure about useing linear interpolation in a 2x2 environment
        # #https://pillow.readthedocs.io/en/4.3.x/handbook/concepts.html#concept-modes
    def create_image_from_file(self, fileData):
        image = Image.open(fileData)
        image = image.convert('RGB')

        return image.resize((227, 227), Image.BILINEAR)

    # + saveFullSizeToDB(): boolean
    # + saveThumbToDB(): boolean
    # + createThumbnail(): boolean

    def get_image(self):
        return self.image

    def get_imageName(self):
        return self.imageName

    def get_imageScore(self):
        return self.imageScore

    def get_foodBoolean(self):
        return self.foodBoolean

    def set_imageScore(self, score ):
        self.imageScore = score

    def set_foodBoolean(self, booleanValue):
        self.foodBoolean = booleanValue