package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchService {
    @GET("v2/local/search/address")
    @Headers("Authorization: KakaoAK ${SEARCH_REST_API_KEY}")

    suspend fun getSearch(
        @Query("query") query : String,
        @Query("page") page : Int
    ): SearchResponse

}