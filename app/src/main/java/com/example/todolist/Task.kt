package com.example.todolist

data class Task(
    val title: String,
    val description: String,
    val hour:String,
    val date:String,
    val id: Int = 0
)
