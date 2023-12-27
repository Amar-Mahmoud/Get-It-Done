package com.example.todolistapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.task_view.view.*
import kotlinx.android.synthetic.main.todo_item.view.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.*


class TodoAdapter(

    private val todoListItems: MutableList<Todo>,
    var numOfTasksFinished: Int =0
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    var itemsChecked=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_item,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "RestrictedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todoListItems[position]
        holder.itemView.apply {

            val helper = context?.let { DBHelper(it) }
            val db = helper?.readableDatabase

            val btnOpenTaskMenu = findViewById<ImageView>(R.id.btnOpenTaskMenu)
            val tvTodoText = findViewById<TextView>(R.id.tvTodoText)
            val tvCategoryText = findViewById<TextView>(R.id.tvCategoryText)
            val tvTodoDescription = findViewById<TextView>(R.id.tvTodoDescription)
            val chipMain = findViewById<Chip>(R.id.chipMain)
            val tvCalendartext = findViewById<TextView>(R.id.tvCalendartext)
            val tvTimetext = findViewById<TextView>(R.id.tvTimetext)
            val expandablePart=findViewById<LinearLayout>(R.id.expandablePart)
            Log.d("debug","From TodoApdater: Inserted ${curTodo.title} , ${curTodo.description} , ${curTodo.categoryTitle.toString()} , ${curTodo.isChecked} , ${curTodo.date} , ${curTodo.time} , Chip Tag: ${curTodo.chip.tag.toString()}  into UI")

            expandablePart.isVisible=false

            tvTodoText.text= curTodo.title
            tvCategoryText.text=curTodo.categoryTitle
            tvTodoDescription.text= curTodo.description
            chipMain.setTextColor(curTodo.chip.textColors)
            chipMain.chipBackgroundColor=curTodo.chip.chipIconTint
            if(curTodo.chipColor!=0)chipMain.setChipBackgroundColorResource(curTodo.chipColor)
            tvCalendartext.text= curTodo.date
            tvTimetext.text=curTodo.time


            if((curTodo.description)!=""){
                btnExpand.isVisible=true

                card.setOnClickListener {
                        expandablePart.isVisible= !expandablePart.isVisible
                }
            }

            chipTimeRemaining.setOnClickListener{
                Toast.makeText(context, "${curTodo.time} at ${curTodo.date}", Toast.LENGTH_SHORT).show()
            }
            if(chipMain.tag!=null)chipMain.setChipBackgroundColorResource(curTodo.chip.tag as Int); Log.d("Debug", "TodoA: "+ curTodo.chip.tag + ", " + curTodo.chip.text.toString())




            if(curTodo.date!="Today") {
                val df: DateTimeFormatter =
                    DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("yyyy-M-dd")
                        .toFormatter(Locale.ENGLISH)

                val current = LocalDate.now()
                val d1 = LocalDate.parse(curTodo.date, df)
                val d2 = LocalDate.parse(current.toString(), df)

                val dateDiff: Long = ChronoUnit.DAYS.between(d2, d1)
                if(dateDiff<2)
                chipTimeRemaining.text="in $dateDiff day"
                else                 chipTimeRemaining.text="in $dateDiff days"

                card.setOnLongClickListener {
                    curTodo.isChecked=!curTodo.isChecked
                    card.isChecked=!card.isChecked

                    toggleCrossedOut(tvTodoText, curTodo.isChecked)

                    if (curTodo.isChecked){
                        itemsChecked+=1
                    }
                    else{
                        itemsChecked-=1

                    }
                    true
                }
            }

            card.setOnLongClickListener {
                curTodo.isChecked=!curTodo.isChecked
                card.isChecked=!card.isChecked

                toggleCrossedOut(tvTodoText, curTodo.isChecked)

                if (curTodo.isChecked){
                    itemsChecked+=1
                }
                else{
                    itemsChecked-=1

                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return todoListItems.size
    }

    private fun setCheckedCount(int: Int){
        itemsChecked = int
    }

    private fun toggleCrossedOut(tvTodoText: TextView, isChecked: Boolean){
        if(isChecked){
            numOfTasksFinished++
            tvTodoText.paintFlags = tvTodoText.paintFlags or STRIKE_THRU_TEXT_FLAG

        }
        else{
            numOfTasksFinished--
            tvTodoText.paintFlags = tvTodoText.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addTodoItem(todo: Todo){
        todoListItems.add(todo)
        notifyItemInserted(todoListItems.size-1)


    }
    fun addTodoItemAtIndex(todo: Todo, index:Int){
        todoListItems.add(index, todo)
        notifyItemInserted(index)


    }


    fun deleteTodoItemAtIndex(position:Int){
        todoListItems.removeAll{ todo ->
            todo.isChecked
        }
        setCheckedCount(0)
        notifyItemRemoved(position)
    }
    fun deleteTodoItem(){
        // TODO:  
        todoListItems.removeAll{ todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }
}

