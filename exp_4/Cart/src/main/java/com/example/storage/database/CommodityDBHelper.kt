package com.example.storage.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.storage.R
import com.example.storage.info.commodityInfo

object goodContract {
    object goodEntry : BaseColumns {
        const val TABLE_NAME = "commodity"
        const val ID ="id"
        const val NAME = "name"
        const val DESC = "desc"
        const val PRICE = "price"
        const val THUMB = "thumb_path"
        const val PIC = "pic_path"
    }
}

private val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${goodContract.goodEntry.TABLE_NAME} (" +
            "${goodContract.goodEntry.ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "${goodContract.goodEntry.NAME} VARCHAR NOT NULL," +
            "${goodContract.goodEntry.DESC} VARCHAR NOT NULL," +
            "${goodContract.goodEntry.PRICE} FLOAT NOT NULL," +
            "${goodContract.goodEntry.THUMB} VARCHAR NOT NULL," +
            "${goodContract.goodEntry.PIC} VARCHAR NOT NULL" +
            ");"

private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${goodContract.goodEntry.TABLE_NAME}"

// 声明一个手机商品的名称数组
private val mNameArray = arrayOf(
    "iPhone8", "Mate10", "小米6", "OPPO R11", "vivo X9S", "魅族Pro6S"
)

// 声明一个手机商品的描述数组
private val mDescArray = arrayOf(
    "Apple iPhone 8 256GB 玫瑰金色 移动联通电信4G手机",
    "华为 HUAWEI Mate10 6GB+128GB 全网通（香槟金）",
    "小米 MI6 全网通版 6GB+128GB 亮白色",
    "OPPO R11 4G+64G 全网通4G智能手机 玫瑰金",
    "vivo X9s 4GB+64GB 全网通4G拍照手机 玫瑰金",
    "魅族 PRO6S 4GB+64GB 全网通公开版 星空黑 移动联通电信4G手机"
)

// 声明一个手机商品的价格数组
private val mPriceArray = floatArrayOf(6888f, 3999f, 2999f, 2899f, 2698f, 2098f)

// 声明一个手机商品的小图数组
private val mThumbArray = intArrayOf(
    R.drawable.iphone_s, R.drawable.huawei_s, R.drawable.xiaomi_s,
    R.drawable.oppo_s, R.drawable.vivo_s, R.drawable.meizu_s
)

// 声明一个手机商品的大图数组
private val mPicArray = intArrayOf(
    R.drawable.iphone, R.drawable.huawei, R.drawable.xiaomi,
    R.drawable.oppo, R.drawable.vivo, R.drawable.meizu
)

class CommodityDBHelper(ctx: Context) : SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy
        // is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun buildDB(){
        Log.e(TAG, "Build Database")

        val db = this.writableDatabase
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)

        for (i in 0..(mNameArray.size-1)) {
            Log.e(TAG, i.toString())
            val values = ContentValues().apply {
                put(goodContract.goodEntry.NAME, mNameArray[i])
                put(goodContract.goodEntry.DESC, mDescArray[i])
                put(goodContract.goodEntry.PRICE, mPriceArray[i])
                put(goodContract.goodEntry.THUMB, mThumbArray[i])
                put(goodContract.goodEntry.PIC, mPicArray[i])
            }
            db.insert(goodContract.goodEntry.TABLE_NAME,null, values)
        }

    }
    fun readDB(): Cursor {
        Log.e(TAG, "Read Database and return cursor")
        val db = this.readableDatabase
        val query = "SELECT * FROM ${goodContract.goodEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        return cursor
    }

    fun findComm(good_id: String) : Cursor {
        Log.e(TAG, "Find $good_id in database")
        val db = this.readableDatabase
        val query = "SELECT * FROM ${goodContract.goodEntry.TABLE_NAME} WHERE ${goodContract.goodEntry.ID} = $good_id"
        val cursor = db.rawQuery(query, null)
//        Log.e("Find", cursor.getString(0)+" "+cursor.getString(1)+" "+good_id)
        cursor.moveToFirst()
        return cursor
    }
    companion object {
        // If the database schema is changed, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "commodity.db"
        private const val TAG = "Commodity DB Helper"
    }

}