package nbc.group.recipes.presentation.graph

import android.graphics.Bitmap

fun adjustImage(
    bitmap: Bitmap,
    nodeWidth: Int,
    nodeHeight: Int
): Bitmap {
    return Bitmap.createScaledBitmap(
        centerCropImage(bitmap),
        nodeWidth,
        nodeHeight,
        true
    )
}

private fun centerCropImage(source: Bitmap): Bitmap {
    val width = source.width
    val height = source.height
    return if (width > height) {
        Bitmap.createBitmap(
            source, (width - height) / 2, 0, height, height
        )
    } else if(width < height) {
        Bitmap.createBitmap(
            source, 0, (height - width) / 2, width, width
        )
    } else {
        Bitmap.createBitmap(
            source, 0, 0, width, width
        )
    }
}