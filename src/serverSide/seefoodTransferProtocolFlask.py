import os
from flask import Flask, request, json, jsonify, redirect, url_for
import numpy as np
from AIModel import AIModel
from ImageClass import ImageClass

ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'jfif', 'gif'])
app = Flask(__name__)

#modelObject = AIModel()
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
Method      : start_screen
Parameters  : None
Return      : JSON Table
This method is used to ensure that the session model is running and the server is ready for images
"""
@app.route('/', methods=['Get'])
def start_screen():
    # 404 The requested resource could not be found but may be available in the future. Subsequent requests by the client are permissible.
    # 201 The request has been fulfilled, resulting in the creation of a new resource
    data = []
    message = ""
    code = 404
    if (modelObject == None):
        message = "Session model was not loaded successfully"
    else:
        message = "Session model was loaded successfully"
        code = 201

    data.append({'message': message})
    json_string = json.dumps(data)
    return json_string , code

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
        # endLoop
                #dumpFile.append(JSON_dumpEvaluation(file_name, None, None))
    else:
        return json.dumps({"error": "not file found"}), 404


    json_string = json.dumps(dumpFile)
    return json_string

@app.route("/gallery",  methods=['GET'])
def gallery():
    return "Hello World!"


@app.route("/full_size_image",  methods=['GET'])
def get_full_sized_image():
    return "Full size image!"

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

if __name__ == "__main__":
   #modelObject.setModel(modelObject)
    app.run(debug=True)

