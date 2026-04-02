package com.teddy.mirandaytoledo.register.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.teddy.mirandaytoledo.register.domain.PendingRegistrationStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RegisterCatalogDao {
    @Query("SELECT * FROM educational_levels_cache ORDER BY name")
    fun observeEducationalLevels(): Flow<List<EducationalLevelCacheEntity>>

    @Query("SELECT * FROM schools_cache ORDER BY name")
    fun observeSchools(): Flow<List<SchoolCacheEntity>>

    @Query("SELECT * FROM school_groups_cache ORDER BY schoolName, groupCode")
    fun observeSchoolGroups(): Flow<List<SchoolGroupCacheEntity>>

    @Query("SELECT * FROM product_types_cache ORDER BY name")
    fun observeProductTypes(): Flow<List<ProductTypeCacheEntity>>

    @Query("SELECT * FROM finishes_cache ORDER BY name")
    fun observeFinishes(): Flow<List<FinishCacheEntity>>

    @Query("SELECT * FROM sizes_cache ORDER BY sizeGroup, name")
    fun observeSizes(): Flow<List<SizeCacheEntity>>

    @Query("SELECT * FROM frame_model_finish_relations_cache ORDER BY frameModelName, finishName")
    fun observeFrameModelFinishRelations(): Flow<List<FrameModelFinishRelationCacheEntity>>

    @Query("SELECT * FROM frame_model_finish_color_relations_cache ORDER BY frameModelName, finishName, colorName")
    fun observeFrameModelFinishColorRelations(): Flow<List<FrameModelFinishColorRelationCacheEntity>>

    @Query("SELECT * FROM price_rules_cache ORDER BY productTypeName, finishName, sizeName")
    fun observePriceRules(): Flow<List<PriceRuleCacheEntity>>

    @Query("DELETE FROM educational_levels_cache")
    suspend fun clearEducationalLevels()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducationalLevels(items: List<EducationalLevelCacheEntity>)

    @Query("DELETE FROM schools_cache")
    suspend fun clearSchools()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchools(items: List<SchoolCacheEntity>)

    @Query("DELETE FROM school_groups_cache")
    suspend fun clearSchoolGroups()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchoolGroups(items: List<SchoolGroupCacheEntity>)

    @Query("DELETE FROM product_types_cache")
    suspend fun clearProductTypes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductTypes(items: List<ProductTypeCacheEntity>)

    @Query("DELETE FROM finishes_cache")
    suspend fun clearFinishes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinishes(items: List<FinishCacheEntity>)

    @Query("DELETE FROM sizes_cache")
    suspend fun clearSizes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSizes(items: List<SizeCacheEntity>)

    @Query("DELETE FROM frame_model_finish_relations_cache")
    suspend fun clearFrameModelFinishRelations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFrameModelFinishRelations(items: List<FrameModelFinishRelationCacheEntity>)

    @Query("DELETE FROM frame_model_finish_color_relations_cache")
    suspend fun clearFrameModelFinishColorRelations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFrameModelFinishColorRelations(items: List<FrameModelFinishColorRelationCacheEntity>)

    @Query("DELETE FROM price_rules_cache")
    suspend fun clearPriceRules()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceRules(items: List<PriceRuleCacheEntity>)

    @Transaction
    suspend fun replaceAll(
        educationalLevels: List<EducationalLevelCacheEntity>,
        schools: List<SchoolCacheEntity>,
        schoolGroups: List<SchoolGroupCacheEntity>,
        productTypes: List<ProductTypeCacheEntity>,
        finishes: List<FinishCacheEntity>,
        sizes: List<SizeCacheEntity>,
        frameModelFinishRelations: List<FrameModelFinishRelationCacheEntity>,
        frameModelFinishColorRelations: List<FrameModelFinishColorRelationCacheEntity>,
        priceRules: List<PriceRuleCacheEntity>
    ) {
        clearEducationalLevels()
        insertEducationalLevels(educationalLevels)
        clearSchools()
        insertSchools(schools)
        clearSchoolGroups()
        insertSchoolGroups(schoolGroups)
        clearProductTypes()
        insertProductTypes(productTypes)
        clearFinishes()
        insertFinishes(finishes)
        clearSizes()
        insertSizes(sizes)
        clearFrameModelFinishRelations()
        insertFrameModelFinishRelations(frameModelFinishRelations)
        clearFrameModelFinishColorRelations()
        insertFrameModelFinishColorRelations(frameModelFinishColorRelations)
        clearPriceRules()
        insertPriceRules(priceRules)
    }
}

@Dao
interface PendingRegistrationDao {
    @Query("SELECT * FROM pending_registrations WHERE status != 'Synced' ORDER BY createdAt DESC")
    fun observePendingRegistrations(): Flow<List<PendingRegistrationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PendingRegistrationEntity): Long

    @Query("SELECT * FROM pending_registrations WHERE localId = :localId LIMIT 1")
    suspend fun getById(localId: Long): PendingRegistrationEntity?

    @Query(
        """
        UPDATE pending_registrations
        SET status = :status,
            lastErrorMessage = :lastErrorMessage,
            lastAttemptAt = :lastAttemptAt,
            syncedOrderId = :syncedOrderId
        WHERE localId = :localId
        """
    )
    suspend fun updateStatus(
        localId: Long,
        status: PendingRegistrationStatus,
        lastErrorMessage: String?,
        lastAttemptAt: Long?,
        syncedOrderId: Int?
    )

    @Query("DELETE FROM pending_registrations WHERE localId = :localId")
    suspend fun deleteById(localId: Long)
}
