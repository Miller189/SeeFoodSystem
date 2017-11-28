from PIL import Image
import base64
import os
import time

"""
Class      : ImageClass
This class starts the session tensorflow model and runs the session
"""
class ImageClass:
    def __init__(self, fileData):
        self.image = self.create_image_from_file(fileData)
        self.imageName = self.new_name_on_time(fileData.filename)
        self.foodBoolean = None
        self.imageScore = None
        self.fullSizedImagePathName = ""
        self.thumbnailImagePathName = ""
        self.save_full_size_image()
        self.create_thumbnail_57_57(fileData)

    """
       Method      : new_name_on_time
       Parameters  : self, oldName
       Return      : new Image name
       This method is used to rename the image
    """
    def new_name_on_time(self, oldName):
        fileBaseName = os.path.basename(oldName)
        currentTimeObjectMade = time.strftime("%Y%m%d-%H%M%S")
        fileBaseNameNoEx = os.path.splitext(fileBaseName)[0]
        return (fileBaseNameNoEx + "-" + currentTimeObjectMade )

    """
       Method      : create_image_from_base64
       Parameters  : self, stringBase64
       Return      : None
       This method is used to create an image based off a 64 bit string
    """
    def create_image_from_base64(self, stringBase64):
        self.image = self.create_image(stringBase64)

    """
         Method      : create_image
         Parameters  : self, stringBase64
         Return      : None
         This method is used to create an image based off a 64 bit string
    """

    def create_image(self, stringBase64):
        return base64.decodestring(stringBase64)

        # Not sure about useing linear interpolation in a 2x2 environment
        # #https://pillow.readthedocs.io/en/4.3.x/handbook/concepts.html#concept-modes
    """
        Method      : create_image_from_file
        Parameters  : self, fileData
        Return      : image
        This method creates an image
    """

    def create_image_from_file(self, fileData):
        try:
            image = Image.open(fileData)
            image = image.convert('RGB')
            return image.resize((227, 227), Image.BILINEAR)
        except IOError:
            pass

    """
        Method      : save_full_size_image
        Parameters  : self, fileData
        Return      : none
        This method saves the image to the folder on the server
    """

    def save_full_size_image(self):
        path_name = os.path.join('/var/www/seefood/image/full_size_image/', str(self.imageName+".png"))
        self.set_fullSizedImagePathName(path_name)
        self.image.save(path_name, "PNG")
        os.chmod(path_name, 0777)


    """
       Method      : save_thumbnail
       Parameters  : self, image
       Return      : none
       This method saves the image to the folder on the server.
    """
    def save_thumbnail(self,image):
        path_name = os.path.join('/var/www/seefood/image/thumbnail/', str(self.imageName+".thumbnail"))
        self.set_thumbnailImagePathName(path_name)
        image.save(path_name, "PNG")
        os.chmod(path_name, 0777)

    """
          Method      : create_thumbnail_57_57
          Parameters  : self, fileData
          Return      : none
          The method creats a thumbnail and the call the save_thumbnail method
    """
    def create_thumbnail_57_57(self,fileData):
        image = Image.open(fileData)
        size = 57, 57
        image.thumbnail(size)
        self.save_thumbnail(image)


    """
        Method      : Getter and setters
    """
    def get_image(self):
        return self.image

    def get_imageName(self):
        return self.imageName

    def get_imageScore(self):
        return self.imageScore

    def get_foodBoolean(self):
        return self.foodBoolean

    def set_imageScore(self,score):
        self.imageScore = score

    def set_foodBoolean(self, booleanValue):
        self.foodBoolean = booleanValue

    def set_fullSizedImagePathName(self, path):
        self.fullSizedImagePathName = path

    def set_thumbnailImagePathName(self, path):
        self.thumbnailImagePathName = path

    def get_fullSizedImagePathName(self):
        return self.fullSizedImagePathName

    def get_thumbnailImagePathName(self):
        return self.thumbnailImagePathName
