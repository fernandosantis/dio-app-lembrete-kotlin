package com.example.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    val timeZone = TimeZone.getDefault()
    val offset = timeZone.getOffset(Date().time) * -1
    var dateAdd : Date = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.txtTitle.text = it.title
                binding.txtDescription.text = it.description
                dateAdd = it.date
                binding.txtDate.text = dateAdd.format()
                binding.txtTime.text = it.time
                binding.toolbar.title = getString(R.string.t_EditTask)
                binding.cmdAddTask.text = getString(R.string.t_EditTask)
            }
        }

        insertListeners()
    }


    private fun insertListeners() {
            binding.txtDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                // pega timezone do Dispositivo

                dateAdd = Date(it + offset)
                binding.txtDate.text = dateAdd.format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.txtTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.txtTime.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }

        fun checkError():Boolean {
            return binding.txtTitle.text != ""
        }

        binding.cmdAddTask.setOnClickListener {
            val task = Task(
                title = binding.txtTitle.text,
                description = binding.txtDescription.text,
                date = dateAdd,
                time = binding.txtTime.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            if (checkError()) {
                TaskDataSource.insertTask(task)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                binding.txtTitle.error = getString(R.string.t_invalid_title)
            }

        }

        binding.cmdCancelTask.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
