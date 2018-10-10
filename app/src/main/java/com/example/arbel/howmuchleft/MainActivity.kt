package com.example.arbel.howmuchleft

//  https://www.tutorialkart.com/kotlin-android/android-sqlite-example-application/


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var itemsDBHelper : DatabaseHelper

    private val initialBudget = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        itemsDBHelper = DatabaseHelper(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = this.menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        val intent = Intent(this, SettingsActivity::class.java)
        if (id == R.id.menu_settings) {
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }



    fun addItem(v:View){
        val name = this.edittext_name.text.toString()
        val price = this.edittext_price.text.toString()
        var result = itemsDBHelper.insertItem(ItemModel(name,price))
        //clear all edittext s
        this.edittext_price.setText("")
        this.edittext_name.setText("")
        this.textview_result.text = "new item successfuly added"
        this.ll_entries.removeAllViews()
    }


    fun showAllItems(v:View){
        var items = itemsDBHelper.readAllItems()
        this.ll_entries.removeAllViews()
        items.forEach {
            val textView = TextView(this)
            textView.textSize = 20F
            textView.text = it.name.toString() + " - " + it.price.toString()
            this.ll_entries.addView(textView)
        }
        this.textview_result.text = "Fetched " + items.size + " items"
        val textView = TextView(this)
        val expenses = itemsDBHelper.calcItemsPrice().toString()

        textView.textSize = 30F

        textView.text = "you have spended $expenses euros"
        this.ll_entries.addView(textView)
    }

    fun showTotalBudget(v:View){
        this.ll_entries.removeAllViews()
        val textView = TextView(this)
        textView.textSize = 30F
        textView.text = "total Budget left: " + (initialBudget - itemsDBHelper.calcItemsPrice()).toString()
        this.ll_entries.addView(textView)

    }

}