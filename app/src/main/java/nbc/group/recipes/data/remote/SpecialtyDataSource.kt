package nbc.group.recipes.data.remote

import nbc.group.recipes.data.network.SpecialtyService
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named

class SpecialtyDataSource @Inject constructor(
    @Named("SpecialtyService") private val service: SpecialtyService
) {
//    suspend fun getSpecialty(
//        query: String = "검색 특산물",
//        display: Int = 10,
//        start: Int = 1,
//        sort: String = "sim",
//    ) = service.getSpecialty(
//        query = query,
//        display = display,
//        start = start,
//        sort = sort
//    )
}