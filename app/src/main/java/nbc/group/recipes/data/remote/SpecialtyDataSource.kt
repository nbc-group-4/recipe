package nbc.group.recipes.data.remote

import nbc.group.recipes.data.network.SpecialtyService
import javax.inject.Inject
import javax.inject.Named

class SpecialtyDataSource @Inject constructor(
    @Named("SpecialtyService") private val service: SpecialtyService
) {
    suspend fun getSpecialty(
        sAreaNm: String? = "",
        cntntsSj: String? = ""
    ) = service.getSpecialty(
        sAreaNm = sAreaNm ?: "",
        cntntsSj = cntntsSj ?: ""
    )
}