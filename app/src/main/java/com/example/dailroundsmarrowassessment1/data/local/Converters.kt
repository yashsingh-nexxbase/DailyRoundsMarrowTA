package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromAnswers(answers: List<Int>): String = answers.joinToString(",")

    @TypeConverter
    fun toAnswers(raw: String): List<Int> =
        if (raw.isBlank()) emptyList() else raw.split(",").map { it.toInt() }
}
