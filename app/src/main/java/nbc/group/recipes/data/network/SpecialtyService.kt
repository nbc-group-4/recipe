package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.SpecialtyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpecialtyService {
    @GET(SPECIALTY_API)
    suspend fun getSpecialty(
        @Query("API_KEY") apiKey: String = SPECIALTY_API_KEY, // API 인증키
        @Query("sText") sText: String,  // 검색어
        @Query("sAreaNm") sAreaNm: String,  // 시도 선택값
        @Query("sAreaCode") sAreaCode: String,  // 시군구 선택값
        @Query("pageNo") pageNo: Int,  // 조회할 페이지 번호
        @Query("numOfRows") numOfRows: Int  // 한 페이지 당 제공 건수
    ): SpecialtyResponse
}