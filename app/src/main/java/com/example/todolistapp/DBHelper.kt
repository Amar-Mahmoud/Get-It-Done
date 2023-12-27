package com.example.todolistapp

import android.content.ClipData.Item
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TASKDB"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE USERS(USERSID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, DESCRIPTION TEXT, CATEGORYTITLE TEXT, DATE TEXT, TIME TEXT, CHIPCOLOR TEXT)")
        db?.execSQL("CREATE TABLE CATEGORIES(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, COLOR TEXT)")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


}