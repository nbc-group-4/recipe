package nbc.group.recipes.presentation.adapter.diff_util

import androidx.recyclerview.widget.DiffUtil
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.entity.RecipeEntity

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem == newItem
    }
}