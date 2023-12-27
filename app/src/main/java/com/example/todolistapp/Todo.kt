package com.example.todolistapp

import com.google.android.material.chip.Chip

data class Todo(
    val title: String,
    val description: String,
    val categoryTitle: String,
    val date: String,
    val time:String,
    var chip: Chip,
    var chipColor: Int = 0,
    var isChecked: Boolean = false


)


