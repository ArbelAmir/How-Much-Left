package com.example.arbel.howmuchleft


import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class ItemEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "userItems"
            val COLUMN_ITEM_NAME = "itemName"
            val COLUMN_ITEM_PRICE = "itemPrice"
        }
    }
}