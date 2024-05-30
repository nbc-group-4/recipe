package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response")
data class SpecialtyResponse(
    @Element val header: Header,
    @Element val body: Body,
)

@Xml(name = "header")
data class Header(
    @PropertyElement val resultCode: String,
    @PropertyElement val resultMsg: String,
    @Element val requestParameter: RequestParameter,
)

@Xml(name = "requestParameter")
data class RequestParameter(
    @PropertyElement val sAreaNm: String,
)

@Xml(name = "body")
data class Body(
    @Element val items: Items,
    @PropertyElement val numOfRows: Int,
    @PropertyElement val pageNo: Int,
    @PropertyElement val totalCount: Int,
)

@Xml(name = "items")
data class Items(
    @Element(name = "item") val item: List<Item>
)

@Xml
data class Item(
    @PropertyElement(name="areaNm") var areaName: String?,
    @PropertyElement(name="cntntsNo") var cntntsNo : Int?,
    @PropertyElement(name="cntntsSj") var cntntsSj : String?,
    @PropertyElement(name="imgUrl") var imgUrl : String?,
    @PropertyElement(name="linkUrl") var linkUrl: String?,
    @PropertyElement(name="svcDt") var svcDt: String?
)
