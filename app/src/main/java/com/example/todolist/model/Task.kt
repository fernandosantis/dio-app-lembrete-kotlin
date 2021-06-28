package com.example.todolist.model

import java.util.*

data class Task(
    val title: String,
    val description: String,
    val time: String,
    val date: Date,
    val id: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
