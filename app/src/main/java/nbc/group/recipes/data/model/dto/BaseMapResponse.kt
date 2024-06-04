package nbc.group.recipes.data.model.dto

data class BaseMapResponse (
    val id : Int,
    val regionName : String,
    val x_longitude : Double,
    val y_latitude : Double,
)