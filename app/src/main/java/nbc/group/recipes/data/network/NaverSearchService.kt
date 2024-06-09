package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.NaverSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverSearchService {
    @GET("v1/search/encyc.json")
    suspend fun searchNaver(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int = 1,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "sim"
    ): NaverSearchResponse
}