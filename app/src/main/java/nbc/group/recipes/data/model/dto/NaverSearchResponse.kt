package nbc.group.recipes.data.model.dto

import nbc.group.recipes.data.model.entity.NaverSearchEntity

data class NaverSearchResponse(
    val items: List<NaverSearchEntity>
)