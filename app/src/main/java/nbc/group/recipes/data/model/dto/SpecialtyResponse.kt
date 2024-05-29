package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName

data class SpecialtyResponse(
    @SerializedName("total") val total: Int?,  // 총 검색 결과 개수
    @SerializedName("start") val start: Int?,  // 검색 시작 위치
    @SerializedName("display") val display: Int?,  // 한 번에 표시할 검색 결과 개수
    @SerializedName("item") val item: List<SpecialtyItem>?  // 개별 검색 결과
)

data class SpecialtyItem(
    @SerializedName("title") val title: String, // 상품 이름
    @SerializedName("link") val link: String, // 상품 정보 URL //
    @SerializedName("image") val image: String, // 섬네일 이미지의 URL
//    @SerializedName("productId") val productId: Int, // 네이버 쇼핑의 상품 ID
//    @SerializedName("PRODUCT_TYPE") val productType: Int, // 상품군과 상품 종류에 따른 상품 타입
    @SerializedName("category1") val category1: String, // 상품의 카테고리(대분류)
    @SerializedName("category2") val category2: String, // 상품의 카테고리(중분류)
    @SerializedName("category3") val ingredientName: String, // 상품의 카테고리(소분류) // ingredientName
    @SerializedName("category4") val ingredientName2: String // 상품의 카테고리(세분류) // ingredientName
)