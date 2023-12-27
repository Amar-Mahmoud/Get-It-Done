package com.example.todolistapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.customcategory.*
import kotlinx.android.synthetic.main.customcategory.view.*
import kotlinx.android.synthetic.main.customcategory.view.btnAddLink
import kotlinx.android.synthetic.main.task_view.view.*
import java.time.*
import java.util.*


class ModalBottomSheet : BottomSheetDialogFragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.customcategory, container, false)
        val buttonCreateTask = view.btnAddTask
        val taskTitle = view.etTodoName2
        val taskDescription = view.etTodoDescription
        var timeSelected = "12:00"
        var dateSelected = "Today"
        val chipGroupCat2 = view.category_chip_group
        val openCalendar = view.btnAddDate
        val openUrgency = view.btnToggleUrgency
        val btnAddLink = view.btnAddLink
        val chipGroup = view.chipGroup
        var chipCategory :Chip = Chip(context)
        chipCategory.setChipIconTintResource(R.color.other_color)
        chipCategory.text="Other"
        chipCategory.tag = R.color.other_color

        val helper = context?.let { DBHelper(it) }
        val db = helper?.readableDatabase
        val rs2 = db?.rawQuery("SELECT * FROM CATEGORIES", null)
        val colorsMap = mapOf("Blue" to R.color.blue, "Green" to R.color.pure_green, "Red" to R.color.pure_red, "OffWhite" to R.color.offwhite, "Yellow" to R.color.pure_yellow, "LightGreen" to R.color.green, "Purple" to R.color.purple, "Navy" to R.color.lightblue_1, "Pink" to R.color.pink, "DarkGreen" to R.color.pure_green, "LightPink" to R.color.shrine_pink_50);


        if (rs2 != null) {
            if(rs2.moveToFirst())
                do {
                    val chip: Chip = Chip(context)
                    chip.text=rs2.getString(1)
                    chip.elevation=2f
                    chip.iconStartPadding=12f
                    chip.isCheckable=true
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.setChipIconResource(R.drawable.circle)
                    chip.setTextColor(Color.BLACK)
                    chip.chipIconSize=35f
                    chip.chipCornerRadius=25f
                    colorsMap[rs2.getString(2)]?.let { chip.tag = it }
                    colorsMap[rs2.getString(2)]?.let { chip.setChipIconTintResource(it) }
                    Log.d("Debug", "Set the color for chip: " + chip.text.toString())
                    chipGroupCat2.addView(chip)
                }
                while (rs2.moveToNext())
        }

        rs2?.close()

        val chipsCount: Int = chipGroupCat2.childCount
        var i = 0
        var chipSelectedIndex :Int

        while (i < chipsCount) {
            val chip = chipGroupCat2.getChildAt(i) as Chip
            chip.setOnClickListener{

                val ids = chipGroupCat2.checkedChipIds
                if (ids.size > 1) {
                    chip.isChecked = false
                }
                if (chip.isChecked) {
                    chipCategory=chip
                    chipSelectedIndex=i
                    chipCategory.tag=chip.tag
                    Log.d("Debug", "Selected Category: "+ chipCategory.text.toString() +" "+ chipCategory.tag.toString())
                }
            }
            i++
        }
                btnAddLink.setOnClickListener {
                    btnAddLink.setBackgroundResource(R.drawable.avd_anim_link
                    )
                    val avdOpenMenu: AnimatedVectorDrawable =
                        btnAddLink.background as AnimatedVectorDrawable
                    avdOpenMenu.start()

                }
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Select time")
                .setHour(12)
                .setTheme(R.style.ThemeOverlay_App_TimePicker)
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()


        buttonCreateTask.setOnClickListener {
            if ((taskTitle.text.toString()).isNotEmpty() &&(chipCategory.text)!="Important") {
                if (db != null) {
                    (activity as MainActivity?)?.addTask(taskTitle.text.toString(),taskDescription.text.toString() ,chipCategory.text as String, dateSelected,timeSelected, chipCategory, db)
                    if (chipCategory.tag!=null)Log.d("Debug", chipCategory.tag.toString())
                }
                taskTitle.text?.clear()
                taskDescription.text?.clear()

                dismiss()

            }
            else {

                if((taskTitle.text.toString()).isNotEmpty() &&chipCategory.text=="Important"){
                    if (db != null) {
                        (activity as MainActivity?)?.addTaskAtIndex(taskTitle.text.toString(),taskDescription.text.toString(),
                            chipCategory.text as String, dateSelected,timeSelected, chipCategory,0,db)
                    }
                    taskTitle.text?.clear()
                    taskDescription.text?.clear()
                    dismiss()
                }
            }
        }
        openCalendar.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
            }


        datePicker.addOnPositiveButtonClickListener {
            timePicker.show(parentFragmentManager, "tag")

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            dateSelected = "${calendar.get(Calendar.YEAR)}-" +
                    "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"

            val colorInt = resources.getColor(android.R.color.darker_gray)
            val csl = ColorStateList.valueOf(colorInt)

            val chip = Chip(context)
            chip.chipText = dateSelected
            chip.isCloseIconEnabled = true
            chip.setChipBackgroundColorResource(R.color.white)
            chip.elevation = 0F
            chip.chipStrokeWidth=2f
            chip.chipStrokeColor=csl
            chip.setTextColor(Color.BLACK)
            chip.setChipIconResource(R.drawable.calendar)
            chip.setCloseIconTintResource(R.color.black)
            chip.setChipIconTintResource(R.color.black)
            chip.chipCornerRadius = 4F
            //chipGroup.addView(chip)
            chip.setOnCloseIconClickListener {
                chipGroup.removeView(chip)
                openCalendar.isVisible = true
            }
        }

        timePicker.addOnPositiveButtonClickListener {
            val colorInt = resources.getColor(android.R.color.darker_gray)
            val csl = ColorStateList.valueOf(colorInt)
            //openCalendar.chipIcon=resources.getDrawable(R.drawable.tt_edit)
            if(timePicker.hour<10&&timePicker.minute>10){
                timeSelected = "0"+timePicker.hour.toString() + ":" + timePicker.minute.toString()

            }
            else if(timePicker.hour<10&&timePicker.minute<10){
                timeSelected = "0"+timePicker.hour.toString() + ":"+ "0"+ timePicker.minute.toString()

            }
            else if(timePicker.minute<10&&timePicker.hour>10){
                timeSelected =timePicker.hour.toString() + ":"+ "0"+ timePicker.minute.toString()

            }


            else{
            timeSelected = timePicker.hour.toString() + ":" + timePicker.minute.toString()
        }
            openCalendar.text="$dateSelected  $timeSelected"
            val chip = Chip(context)
            chip.text = timeSelected
            chip.isCloseIconVisible = true
            chip.setChipIconResource(R.drawable.clock_1)
            chip.setChipIconTintResource(R.color.black)
            chip.chipStrokeWidth=2f
            chip.chipStrokeColor=csl
            chip.setCloseIconTintResource(R.color.black)
            chip.setChipBackgroundColorResource(R.color.white)
            chip.elevation = 0F
            chip.setTextColor(Color.BLACK)
            chip.chipCornerRadius = 4F
            //chipGroup.addView(chip)
            chip.setOnCloseIconClickListener {
                chipGroup.removeView(chip)

            }
        }
        return view
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }

 }













