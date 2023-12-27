package com.example.todolistapp

import OnSwipeTouchListener
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R.layout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.customcategory.*
import kotlinx.android.synthetic.main.task_view.view.*
import kotlinx.android.synthetic.main.customcategory.view.*
import java.util.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {


    private lateinit var todoAdapter: TodoAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "ServiceCast", "RestrictedApi", "SetTextI18n",
        "SuspiciousIndentation"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        val colorsMap = mapOf("Blue" to R.color.blue, "Green" to R.color.pure_green, "Red" to R.color.pure_red, "OffWhite" to R.color.offwhite, "Yellow" to R.color.pure_yellow, "LightGreen" to R.color.green, "Purple" to R.color.purple, "Navy" to R.color.lightblue_1, "Pink" to R.color.pink, "DarkGreen" to R.color.pure_green, "LightPink" to R.color.shrine_pink_50);
        val sharedPreferences = getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE)
        var allowSwiper = sharedPreferences.getBoolean("allowSwipe", false)
        val modalBottomSheet = ModalBottomSheet()
        supportActionBar?.hide()
        Log.d("debug", "INITIAL Allow Swipe Interaction VAL:$allowSwiper")

        todoAdapter  = TodoAdapter(mutableListOf())
        rvTodoList.adapter = todoAdapter
        rvTodoList.layoutManager = LinearLayoutManager(this)

        val helper = DBHelper(applicationContext)
        val db = helper.readableDatabase
        val rs = db.rawQuery("SELECT * FROM USERS", null)



    if(rs.moveToFirst())
        do
        {
            val titleTemp = rs.getString(1)
            val descriptionTemp = rs.getString(2)
            val categoryTemp = rs.getString(3)
            val dateTemp = rs.getString(4)
            val timeTemp = rs.getString(5)

            val chip= Chip(this)

            val todo = Todo(titleTemp,descriptionTemp ,categoryTemp, dateTemp,timeTemp, chip, if(rs.getString(6)!=null)rs.getString(6).toInt(); else 0)




            if(chip.text.equals("Important"))
                todoAdapter.addTodoItemAtIndex(todo,0)
            else
                todoAdapter.addTodoItem(todo)


        }
        while (rs.moveToNext())
        rs.close()
        rvTodoList.scheduleLayoutAnimation()



        if(allowSwiper) {
            rvTodoList.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
                override fun onSwipeLeft() {
                    super.onSwipeLeft()

                }

                override fun onSwipeRight() {
                    super.onSwipeRight()

                }

                override fun onSwipeUp() {
                    super.onSwipeUp()

                    modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)

                }

                override fun onSwipeDown() {
                    super.onSwipeDown()
                }
            })
        }

        btnNewTask.setOnClickListener {
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)

        }
        btnNewTask.setOnLongClickListener {
            true
        }
        btnSettings.setOnClickListener{
            val i = Intent(this, SettingsPage::class.java)
            startActivity(i)
        }

    }


    fun addTask(string: String,description: String, categoryTitle:String,  date:String,time:String, chip:Chip,db:SQLiteDatabase){
        val todo = Todo(string,description, categoryTitle, date,time,chip)
        Log.d("debug","From MainActivity: Created $string , $description , $categoryTitle , $categoryTitle , $date , $time, ${chip.tag}  into USERS DATABASE")

        todoAdapter.addTodoItem(todo)
        val cv = ContentValues()
        cv.put("TITLE", string)
        cv.put("DESCRIPTION", description)
        cv.put("CATEGORYTITLE", categoryTitle)
        cv.put("DATE", date)
        cv.put("TIME", time)
        if(chip.tag!=null)cv.put("CHIPCOLOR", chip.tag.toString())
        Log.d("Debug", "From MainActivity: Inserted Chip with color : " + chip.tag.toString())

        db.insert("USERS", null, cv)
        rvTodoList.scheduleLayoutAnimation()

        //

    }

    fun addTaskAtIndex(string: String,description: String,categoryTitle:String, date:String,time:String, chip:Chip,index:Int,db:SQLiteDatabase){
        val todo = Todo(string,description,categoryTitle, date,time,chip)
        Log.d("debug","Created $string , $description , $categoryTitle , $categoryTitle , $date , $time at index $index into USERS DATABASE ")

        todoAdapter.addTodoItemAtIndex(todo,index)
        val cv = ContentValues()
        cv.put("TITLE", string)
        cv.put("DESCRIPTION", description)
        cv.put("CATEGORYTITLE", categoryTitle)
        cv.put("DATE", date)
        cv.put("TIME", time)
        if(chip.tag!=null)cv.put("CHIPCOLOR", chip.tag.toString());
        db.insert("USERS", null, cv)
        rvTodoList.scheduleLayoutAnimation()


    }




    }
















