package nbc.group.recipes.data.remote

import nbc.group.recipes.data.network.SpecialtyService
import javax.inject.Inject
import javax.inject.Named

class SpecialtyDataSource @Inject constructor(
    @Named("SpecialtyService") private val service: SpecialtyService
) {
    suspend fun getData() = service.getData()
}
