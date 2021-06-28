package com.example.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource

private lateinit var binding: ActivityMainBinding
private val adapter by lazy { TaskListAdapter() }

class MainActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        updateList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.cmdAddTask.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
            updateList()
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java )
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
            updateList()
        }

        adapter.listenerdelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            updateList()
            adapter.notifyDataSetChanged()
        }

    }

    private fun updateList() {
        binding.lstTasks.adapter = null
        val list = TaskDataSource.getList()
        binding.lstTasks.adapter = adapter
        adapter.submitList(list)
        val emode = if (list.isEmpty())  View.VISIBLE else View.GONE
        val vmode = if (list.isEmpty())  View.GONE else View.VISIBLE
        binding.incEmpty.emptyState.visibility = emode
        binding.lstTasks.visibility = vmode
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}
