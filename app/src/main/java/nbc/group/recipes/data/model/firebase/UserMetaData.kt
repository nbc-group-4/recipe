package nbc.group.recipes.data.model.firebase

import nbc.group.recipes.data.model.dto.Recipe

data class UserMetaData(
    val recipeIds: List<String>
) {
    constructor(): this(listOf())
}
