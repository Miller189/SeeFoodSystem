# !/usr/bin/python
import GalImgCount_tbl
import MySQLdb

class SeeFoodDB:
    def __init__(self):
        self.dbcon = self.open_database_connection()

    """
    *****NEEDED
    Method      : open_database_connection
    Parameters  : 
    Return      : dbcon
    Make database connection
    """
    def open_database_connection(self):
        # Open database connection
        dbcon = MySQLdb.connect("localhost", "root", "pass1234", "SeeFoodDB")
        return dbcon

    """
    *****NEEDED
    Method      : get_dbcon
    Parameters  : 
    Return      : dbcon
    get dbcon
    """
    def get_dbcon(self):
        return self.dbcon

    """
    *****NEEDED
    Method      : initialize_cursor
    Parameters  : 
    Return      : self.dbcon.cursor()
    Initialize the cursor
    """
    def initialize_cursor(self):
        # prepare a cursor object using cursor() method
        return self.dbcon.cursor()

    """
    *****NEEDED
    Method      : close_database_connection
    Parameters  : 
    Return      : 
    Check if table exists
    """
    def close_database_connection(self):
        # disconnect from server
        self.dbcon.close()


    """
    *****NEEDED
    Method      : check_tbl_exist
    Parameters  : dbcon, tblName
    Return      : Boolean
    Check if table exists
    """
    def check_tbl_exist(self, tblName):
        dbcur = self.initialize_cursor()
        dbcur.execute("""
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_name = '{0}' 
            """.format(tblName.replace('\'', '\'\'')))

        if dbcur.fetchone()[0] == 1:
            dbcur.close()
            return True

        dbcur.close()
        return False

    """
    Not Need!
    Method      : delete_table
    Parameters  : dbcon, tblName
    Return      : Boolean and 1
    Delete table
    """
    def delete_table(self, tblName):
        # prepare a cursor object using cursor() method
        dbcur = self.initialize_cursor()

        sql = "DROP TABLE IF EXISTS %s " % (tblName)

        if self.check_tbl_exist(tblName) == True:

            try:
                # Drop table if it already exist using execute() method.
                dbcur.execute(sql)
                print "Delete '%s!'" % (tblName)
                dbcur.close()
                return True
            except:
                # Rollback in case there is any error
                print'error deleting %s!' % (tblName)
                self.dbcon.rollback()
                dbcur.close()
                return False
        else:
            print "Table does not exist!"
            dbcur.close()
            return False

    """
    Not Needed
    Method      : record_count
    Parameters  : dbcon, tblName
    Return      : Boolean and 1
    This function return total number of records count in the table
    """
    def record_count(self, tblName):
        # prepare a cursor object using cursor() method
        dbcur = self.initialize_cursor()

        sql = "SELECT * FROM %s" % (tblName)

        try:
            # Execute the SQL command

            dbcur.execute(sql)
            # Fetch all the rows in a list of lists.
            results = int(dbcur.rowcount)
            #print "record count: %d" % results
            dbcur.close()
            return results
        except:
            print "Error: unable to fecth data"
            # disconnect from server
            dbcur.close()
            return -1

    #----------------%$%#%#

    """
    *****NEEDED
    Method      : create_table
    Parameters  : dbcon
    Return      : Boolean
    create Image Data table if and only if Table is not already made
    """
    def create_table(self):
        # prepare a cursor object using cursor() method
        dbcur = self.initialize_cursor()

        # Create table as per requirement
        sql = """CREATE TABLE ImgData_tbl(
                  ImgID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  ImgName VARCHAR(450) NOT NULL,
                  FullSzImgPath VARCHAR(900) NOT NULL,
                  ThumbSzImgPath VARCHAR(900) NOT NULL,
                  IsFood BOOL NOT NULL,
                  Score FLOAT(7,4) NOT NULL)"""

        if self.check_tbl_exist("ImgData_tbl") == False:

            try:
                # Execute the SQL command
                dbcur.execute(sql)
                self.dbcon.commit()
                dbcur.close()

                return True
            except:
                # Rollback in case there is any error
                self.dbcon.rollback()
                dbcur.close()
                return False
        else:
            dbcur.close()
            return True

    """
    *****NEEDED
    Method      : insert_image_data
    Parameters  : dbcon, ImgDataLst (list)
    Return      : Boolean
    Insert image data into the table
    """
    def insert_image_data(self,ImgDataLst):
        dbcur = self.initialize_cursor()


        sql = "INSERT INTO ImgData_tbl(ImgID,ImgName,FullSzImgPath,ThumbSzImgPath,IsFood,Score)" \
              "VALUES (NULL,'%s','%s','%s',%s,'%f')" % \
              (ImgDataLst[0], ImgDataLst[1], ImgDataLst[2], ImgDataLst[3], float(ImgDataLst[4]))
        try:
            # Execute the SQL command
            dbcur.execute(sql)
            dbcur.close()
            # Commit your changes in the database
            self.dbcon.commit()

            return True
        except:
            # Rollback in case there is any error
            self.dbcon.rollback()
            dbcur.close()
            return False

    """
    *****NEEDED
    Method      : read_by_image_id
    Parameters  : dbcon, imageId
    Return      : False or result
    Select records by Image Id and return only one item
    """
    def read_by_image_id(self, imageId):
        dbcur = self.initialize_cursor()
        sql = "SELECT * FROM ImgData_tbl WHERE ImgId = '%s'" % (imageId)
        try:
            # Execute the SQL command
            dbcur.execute(sql)
            result = dbcur.fetchone()
            dbcur.close()
            return result
        except:
            # disconnect from server
            dbcur.close()
            return False

    """
    *****NEEDED
    Method      : read_by_image_name
    Parameters  : dbcon, imageName
    Return      : False or results
    Select records by Image Name. 
    This query could theoretically return more than one item
    """

    def read_by_image_name(self, imageName):
        dbcur = self.initialize_cursor()
        sql = "SELECT * FROM ImgData_tbl WHERE ImgName= '%s'" % (imageName)
        try:
            # Execute the SQL command
            dbcur.execute(sql)
            # Fetch all the rows in a list of lists.
            results = dbcur.fetchall()
            dbcur.close()
            return results
        except:
            # disconnect from server
            dbcur.close()
            return False

    """
    Not NEEDED
    Method      : print_whole_tbl
    Parameters  : dbcon
    Return      : Boolean
    print the whole table
    """
    def print_whole_tbl(self):
        # prepare a cursor object using cursor() method
        dbcur = self.initialize_cursor()

        sql = "SELECT * FROM ImgData_tbl"
        try:
            # Execute the SQL command

            dbcur.execute(sql)

            # Fetch all the rows in a list of lists.
            results = dbcur.fetchall()
            dbcur.close()
            for row in results:
                ImgID = row[0]
                ImgName = row[1]
                FullSzImgPath = row[2]
                ThumbSzImgPath = row[3];
                IsFood = row[4]
                Score = row[5]

                # Now print fetched result
                print "ImageID=%d,ImageName=%s,Full Size Image Path=%s,Thumbnail Image Path=%s, Is_Food=%s, Score=%f" % \
                      (ImgID, ImgName, FullSzImgPath, ThumbSzImgPath, IsFood, Score)
            return True
        except:
            print "Error: unable to fecth data"

            # disconnect from server
            dbcur.close()
            return False

    """
    Not NEEDED
    Method      : print_by_number_of_records
    Parameters  : dbcon, offset, count
    Return      : Boolean and  results
    print record by specic offset and specific number of records
    """

    def print_by_number_of_records(self, offset, count):
        # prepare a cursor object using cursor() method
        dbcur = self.initialize_cursor()

        sql = "SELECT * FROM ImgData_tbl limit %d,%d" % \
              (offset, count)
        try:
            # Execute the SQL command
            dbcur.execute(sql)

            # Fetch all the rows in a list of lists.
            results = dbcur.fetchall()
            dbcur.close()
            return results
            # for row in results:
            #     ImgID = row[0]
            #     ImgName = row[1]
            #     FullSzImgPath = row[2]
            #     ThumbSzImgPath = row[3];
            #     IsFood = row[4]
            #     Score = row[5]
            #
            #     # Now print fetched result
            #     print "ImageID=%d,ImageName=%s,Full Size Image Path=%s,Thumbnail Image Path=%s, Is_Food=%s, Score=%f" % \
            #           (ImgID, ImgName, FullSzImgPath, ThumbSzImgPath, IsFood, Score)


        except:
            print "Error: unable to fecth data"

            # disconnect from server
            dbcur.close()
            return False

    """

    Method      : gallery_read
    Parameters  : dbcon, count
    Return      : Boolean
    print record by specic offset and specific number of records
    """
    def gallery_read(self, count):
        GalImgCount_tbl.create_table(self.get_dbcon(),self)
        offset = 10

        if self.record_count('GalImgCount_tbl') == 0:
            offset = 0
        else:
            offset = GalImgCount_tbl.read_gallery_image_counter(self.get_dbcon(),self)
            if offset == False:
                return False

        totalrecord = self.record_count("ImgData_tbl")

        if offset < totalrecord:
            results =  self.print_by_number_of_records(offset, count)
            if results == False:
                return False
        else:
            offset = 0
            results = self.print_by_number_of_records(offset, count)
            if results == False:
                return False

        offset += int(count)
        if GalImgCount_tbl.insert_count_gallery_img(self.get_dbcon(), offset, self) ==False:
            return False
        return results