package com.teddy.mirandaytoledo.register.data.repository

import com.teddy.mirandaytoledo.catalog.frames.finishes.domain.usecase.GetFinishesUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishColorRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.restrictions.domain.usecase.GetFrameModelFinishRelationsUseCase
import com.teddy.mirandaytoledo.catalog.frames.sizes.domain.usecase.GetSizesUseCase
import com.teddy.mirandaytoledo.catalog.prices.pricerules.domain.usecase.GetPriceRulesUseCase
import com.teddy.mirandaytoledo.catalog.prices.producttypes.domain.usecase.GetProductTypesUseCase
import com.teddy.mirandaytoledo.catalog.scholar.educationallevel.domain.usecase.GetEducationalLevelsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.groups.domain.usecase.GetSchoolGroupsUseCase
import com.teddy.mirandaytoledo.catalog.scholar.schools.domain.usecase.GetSchoolsUseCase
import com.teddy.mirandaytoledo.core.domain.util.EmptyResult
import com.teddy.mirandaytoledo.core.domain.util.NetworkError
import com.teddy.mirandaytoledo.core.domain.util.Result
import com.teddy.mirandaytoledo.register.data.dto.CreateOrderRegistrationRequestDto
import com.teddy.mirandaytoledo.register.data.local.FrameModelFinishColorRelationCacheEntity
import com.teddy.mirandaytoledo.register.data.local.FrameModelFinishRelationCacheEntity
import com.teddy.mirandaytoledo.register.data.local.PendingRegistrationDao
import com.teddy.mirandaytoledo.register.data.local.PendingRegistrationEntity
import com.teddy.mirandaytoledo.register.data.local.PriceRuleCacheEntity
import com.teddy.mirandaytoledo.register.data.local.RegisterCatalogDao
import com.teddy.mirandaytoledo.register.data.local.toCache
import com.teddy.mirandaytoledo.register.data.local.toDomain
import com.teddy.mirandaytoledo.register.domain.OrderRegistrationResult
import com.teddy.mirandaytoledo.register.domain.PendingRegistration
import com.teddy.mirandaytoledo.register.domain.PendingRegistrationStatus
import com.teddy.mirandaytoledo.register.domain.RegisterCatalogBundle
import com.teddy.mirandaytoledo.register.domain.RegisterOfflineRepository
import com.teddy.mirandaytoledo.register.domain.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class RegisterOfflineRepositoryImpl(
    private val catalogDao: RegisterCatalogDao,
    private val pendingDao: PendingRegistrationDao,
    private val json: Json,
    private val submitRepository: RegisterRepository,
    private val getEducationalLevelsUseCase: GetEducationalLevelsUseCase,
    private val getSchoolsUseCase: GetSchoolsUseCase,
    private val getSchoolGroupsUseCase: GetSchoolGroupsUseCase,
    private val getProductTypesUseCase: GetProductTypesUseCase,
    private val getFinishesUseCase: GetFinishesUseCase,
    private val getSizesUseCase: GetSizesUseCase,
    private val getFrameModelFinishRelationsUseCase: GetFrameModelFinishRelationsUseCase,
    private val getFrameModelFinishColorRelationsUseCase: GetFrameModelFinishColorRelationsUseCase,
    private val getPriceRulesUseCase: GetPriceRulesUseCase
) : RegisterOfflineRepository {

    override fun observeCatalogBundle(): Flow<RegisterCatalogBundle> {
        val scholarBundle = combine(
            catalogDao.observeEducationalLevels(),
            catalogDao.observeSchools(),
            catalogDao.observeSchoolGroups(),
        ) { educationalLevels, schools, schoolGroups ->
            Triple(educationalLevels, schools, schoolGroups)
        }

        val pricingBundle = combine(
            catalogDao.observeProductTypes(),
            catalogDao.observeFinishes(),
            catalogDao.observeSizes()
        ) { productTypes, finishes, sizes ->
            Triple(productTypes, finishes, sizes)
        }

        val restrictionBundle = combine(
            catalogDao.observeFrameModelFinishRelations(),
            catalogDao.observeFrameModelFinishColorRelations(),
            catalogDao.observePriceRules()
        ) { relations, colorRelations, priceRules ->
            RestrictionBundle(
                relations = relations,
                colorRelations = colorRelations,
                priceRules = priceRules
            )
        }

        return combine(scholarBundle, pricingBundle, restrictionBundle) { scholar, pricing, restrictions ->
            RegisterCatalogBundle(
                educationalLevels = scholar.first.map { it.toDomain() },
                schools = scholar.second.map { it.toDomain() },
                schoolGroups = scholar.third.map { it.toDomain() },
                productTypes = pricing.first.map { it.toDomain() },
                finishes = pricing.second.map { it.toDomain() },
                sizes = pricing.third.map { it.toDomain() },
                frameModelFinishRelations = restrictions.relations.map { it.toDomain() },
                frameModelFinishColorRelations = restrictions.colorRelations.map { it.toDomain() },
                priceRules = restrictions.priceRules.map { it.toDomain() }
            )
        }
    }

    override fun observePendingRegistrations(): Flow<List<PendingRegistration>> {
        return pendingDao.observePendingRegistrations().map { items ->
            items.map { it.toDomain() }
        }
    }

    override suspend fun syncCatalogs(): EmptyResult<NetworkError> {
        val educationalLevels = when (val result = getEducationalLevelsUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val schools = when (val result = getSchoolsUseCase(page = 1, pageSize = 500)) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val schoolGroups = when (val result = getSchoolGroupsUseCase(page = 1, pageSize = 500)) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val productTypes = when (val result = getProductTypesUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val finishes = when (val result = getFinishesUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val sizes = when (val result = getSizesUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val relations = when (val result = getFrameModelFinishRelationsUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val colorRelations = when (val result = getFrameModelFinishColorRelationsUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }
        val priceRules = when (val result = getPriceRulesUseCase()) {
            is Result.Error -> return Result.Error(result.error)
            is Result.Success -> result.data
        }

        catalogDao.replaceAll(
            educationalLevels = educationalLevels.map { it.toCache() },
            schools = schools.map { it.toCache() },
            schoolGroups = schoolGroups.map { it.toCache() },
            productTypes = productTypes.map { it.toCache() },
            finishes = finishes.map { it.toCache() },
            sizes = sizes.map { it.toCache() },
            frameModelFinishRelations = relations.map { it.toCache() },
            frameModelFinishColorRelations = colorRelations.map { it.toCache() },
            priceRules = priceRules.map { it.toCache() }
        )

        return Result.Success(Unit)
    }

    override suspend fun savePendingRegistration(
        request: CreateOrderRegistrationRequestDto,
        studentFullName: String,
        schoolLabel: String,
        productTypeName: String
    ): Long {
        return pendingDao.insert(
            PendingRegistrationEntity(
                payloadJson = json.encodeToString(CreateOrderRegistrationRequestDto.serializer(), request),
                studentFullName = studentFullName,
                schoolLabel = schoolLabel,
                productTypeName = productTypeName,
                createdAt = System.currentTimeMillis(),
                status = PendingRegistrationStatus.PendingSync,
                lastErrorMessage = null,
                lastAttemptAt = null,
                syncedOrderId = null
            )
        )
    }

    override suspend fun submitPendingRegistration(localId: Long): Result<OrderRegistrationResult, NetworkError> {
        val pending = pendingDao.getById(localId) ?: return Result.Error(NetworkError.UNKNOWN)
        val payload = json.decodeFromString(CreateOrderRegistrationRequestDto.serializer(), pending.payloadJson)

        pendingDao.updateStatus(
            localId = localId,
            status = PendingRegistrationStatus.Syncing,
            lastErrorMessage = null,
            lastAttemptAt = System.currentTimeMillis(),
            syncedOrderId = null
        )

        return when (val result = submitRepository.submitRegistration(payload)) {
            is Result.Success -> {
                pendingDao.updateStatus(
                    localId = localId,
                    status = PendingRegistrationStatus.Synced,
                    lastErrorMessage = null,
                    lastAttemptAt = System.currentTimeMillis(),
                    syncedOrderId = result.data.orderId
                )
                Result.Success(result.data)
            }

            is Result.Error -> {
                pendingDao.updateStatus(
                    localId = localId,
                    status = PendingRegistrationStatus.Failed,
                    lastErrorMessage = result.error.name,
                    lastAttemptAt = System.currentTimeMillis(),
                    syncedOrderId = null
                )
                Result.Error(result.error)
            }
        }
    }

    override suspend fun deletePendingRegistration(localId: Long) {
        pendingDao.deleteById(localId)
    }
}

private data class RestrictionBundle(
    val relations: List<FrameModelFinishRelationCacheEntity>,
    val colorRelations: List<FrameModelFinishColorRelationCacheEntity>,
    val priceRules: List<PriceRuleCacheEntity>
)
