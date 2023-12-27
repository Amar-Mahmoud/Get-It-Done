package com.example.todolistapp


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings_page.*
import kotlinx.android.synthetic.main.activity_settings_page.view.*
import kotlinx.android.synthetic.main.activity_start_screen2.*
import kotlinx.android.synthetic.main.create_category.*
import kotlinx.android.synthetic.main.create_category.view.*


class SettingsPage : AppCompatActivity() {
    var allowSwipe = false

    private lateinit var mainActivity: MainActivity
    private lateinit var selectedChip: Chip
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)
        supportActionBar?.hide()
        loadData()


        val catGroup = findViewById<ChipGroup>(R.id.category_chip_group)
        var colorChip: Chip = Chip(this)
        var helper = DBHelper(applicationContext)
        var db = helper.readableDatabase
        var databasePointer = db.rawQuery("SELECT * FROM CATEGORIES", null)

        val sharedPreference =  getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE)
        var chipIsSelected : Boolean = false
        cbSwipeInteractions.setOnClickListener{
            saveData()
            tvRestart.isVisible = true
        }

        val colorsMap = mapOf("Blue" to R.color.blue, "Green" to R.color.pure_green, "Red" to R.color.pure_red, "OffWhite" to R.color.offwhite, "Yellow" to R.color.pure_yellow, "LightGreen" to R.color.green, "Purple" to R.color.purple, "Navy" to R.color.lightblue_1, "Pink" to R.color.pink, "DarkGreen" to R.color.pure_green, "LightPink" to R.color.shrine_pink_50);



        fun refreshChips(){
            category_chip_group.removeAllViews()
            if(databasePointer.moveToFirst())
                do {
                    val chip: Chip = Chip(this)
                    chip.text=databasePointer.getString(1)
                    chip.elevation=2f
                    chip.iconStartPadding=1f
                    chip.chipCornerRadius=25f
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.setChipIconResource(R.drawable.circle)
                    chip.setTextColor(Color.BLACK)
                    chip.chipIconSize=33f
                    colorsMap[databasePointer.getString(2)]?.let { chip.tag = it }
                    colorsMap[databasePointer.getString(2)]?.let { chip.setChipIconTintResource(it) }


                    category_chip_group.addView(chip)

                }
                while (databasePointer.moveToNext())
        }
        refreshChips()




        addChip.setOnClickListener {
            var view = layoutInflater.inflate(R.layout.create_category, null, false);
            val colorGroup = view.colorChipGroup

            var m = MaterialAlertDialogBuilder(this)
                .setView(view)
                .show()

            if(colorGroup.childCount!=0){


                val chipsCount: Int = colorGroup.childCount
                var i = 0
                var chipSelectedIndex :Int
                selectedChip = Chip(this)

                while (i < chipsCount) {
                    val chip = colorGroup.getChildAt(i) as Chip
                    chip.setOnClickListener{
                        Log.d("Debug","Title: "+view.etCatTitle.text.toString() + " Chip Tag: " + chip.tag + " Chip Color for DB: " )
                        val ids = colorGroup.checkedChipIds
                        if (ids.size > 1) {
                            chip.isChecked = false
                        }
                        if (chip.isChecked) {
                            selectedChip=chip
                            chipSelectedIndex=i
                            chipIsSelected=true
                        }
                        else chipIsSelected=false
                    }
                    i++
                }

            }
            view.btnCreateCat.setOnClickListener {
                if(chipIsSelected && !view.etCatTitle.text.isNullOrEmpty() ){
                        val cv = ContentValues()
                        cv.put("TITLE",view.etCatTitle.text.toString() )
                        cv.put("COLOR",selectedChip.tag.toString() )
                        Log.d("Debug", "Added to DB: " + view.etCatTitle.text.toString() + " "+ selectedChip.tag.toString())
                        db.insert("CATEGORIES", null, cv)
                        m.cancel()
                        finish();
                        startActivity(getIntent());
                    }




            }
        }




            /*addChip.setOnClickListener{
                val dialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.create_category, null)
                Log.d("Debug", "Should open layout")

                dialog.setContentView(view)
                dialog.show()
                this.setContentView(view)

                val colorGroup = findViewById<ChipGroup>(R.id.colorChipGroup)
                val chipsCount: Int = catGroup.childCount
                Log.e("tag", chipsCount.toString())
                var i = 0
                var chipSelectedIndex :Int

                while (i < chipsCount) {
                    /*val chip = colorGroup.getChildAt(i) as Chip
                    chip.setOnClickListener{
                        Log.e("", chip.chipIconTint.toString())

                        val ids = colorGroup.checkedChipIds
                        if (ids.size > 1) {
                            chip.isChecked = false
                        }
                        if (chip.isChecked) {
                            colorChip=chip
                            colorChip.chipIconTint=chip.chipIconTint
                            chipSelectedIndex=i
                        }
                        else{
                        }
                    }
                    i++*/

                view.btnCreateCat.setOnClickListener{
                    var chip: Chip = Chip(this)
                    chip.text=view.etCatTitle.text.toString()
                    chip.elevation=6f
                    chip.iconStartPadding=12f
                    chip.isCheckable=false
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.setChipIconResource(R.drawable.circle)
                    chip.setTextColor(Color.BLACK)
                    chip.chipIconSize=25f
                    chip.chipIconTint=colorChip.chipIconTint

                    catGroup.addView(chip)
                }
            }


    }*/

    }
    private fun saveData(){
        val allowSwipe = cbSwipeInteractions.isChecked
        Log.d("debug", "Allow Swipe Interaction VAL:$allowSwipe")

        val sharedPreferences = getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("allowSwipe", allowSwipe)
        }.apply()


    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("USER_SETTINGS", Context.MODE_PRIVATE)
        val savedAllowValue = sharedPreferences.getBoolean("allowSwipe", false)
        cbSwipeInteractions.isChecked=savedAllowValue
    }
}