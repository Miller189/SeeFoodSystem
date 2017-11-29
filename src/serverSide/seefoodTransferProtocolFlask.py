import os
from flask import Flask, request, json, send_file, jsonify, redirect, url_for
import base64
import numpy as np
from AIModel import AIModel
from ImageClass import ImageClass
import scoreMath
from SeeFoodDB import SeeFoodDB
import BaseHTTPServer
from PIL import Image
import io

ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'jfif', 'gif'])
app = Flask(__name__)

modelObject = AIModel()


"""
Method      : insert_DB
Parameters  : imageName, fullSzImgPath, thumbSzImgPath, isFood, score
Return      : Boolean
This method add image data to the db
"""
def insert_DB(imageName, fullSzImgPath, thumbSzImgPath, isFood, score):
    objDB = SeeFoodDB()
    dataImage = [imageName, fullSzImgPath, thumbSzImgPath, isFood, score]
    if objDB.create_table():
        if objDB.insert_image_data(dataImage):
            objDB.close_database_connection()
            return True
        else:
            objDB.close_database_connection()
            return False
    else:
        objDB.close_database_connection()
        return False



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
                return json.dumps({"error": "Images does not have an Name"}), 404
            if Datafile and allowed_file(Datafile.filename):
                obj = ImageClass(Datafile)
                file_name = obj.get_imageName() + ".png"

                #list score
                listScore = modelObject.evaluation(obj.get_image())
                scorePercentage = scoreMath.get_score_Percentage(listScore)
                obj.set_imageScore(scorePercentage)

                #is food
                food_boolean = modelObject.is_food(listScore)
                obj.set_foodBoolean(food_boolean)

                dumpFile.append(JSON_dumpEvaluation(file_name, scorePercentage, food_boolean))
                if insert_DB(obj.get_imageName(),obj.get_fullSizedImagePathName(), obj.get_thumbnailImagePathName(),obj.get_foodBoolean(),obj.get_imageScore()) == False:
                    return json.dumps({"error": "Images could not me saved to Database"}), 404
            else:
                return json.dumps({"error": Datafile.filename + ": Is not a valid image extension"}), 404
        # endLoop

    else:
        return json.dumps({"error": "not file found"}), 404
    json_string = json.dumps(dumpFile)
    return json_string


@app.route("/gallery",  methods=['GET'])
def gallery():
    # extracts
    try:
        count = request.args.get('limit')
        objDB = SeeFoodDB()
        dumpFile = list()
        results = objDB.gallery_read(int(count))
        if results == False:
            return json.dumps({"error": "Could not get " + count + " Images"}), 404
        else:
            for row in results:
                imgID = row[0]
                imgName = row[1]
                fullSzImgPath = row[2]
                thumbImgPath = row[3];
                isFood = row[4]
                score = row[5]
                dumpFile.append(JSON_dump_gallery(row[0],row[1],row[3],row[4],row[5]))

        objDB.close_database_connection()
        return json.dumps(dumpFile), 200
    except Exception as e:
        objDB.close_database_connection()
        return str(e), 404



"""
Method      : get_full_sized_image
Parameters  : Image
Return      : list
This method creates a list
"""
@app.route("/full_size_image", methods=['GET'])
def get_full_sized_image():
    # extracts
    fileID = request.args.get('file_ID')
    objDB = SeeFoodDB()
    result = objDB.read_by_image_id(fileID)
    if result == False:
        return json.dumps({"error": "Could not find full size image"}), 404
    else:
        try:
            return send_file(str(result[2]), attachment_filename=str(result[1]))
        except Exception as e:
            return str(e),  404

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
Method      : JSON_dump_gallery
Parameters  : imgID, imgName, fullSzImgPath, thumbImgPath, isFood, score
Return      : list
This method creates a list
"""
def JSON_dump_gallery(imgID, imgName, imagePath, isFood, score):
    byeOfImageArray = io.BytesIO()
    image = Image.open(imagePath)
    image.save(byeOfImageArray, format="PNG")
    ImagebyeArrayData = byeOfImageArray.getvalue()
    imageData = base64.b64encode(ImagebyeArrayData)
    data = []
    data.append({
        'file_ID': imgID,
        'file_name': imgName,
        'data': imageData,
        'food_boolean': isFood,
        'file_score': score
    })
    return data

if __name__ == "__main__":
    app.run(threaded=True)

