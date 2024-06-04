package nbc.group.recipes

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.io.InputStream

val specialties = listOf(
    "취나물", "미나리", "잎들깨", "오이", "부추", "깻잎", "대파", "배추", "미나리", "고추", "쑥", "상추", "고사리",
    "잣", "오디", "두릅", "가시오가피", "곰취", "삼백초", "녹차", "눈개승마", "어성초", "피망", "곤드레",
    "파프리카", "시래기", "가지", "쪽파", "명이", "부지갱이", "미역취", "서덜취", "머위", "음나무", "차나무", "여주",
    "양상추", "시금치", "박나물", "치자", "알로에", "브로콜리", "고구마줄기", "고구마", "감자", "당근", "연근", "마늘",
    "양파", "무", "도라지", "인삼", "순무", "돼지감자", "더덕", "산삼", "생강", "마", "천마", "토란", "산양삼",
    "우엉", "쌀", "찹쌀", "흑미", "현미", "율무", "콩", "들깨", "호두", "참외", "멜론", "체리", "복숭아", "포도",
    "사과", "수박", "단감", "블루베리", "토마토", "딸기", "감", "배", "복숭아", "다래", "구기자", "오미자", "대추",
    "복분자", "무화과", "한라봉", "유자", "대봉", "자두", "망고", "애플망고", "용과", "키위", "매실", "산딸기",
    "단호박", "호박", "애호박", "옥수수", "밤", "쥬키니", "붕장어", "곰장어", "갈치", "멸치", "장어", "숭어", "황복",
    "참게", "코다리", "오징어", "전어", "은어", "철갑상어", "쥐치", "조개", "가리비", "전복", "새조개", "바지락",
    "굴", "개조개", "문어", "대게", "황태", "어란", "다시마", "미역", "김", "한우", "곤충", "꿀", "닭", "오리",
    "밀가루", "잡곡", "양돈", "녹돈", "돼지", "유정란", "율란", "달걀", "송이", "표고", "느타리", "영지",
    "노루궁뎅이", "상황", "양송이", "팽이버섯", "새송이버섯",
)

//fun convertToOfficial(target: String): String {
//    var result = ""
//    var length = 0
//    specialties.forEach { specialty ->
//        if(target.contains(specialty)) {
//            if(length < specialty.length) {
//                result = specialty
//                length = specialty.length
//            }
//        }
//    }
//    if (length == 0) throw Exception("There is no matching ingredient error!")
//    return result
//}


//private fun convertToOfficial(target: String, categorySpecialties: List<String>): String {
//    var result = ""
//    var length = 0
//    categorySpecialties.forEach { specialty ->
//        if (target.contains(specialty)) {
//            if (length < specialty.length) {
//                result = specialty
//                length = specialty.length
//            }
//        }
//    }
//    if (length == 0) throw Exception("There is no matching ingredient error!")
//    return result
//}


@GlideModule
class StorageGlideModule: AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.append(
            StorageReference::class.java,
            InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}
