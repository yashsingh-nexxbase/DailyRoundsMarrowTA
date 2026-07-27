package com.example.dailroundsmarrowassessment1.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json

    @TypeConverter
    fun fromAnswers(answers: List<Int>): String = answers.joinToString(",")

    @TypeConverter
    fun toAnswers(raw: String): List<Int> =
        if (raw.isBlank()) emptyList() else raw.split(",").map { it.toInt() }

    @TypeConverter
    fun fromOptions(options: List<String>): String = json.encodeToString(options)

    @TypeConverter
    fun toOptions(raw: String): List<String> = json.decodeFromString(raw)
}
