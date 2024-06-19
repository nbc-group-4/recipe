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


// 홈퀴즈 어댑터 더미 데이터
val dummyRecipe = listOf(
    RecipeItem(
        item = "1",
        imageResourceId = R.drawable.img_recipe_sample1
    ),
    RecipeItem(
        item = "2",
        imageResourceId = R.drawable.img_recipe_sample2
    ),
    RecipeItem(
        item = "3",
        imageResourceId = R.drawable.img_recipe_sample3
    ),
    RecipeItem(
        item = "4",
        imageResourceId = R.drawable.img_recipe_sample4
    ),
    RecipeItem(
        item = "5",
        imageResourceId = R.drawable.img_recipe_sample5
    ),
    RecipeItem(
        item = "6",
        imageResourceId = R.drawable.img_recipe_sample6
    ),
    RecipeItem(
        item = "7",
        imageResourceId = R.drawable.img_recipe_sample7
    ),
    RecipeItem(
        item = "8",
        imageResourceId = R.drawable.img_recipe_sample8
    ),
)

data class RecipeItem(
    val item: String,
    val imageResourceId: Int,
)