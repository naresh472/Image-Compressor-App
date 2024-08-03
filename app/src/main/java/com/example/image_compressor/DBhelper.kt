package com.example.image_compressor

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import java.sql.SQLData

class DBhelper(context: Context , factory:SQLiteDatabase.CursorFactory?) :
            SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION){




    override fun onCreate(db: SQLiteDatabase?) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                +
                image_name + " TEXT" +
                   ")")


        db?.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)

    }
    fun addimage( image : String ){


        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
       // values.put(ID_COL, id)
        values.put(image_name, image)


        val db = this.writableDatabase


        db.insert(TABLE_NAME, null, values)


        db.close()
    }
    fun getimage(): Cursor? {

        val db = this.readableDatabase


        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    fun deleteimage() {

        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null);
        db.close();
        return
    }
    companion object{

        private val DATABASE_NAME = "image_compress"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "image_list"



        val image_name = "image_name"


    }
}


