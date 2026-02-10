package com.manraap.permanentlearning.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromRecallQuality(value: RecallQuality?): String? = value?.name

    @TypeConverter
    fun toRecallQuality(value: String?): RecallQuality? = value?.let { RecallQuality.valueOf(it) }
}
