package com.teddy.mirandaytoledo.register.data.local

import androidx.room.TypeConverter
import com.teddy.mirandaytoledo.register.domain.PendingRegistrationStatus

class RegisterTypeConverters {
    @TypeConverter
    fun fromPendingStatus(value: PendingRegistrationStatus): String = value.name

    @TypeConverter
    fun toPendingStatus(value: String): PendingRegistrationStatus =
        PendingRegistrationStatus.valueOf(value)
}
