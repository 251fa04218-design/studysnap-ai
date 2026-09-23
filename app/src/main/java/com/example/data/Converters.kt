package com.example.data

import androidx.room.TypeConverter
import com.example.model.MaterialType

class Converters {
    @TypeConverter
    fun fromMaterialType(value: MaterialType): String {
        return value.name
    }

    @TypeConverter
    fun toMaterialType(value: String): MaterialType {
        return try {
            MaterialType.valueOf(value)
        } catch (e: Exception) {
            MaterialType.NOTES
        }
    }
}
