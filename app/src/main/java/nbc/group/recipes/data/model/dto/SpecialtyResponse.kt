package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName

data class SpecialtyResponse(
    @SerializedName("cntntsNo") val cntntsNo: Int?,  // 특산물 번호(키)
    @SerializedName("cntntsSj") val cntntsSj: String?,  // 특산물 명
    @SerializedName("areaNm") val areaNm: String?,  // 지역명
    @SerializedName("imgUrl") val imgUrl: String?,  // 이미지 URL
    @SerializedName("svcDt") val svcDt: Int?,  // 등록일
    @SerializedName("linkUrl") val linkUrl: String?,  // 관련 사이트 URL
)