package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.SearchResponse

interface SearchRepository {
    suspend fun requestSearch(query : String) : SearchResponse
}