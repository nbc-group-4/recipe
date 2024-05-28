package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.SpecialtyResponse
import retrofit2.http.GET

interface SpecialtyService {
    @GET(AGRICULTURE_API)
    suspend fun getData(): SpecialtyResponse
}
