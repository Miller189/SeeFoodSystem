# !/usr/bin/python

"""
*****NEEDED
Method      : create_table
Parameters  : dbcon
Return      : Boolean and 1
"""
def create_table(dbcon , obj):
    # prepare a cursor object using cursor()
    dbcur = obj.initialize_cursor()
    # Create table
    sql = """CREATE TABLE GalImgCount_tbl(
             RecID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
             LastImgRecID VARCHAR(20))"""

    if obj.check_tbl_exist("GalImgCount_tbl") == False:
        try:
            # Execute the SQL command
            dbcur.execute(sql)
            # Commit your changes in the database
            dbcon.commit()
            dbcur.close()

            return True
        except:
            # Rollback in case there is any error
            #print'error creating table!'
            dbcon.rollback()
            dbcur.close()
            return False
    else:
        #print "Table already exist!"
        dbcur.close()
    return 1

"""
*****NEEDED
Method      : insert_count_gallery_img
Parameters  : dbcon, LastImgRecID
Return      : Boolean
"""
def insert_count_gallery_img(dbcon, LastImgRecID, obj):
    dbcur = obj.initialize_cursor()
    sql = """INSERT INTO GalImgCount_tbl(RecID,LastImgRecID) VALUES (NULL,'%d')""" % (LastImgRecID)

    try:
        # Execute the SQL command
        dbcur.execute(sql)
        dbcur.close()
        # Commit your changes in the database
        dbcon.commit()
        return True
    except:
        # Rollback in case there is any error
        dbcon.rollback()
        dbcur.close()
        return False

"""
Method      : read_gallery_image_counter
Parameters  : dbcon
Return      : Data and Boolean
"""
def read_gallery_image_counter(dbcon,obj):
    dbcur = obj.initialize_cursor()
    sql = """SELECT LastImgRecID FROM GalImgCount_tbl ORDER BY RecID DESC LIMIT 1"""
    try:
        # Execute the SQL command
        #print "inside try"
        dbcur.execute(sql)
        results = dbcur.fetchall()
        for row in results:
            LastRecID = int(row[0])
            return LastRecID
        dbcur.close()
    #TypeError as e
    except:
        # disconnect from server
        dbcur.close()
        return False








