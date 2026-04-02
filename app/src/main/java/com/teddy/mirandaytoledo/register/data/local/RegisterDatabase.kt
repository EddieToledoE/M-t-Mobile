package com.teddy.mirandaytoledo.register.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        EducationalLevelCacheEntity::class,
        SchoolCacheEntity::class,
        SchoolGroupCacheEntity::class,
        ProductTypeCacheEntity::class,
        FinishCacheEntity::class,
        SizeCacheEntity::class,
        FrameModelFinishRelationCacheEntity::class,
        FrameModelFinishColorRelationCacheEntity::class,
        PriceRuleCacheEntity::class,
        PendingRegistrationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RegisterTypeConverters::class)
abstract class RegisterDatabase : RoomDatabase() {
    abstract fun registerCatalogDao(): RegisterCatalogDao
    abstract fun pendingRegistrationDao(): PendingRegistrationDao
}
