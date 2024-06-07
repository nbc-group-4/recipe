package nbc.group.recipes.presentation.adapter.diff_util

import androidx.recyclerview.widget.DiffUtil
import nbc.group.recipes.data.model.dto.Recipe

class RecipeDiffUtil: DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }
    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}