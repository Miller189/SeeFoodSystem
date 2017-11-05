from PIL import Image
import json
import base64
import os
import AIModel

"""
Class      : ImageClass
This class starts the session tensorflow model and runs the session
"""


class ImageClass:
    _image = None
    _imageName = ""
    _imageScore = None
    _foodBoolean = None
    _AIModelOb = AIModel()

    def __int__(self, fileData):
        _image = self.create_image_from_file(fileData)
        _imageName = fileData.filename
        _imageScore = self._AIModelOb.run_session_model(_image)
        _foodBoolean = self._AIModelOb.is_food(_imageScore)


    @classmethod
    def create_image_from_base64(self, stringBase64):
        _image = self.create_image(stringBase64)

    @classmethod
    def create_image(stringBase64):
        return base64.decodestring(stringBase64)

    @classmethod
    def create_image_from_file(self, fileData):
        image = Image.open(fileData)
        image = image.convert('RGB')
        # Not sure about useing linear interpolation in a 2x2 environment
        # #https://pillow.readthedocs.io/en/4.3.x/handbook/concepts.html#concept-modes
        return image.resize((227, 227), Image.BILINEAR)

    # + saveFullSizeToDB(): boolean
    # + saveThumbToDB(): boolean
    # + createThumbnail(): boolean
    @classmethod
    def getImage(self):
        return self._image

    @classmethod
    def getName(self):
        return self._imageName

    @classmethod
    def setName(self, imageName):
        _imageName = imageName

    @classmethod
    def setScore(self, score):
        _imageScore = score

    @classmethod
    def getScore(self):
        return self._imageScore

    @classmethod
    def setFoodBoolean(isFood):
        _foodBoolean = isFood

    @classmethod
    def getFoodBoolean(self):
        return self._foodBoolean

