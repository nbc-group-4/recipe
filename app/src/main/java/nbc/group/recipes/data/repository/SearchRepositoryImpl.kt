package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.SearchResponse
import nbc.group.recipes.data.network.SearchService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    @Named("SearchService") private val searchService: SearchService)
    : SearchRepository{
    override suspend fun requestSearch(query: String): SearchResponse {
        return searchService.getSearch(query = query, page = 1)
    }

}