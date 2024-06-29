package nbc.group.recipes.presentation.graph

import android.graphics.Bitmap
import nbc.group.recipes.data.model.entity.RecipeEntity

data class Node <T> (
    val x: Float,
    val y: Float,
    val dx: Float,
    val dy: Float,
    val size: Float,
    val weight: Float,
    val bitmap: Bitmap?,
    val data: T
)
