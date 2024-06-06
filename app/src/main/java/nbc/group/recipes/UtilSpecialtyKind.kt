package nbc.group.recipes

val specialtyKind = listOf(
    KindItem(
        item = "곡물류",
        imageResourceId = R.drawable.img_specialty_sample1
    ),
    KindItem(
        item = "채소류",
        imageResourceId = R.drawable.img_specialty_sample2
    ),
    KindItem(
        item = "과일류",
        imageResourceId = R.drawable.img_specialty_sample3
    ),
    KindItem(
        item = "어류",
        imageResourceId = R.drawable.img_specialty_sample4
    ),
    KindItem(
        item = "해조류",
        imageResourceId = R.drawable.img_specialty_sample5
    ),
    KindItem(
        item = "고기류",
        imageResourceId = R.drawable.img_specialty_sample6
    ),
)

// 더보기 아이템
val specialtyKindMore = listOf(
    KindItem(
        item = "쌀",
        imageResourceId = R.drawable.img_specialty_sample7
    ),
    KindItem(
        item = "기타",
        imageResourceId = R.drawable.img_specialty_sample8
    )
)

data class KindItem(
    val item: String,
    val imageResourceId: Int,
    val isMore: Boolean = false // 더보기 여부를 나타내는 속성
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