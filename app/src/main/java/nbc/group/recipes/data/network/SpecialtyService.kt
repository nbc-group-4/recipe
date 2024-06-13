package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.SpecialtyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpecialtyService {
    @GET(SPECIALTY_API)
    suspend fun getSpecialty(
        @Query("apiKey") apiKey: String = SPECIALTY_API_KEY, // API 인증키
        @Query("sAreaNm") sAreaNm: String?, // 시도 선택값
        @Query("numOfRows") numOfRows: Int = 200, // 한 페이지 당 제공 건수
        @Query("sText") cntntsSj: String? // 특산물 명
    ): SpecialtyResponse
}
