import os
from flask import Flask, request, json, jsonify, redirect, url_for
import numpy as np
import base64
from PIL import Image
import argparse
import tensorflow as tf

# Used to set file exe types
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'jfif', 'gif'])
app = Flask(__name__)

"""
Method      : allowed_file
Parameters  : filename(string)
Return      : Boolean
This method takes a file name and parses out the file extension.
If the file extension is in theAllowed_Extensions the system returns true
Otherwise it returns false
"""
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


"""
Method      : image_evaluation
Parameters  : None
Return      : JSON Table
This method is executed every time the server receives a POST request for an image evaluation.
This method can receive one or many files
Each file is evaluated to ensure that the file extension meets the server criteria
Each File is converted to a image and every image gets sent to the API system for evaluation
The API returns a score for each image
Then this method creates a JSON  dump of the score and image metadata
"""
@app.route('/evaluation', methods=['POST'])
def image_evaluation():
    dumpFile = list()
    if 'file' in request.files:
        manyFiles = request.files.getlist("file")
        for item in manyFiles:
            Datafile = item
            if Datafile.filename == '':
                return "error!"
            if Datafile and allowed_file(Datafile.filename):
                # image will need to get saved and changed to thumbnail
                image = resolution_and_color_optimization(Datafile)
                tensorImage = [np.asarray(image, dtype=np.float32)]

                # Items will be dumbed into json table
                file_name = Datafile.filename
                file_score = load_model(tensorImage) # file_score holds a list.  May want to change based off of client (not Yet)
                food_boolean = is_food(file_score)
                # add list item to dumpFile
                dumpFile.append(JSON_dumpEvaluation(file_name, file_score, food_boolean))
        # endLoop
        json_string = json.dumps(dumpFile)
    return json_string

"""
Method      : JSON_dumpEvaluation
Parameters  : file_name(string), file_score(list), food_boolean(boolean(1,0))
Return      : list
This method creates a list
"""
def JSON_dumpEvaluation(file_name, file_score, food_boolean):
    data = []
    data.append({
        'file_name': file_name,
        'file_score': file_score,
        'food_boolean': food_boolean})

    return data


"""
Method      : is_food
Parameters  : file_score(array like Input)
Return      : Boolean
This method sees if this file_score evaluates to having food or not having food
"""
def is_food(file_score):
    if np.argmax(file_score) == 1:
        # No Food
        return 0
    else:
        # Has Food
        return 1
    return


"""
Method      : create_image
Parameters  : image_data(string)
Return      : base64Image
This method is designed to receive a binary string and evaluate it to an image. 
Not sure on how the client is going to send me the image
"""
def create_image(image_data):
    return base64.decodestring(image_data)


"""
Method      : resolution_and_color_optimization
Parameters  : Datafile(file)
Return      : image (in RGB)
This method takes a file and converts it to an image.
It also changes the file to RGB and resizes it to 227 227
"""
def resolution_and_color_optimization(Datafile):
    image = Image.open(Datafile)
    image = image.convert('RGB')
    # Not sure about useing linear interpolation in a 2x2 environment https://pillow.readthedocs.io/en/4.3.x/handbook/concepts.html#concept-modes
    return image.resize((227, 227), Image.BILINEAR)


"""
Method      : load_model
Parameters  : Image
Return      : Score.tolist()
Start a tensor flow module and creates the necessary graphics to evaluate the image through the AI system. 
Once the evaluation process is done the AI returns the score
    # line (111)could not get it to work with full path fron ec2-user
    # line (111)sftp://ec2-user@34.237.62.217/home/ec2-user/seefood-core-ai
    # line (111)sftp://ec2-user@34.237.62.217/home/ec2-user/seefood-core-ai/saved_model/model_epoch5.ckpt.meta
    # line (111) Code does not have permisions Fuck that!
    # line (112) None value is returned if no variables exist in the MetaGraphDef (i.e., there are no variables to restore).
"""
def load_model(image):
    sessionModel = tf.Session()
    saver_def = tf.train.import_meta_graph(
        '/var/www/seefood/seefood-core-ai/saved_model/model_epoch5.ckpt.meta')
    saver_def.restore(sessionModel, tf.train.latest_checkpoint('/var/www/seefood/seefood-core-ai/saved_model/'))
    graph = tf.get_default_graph()
    x_input = graph.get_tensor_by_name('Input_xn/Placeholder:0')
    keep_prob = graph.get_tensor_by_name('Placeholder:0')
    class_scores = graph.get_tensor_by_name("fc8/fc8:0")
    scores = sessionModel.run(class_scores, {x_input: image, keep_prob: 1.})
    return scores.tolist()


if __name__ == "__main__":
    app.run(debug=True)
