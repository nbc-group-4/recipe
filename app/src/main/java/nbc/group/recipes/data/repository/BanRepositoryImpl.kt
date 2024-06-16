package nbc.group.recipes.data.repository

import kotlinx.coroutines.flow.Flow
import nbc.group.recipes.data.local.dao.ContentBanDao
import nbc.group.recipes.data.local.dao.UserBanDao
import nbc.group.recipes.data.model.entity.ContentBanEntity
import nbc.group.recipes.data.model.entity.UserBanEntity
import javax.inject.Inject

class BanRepositoryImpl @Inject constructor(
    private val contentBanDao: ContentBanDao,
    private val userBanDao: UserBanDao
): BanRepository {

    override suspend fun getAllContentBan() = contentBanDao.getAllData()

    override suspend fun getAllUserBan() = userBanDao.getAllData()

    override fun insertContentBan(
        target: String
    ) = contentBanDao.insertData(target)

    override fun insertUserBan(
        userBanEntity: UserBanEntity
    ) = userBanDao.insertData(userBanEntity)
}