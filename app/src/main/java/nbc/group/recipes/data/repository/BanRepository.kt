package nbc.group.recipes.data.repository

import kotlinx.coroutines.flow.Flow
import nbc.group.recipes.data.local.dao.UserBanDao
import nbc.group.recipes.data.model.entity.ContentBanEntity
import nbc.group.recipes.data.model.entity.UserBanEntity

interface BanRepository {
    suspend fun getAllContentBan(): Flow<List<ContentBanEntity>>
    suspend fun getAllUserBan(): Flow<List<UserBanEntity>>
    fun insertContentBan(target: String): Long
    fun insertUserBan(userBanEntity: UserBanEntity): Long
}