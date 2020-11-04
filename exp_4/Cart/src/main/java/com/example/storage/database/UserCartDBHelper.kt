package com.example.storage.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log



object cartContract {
    object cartEntry : BaseColumns {
        const val TABLE_NAME = "cart"
        const val CART_ID = "id"
        const val GOOD_ID = "good_id"
        const val COUNT = "count"
        const val UPDATE_TIME = "update_time"
    }
}

private val SQL_CREATE_ENTRIES =
    "CREATE TABLE IF NOT EXISTS ${cartContract.cartEntry.TABLE_NAME} (" +
            "${cartContract.cartEntry.CART_ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "${cartContract.cartEntry.GOOD_ID} LONG NOT NULL," +
            "${cartContract.cartEntry.COUNT} INTEGER NOT NULL," +
            "${cartContract.cartEntry.UPDATE_TIME} INTEGER NOT NULL"+
            ");"
private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${goodContract.goodEntry.TABLE_NAME}"



class UserCartDBHelper(ctx: Context): SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun buildDB(){
        Log.e(TAG, "Build Database")

//        val db = dbHelper.writableDatabase
//        db.execSQL(SQL_DELETE_ENTRIES)
//        db.execSQL(SQL_CREATE_ENTRIES)

//        val values = ContentValues().apply {
//            put(cartContract.cartEntry.GOOD_ID, "1")
//            put(cartContract.cartEntry.COUNT, 1)
//            put(cartContract.cartEntry.UPDATE_TIME, 1)
//        }
//        db.insert(goodContract.goodEntry.TABLE_NAME,null, values)

    }

    // 货物加入购物车
    @SuppressLint("Recycle")
    fun insertDB(good_id: String) {
        Log.e(TAG, "insert cart info")
        val db = this.writableDatabase
        val query = "SELECT * FROM ${cartContract.cartEntry.TABLE_NAME} WHERE ${cartContract.cartEntry.GOOD_ID} = $good_id"
        val cursor = db.rawQuery(query, null)

        if(cursor?.count == 0) {
            Log.e(TAG, "Insert new commodity")
            val values = ContentValues().apply{
                put(cartContract.cartEntry.GOOD_ID, good_id)
                put(cartContract.cartEntry.COUNT, 1)
                put(cartContract.cartEntry.UPDATE_TIME, 1)
            }
            val newRowId = db?.insert(cartContract.cartEntry.TABLE_NAME, null, values)
            Log.e(TAG, "New commodity in cart is at "+newRowId.toString())
        } else {
            Log.e("Update", "Cursor count to "+cursor.count.toString())
            cursor.moveToFirst()
            val mcount = cursor.getString(2)
            val update = cursor.getString(3)
            val values = ContentValues().apply{
                put(cartContract.cartEntry.COUNT, mcount.toInt()+1)
                put(cartContract.cartEntry.UPDATE_TIME, update.toInt()+1)
            }
            val countDB = db.update (
                cartContract.cartEntry.TABLE_NAME,
                values,
                "${cartContract.cartEntry.GOOD_ID} = ?",
                arrayOf(good_id)
            )
            Log.e("Update", "In insertDB $countDB")
        }
    }

    // 货物减少
    @SuppressLint("Recycle")
    fun updateDB(good_id: String) {
        Log.e(TAG, "update cart info")
        val db = this.writableDatabase
        val cursor = db.query(
            cartContract.cartEntry.TABLE_NAME,
            null,
            "good_id = ?",
            arrayOf(good_id),
            null,
            null,
            null
        )
        // 如果货物的余量大于1
        val count = cursor!!.getString(cursor.getColumnIndex(cartContract.cartEntry.COUNT))
        if (count.toInt() > 1){
            var mCount = count.toInt() - 1
            val update = cursor.getString(cursor.getColumnIndex(cartContract.cartEntry.UPDATE_TIME))
            var mUpdate = update.toInt() + 1
            val values = ContentValues().apply{
                put(cartContract.cartEntry.COUNT, count+1)
                put(cartContract.cartEntry.UPDATE_TIME, update+1)
            }
            val countDB = db.update (
                cartContract.cartEntry.TABLE_NAME,
                values,
                "${cartContract.cartEntry.GOOD_ID} = ?",
                arrayOf(good_id)
            )
            Log.e("Update", "In updateDB "+ countDB.toString())
        } else {
            // 如果货物余量等于1
            val selection = "${cartContract.cartEntry.GOOD_ID} = ?"
            val selectionArgs = arrayOf(good_id)
            val deleteRows = db.delete(cartContract.cartEntry.TABLE_NAME, selection, selectionArgs)
            Log.e("Update", "In updateDB delete row id "+deleteRows.toString())
        }

    }

    // 寻找货物
    fun readDB(): Cursor {
        Log.e(TAG, "Read Database and return cursor")
        val db = this.readableDatabase
        val query = "SELECT * FROM ${cartContract.cartEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        return cursor
    }

    // 清空购物车
    fun clearDB() {
        Log.e("Clear", "Start")
        val db = this.writableDatabase
        val deleteRow = db.delete(cartContract.cartEntry.TABLE_NAME, null, null)
        Log.e("Delete", "Clear $deleteRow rows")
    }

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "cart.db"
        private const val TAG = "Cart DB Helper"
    }
}
