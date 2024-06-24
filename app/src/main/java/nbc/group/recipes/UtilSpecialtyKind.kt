package nbc.group.recipes

val specialtyKind = listOf(
    KindItem(
        item = "곡물",
        imageResourceId = R.drawable.ic_home_grain
    ),
    KindItem(
        item = "채소",
        imageResourceId = R.drawable.ic_home_vegetable
    ),
    KindItem(
        item = "과일",
        imageResourceId = R.drawable.ic_home_fruit
    ),
    KindItem(
        item = "어류",
        imageResourceId = R.drawable.ic_home_fish
    ),
    KindItem(
        item = "고기",
        imageResourceId = R.drawable.ic_home_meat
    ),
    KindItem(
        item = "기타",
        imageResourceId = R.drawable.ic_home_etc
    ),
)

data class KindItem(
    val item: String,
    val imageResourceId: Int,
    val isMore: Boolean = false // 더보기 여부 속성
)

data class RecipeItem(
    val item: String,
    val imageResourceId: Int,
)