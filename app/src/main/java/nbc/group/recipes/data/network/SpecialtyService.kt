package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.SpecialtyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpecialtyService {
    @GET("SPECIALTY_API")
    suspend fun getSpecialty(
        @Query("query") query: String,  // 검색어
        @Query("display") display: Int,  // 검색 결과 표시 개수
        @Query("start") start: Int,  // 검색 시작 위치
        @Query("sort") sort: String  // 정렬 방식
    ): SpecialtyResponse
}