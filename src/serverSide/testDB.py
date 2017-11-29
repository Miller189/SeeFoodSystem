

#!/usr/bin/python
from SeeFoodDB import SeeFoodDB

obj = SeeFoodDB()


def main():

    obj.delete_table("ImgData_tbl")
    obj.delete_table("GalImgCount_tbl")
    #obj.print_whole_tbl()

    # SeeFoodDB.stuff(ImgDataLst)
    # for x in range(1, 10):
    #     obj.insert_image_data(ImgDataLst)



    obj.close_database_connection()


main()
