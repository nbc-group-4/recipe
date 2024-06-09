package nbc.group.recipes.data.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    val recipeImg: String,
    val recipeName: String,
    @PrimaryKey val id: Int,
    val explain: String,
    val step: String,
    val ingredient: String,
    val difficulty: String,
    val time: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(recipeImg)
        parcel.writeString(recipeName)
        parcel.writeInt(id)
        parcel.writeString(explain)
        parcel.writeString(step)
        parcel.writeString(ingredient)
        parcel.writeString(difficulty)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeEntity> {
        override fun createFromParcel(parcel: Parcel): RecipeEntity {
            return RecipeEntity(parcel)
        }

        override fun newArray(size: Int): Array<RecipeEntity?> {
            return arrayOfNulls(size)
        }
    }

}
