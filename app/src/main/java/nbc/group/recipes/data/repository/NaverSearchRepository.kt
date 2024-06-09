package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.NaverSearchResponse
import nbc.group.recipes.data.network.NaverSearchService
import javax.inject.Inject
import javax.inject.Named

class NaverSearchRepository @Inject constructor(
    @Named("NaverSearchService")private val  naverSearchService: NaverSearchService
) {
    suspend fun searchEncyclopedia(query: String, clientId: String, clientSecret: String): NaverSearchResponse {
        return naverSearchService.searchNaver(clientId, clientSecret, query)
    }
}