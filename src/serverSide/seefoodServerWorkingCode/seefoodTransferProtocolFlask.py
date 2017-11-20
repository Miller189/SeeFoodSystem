import os
from flask import Flask, request, json, jsonify, redirect, url_for
import argparse
import numpy as np
from AIModel import AIModel

# Used to set file exe types
from ImageClass import ImageClass

ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'jfif', 'gif'])
app = Flask(__name__)

modelObject = AIModel()
if __name__ == "__main__":
    print "fuck"
    modelObject.setModel(modelObject)
    app.run(threaded=True)


# def get_running_model():
#     return modelObject

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
                obj = ImageClass(Datafile)
                file_name = obj.get_imageName()
                #score
                file_score = modelObject.evaluation(obj.get_image())
                obj.set_imageScore(file_score)

                #is food
                food_boolean = modelObject.is_food(obj.get_imageScore())
                obj.set_foodBoolean(food_boolean)


                dumpFile.append(JSON_dumpEvaluation(file_name, file_score, food_boolean))
                # imageObject.saveFullSizeToDB()
                # imageObject.saveThumbToDB()
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

