import numpy as np
#import tensorflow as tf


"""
Class      : AIModel
This class starts the session tensorflow model and runs the session
"""
class AIModel:
    def __init__(self):
        self.sessionModel = None
        self.x_input = None
        self.keep_prob = None
        self.class_scores = None
        self.graph = None
        self.start_model()

    def getModel(self):
        return self


    def setModel(self, ob):
        self = ob

    """
    Method      : start_model
    Parameters  : self
    Return      : None
    This method starts the session model. 
    It should only be cold once when the start screen method is called
    """
    def start_model(self):
        self.sessionModel = tf.Session()
        saver_def = tf.train.import_meta_graph(
            '/var/www/seefood/seefood-core-ai/saved_model/model_epoch5.ckpt.meta')
        saver_def.restore(self.sessionModel,
                          tf.train.latest_checkpoint('/var/www/seefood/seefood-core-ai/saved_model/'))
        self.graph = tf.get_default_graph()
        self.x_input = self.graph.get_tensor_by_name('Input_xn/Placeholder:0')
        self.keep_prob = self.graph.get_tensor_by_name('Placeholder:0')
        self.class_scores = self.graph.get_tensor_by_name("fc8/fc8:0")

    """
       Method      : evaluation
       Parameters  : self, image
       Return      : list
        This method takes an image and evaluates whether or not it contains food
    """
    def evaluation(self, image):
        tensorImage = [np.asarray(image, dtype=np.float32)]
        scores = self.sessionModel.run(self.class_scores, {self.x_input: tensorImage, self.keep_prob: 1.})
        return scores.tolist()

    """
    Method      : is_food
    Parameters  : self, file_score
    Return      : Boolean
    This method checks to see whether or not the file contains food
    """
    def is_food(self, file_score):
        if np.argmax(file_score) == 1:
            # No Food
            return 0
        else:
            # Has Food
            return 1
        return
