package com.example.arbel.howmuchleft

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "howMuchLeft.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.ItemEntry.TABLE_NAME + " (" +
                        DBContract.ItemEntry.COLUMN_ITEM_NAME + " TEXT," +
                        DBContract.ItemEntry.COLUMN_ITEM_PRICE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.ItemEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    @Throws(SQLiteConstraintException::class)
    fun insertItem(item: ItemModel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.ItemEntry.COLUMN_ITEM_NAME, item.name)
        values.put(DBContract.ItemEntry.COLUMN_ITEM_PRICE, item.price)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.ItemEntry.TABLE_NAME, null, values)

        return true
    }


    fun readAllItems(): ArrayList<ItemModel> {
        val items = ArrayList<ItemModel>()
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var price: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ITEM_NAME))
                price = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ITEM_PRICE))

                items.add(ItemModel(name, price))
                cursor.moveToNext()

            }
        }
        return items
    }

    fun calcItemsPrice(): Float {
        val items = ArrayList<ItemModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return 0F
        }

        var totalExpanses: Float = 0F
        var name: String
        var price: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ITEM_NAME))
                price = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ITEM_PRICE))
                println("price is: $price")
                println("name is: $name")

                items.add(ItemModel(name, price))
                cursor.moveToNext()

                totalExpanses += price.toFloat()
            }
        }
        println("total expanses are: $totalExpanses")
        return totalExpanses
    }
}

