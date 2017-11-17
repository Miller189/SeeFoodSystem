import argparse
import os
import numpy as np
import tensorflow as tf
from PIL import Image

"""
Class      : AIModel
This class starts the session tensorflow model and runs the session
"""


class AIModel:
    _sessionModel = None
    _graph = None

    # constructor
    def __int__(self):
        _sessionModel = self.start_session()
        _graph = self.start_model(_sessionModel)

    def start_session(self):
        return self.tf.Session()

    def start_model(self, sessionModel):
        sessionModel = tf.Session()
        saver_def = tf.train.import_meta_graph(
            '/var/www/seefood/seefood-core-ai/saved_model/model_epoch5.ckpt.meta')
        saver_def.restore(sessionModel, tf.train.latest_checkpoint('/var/www/seefood/seefood-core-ai/saved_model/'))
        graph = tf.get_default_graph()
        return graph

    def run_session_model(self, image):
        x_input = self._graph.get_tensor_by_name('Input_xn/Placeholder:0')
        keep_prob = self._graph.get_tensor_by_name('Placeholder:0')
        class_scores = self._graph.get_tensor_by_name("fc8/fc8:0")
        return self._sessionModel.run(class_scores, {x_input: image, keep_prob: 1.})

    def is_food(file_score):
        if np.argmax(file_score) == 1:
            # No Food
            return 0
        else:
            # Has Food
            return 1
        return
