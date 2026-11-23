package com.example.myshiftapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Формат: "2026-11-24"
    val type: String, // "День", "Ночь", "Выходной"
    val time: String  // "08:00 - 20:00"
)
