import os
import numpy as np
import argparse
import tensorflow as tf
from PIL import Image

"""
Class      : AIModel
This class starts the session tensorflow model and runs the session
"""


class AIModel:
    def __init__(self, ):
        self.sessionModel = None
        self.x_input = None
        self.keep_prob = None
        self.class_scores = None
        self.graph = None
        self.start_model()

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

    def getModel(self):
        return self
    def setModel(self, ob):
        self = ob

    def evaluation(self, image):
        tensorImage = [np.asarray(image, dtype=np.float32)]
        scores = self.sessionModel.run(self.class_scores, {self.x_input: tensorImage, self.keep_prob: 1.})
        return scores.tolist()

    def is_food(self, file_score):
        if np.argmax(file_score) == 1:
            # No Food
            return 0
        else:
            # Has Food
            return 1
        return
