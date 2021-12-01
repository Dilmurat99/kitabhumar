package com.uyghar.kitabhumar.ui.user

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uyghar.kitabhumar.models.User

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

    fun newMember(member: User) {
        //val sql_str = "INSERT INTO $TABLE_NAME($FIELD_NAME,$FIELD_AUTHOR) VALUES('${kitab.kitab_ismi}', '${kitab.aptor}')"
        var db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(FIELD_ID, member.id)
        contentValues.put(FIELD_NAME, member.name)
        contentValues.put(FIELD_SURNAME, member.surname)
        contentValues.put(FIELD_NICKNAME, member.nickname)
        contentValues.put(FIELD_EMAIL, member.email)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
        //db.execSQL(sql_str)
    }

    fun members() : ArrayList<User> {
        val tizim = ArrayList<User>()
        val sql_str = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql_str,null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val surname = cursor.getString(2)
            val nickname = cursor.getString(3)
            val email = cursor.getString(4)
            val member = User(id, name, surname, nickname, email, "")
            tizim.add(member)
        }
        db.close()
        return tizim
    }

    fun clearDB() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}