package com.uyghar.kitabhumar.ui.user

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserHelper(context: Context): SQLiteOpenHelper(context,"user.db",null,1) {
    val TABLE_NAME = "members"
    val FIELD_ID = "id"
    val FIELD_NAME = "name"
    val FIELD_SURNAME = "surname"
    var FIELD_EMAIL = "email"
    var FIELD_NICKNAME = "nickname"

    override fun onCreate(db: SQLiteDatabase?) {
        val sql_str = "CREATE TABLE IF NOT EXISTS $TABLE_NAME($FIELD_ID INTEGER, $FIELD_NAME TEXT, $FIELD_SURNAME TEXT, $FIELD_NICKNAME TEXT, $FIELD_EMAIL TEXT)"
        db?.execSQL(sql_str)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}