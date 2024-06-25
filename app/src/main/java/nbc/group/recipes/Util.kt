package nbc.group.recipes

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.InputStream

val specialties = setOf(
    "취나물", "미나리", "잎들깨", "오이", "부추", "깻잎", "대파", "배추", "미나리", "고추", "쑥", "상추", "고사리",
    "잣", "오디", "두릅", "가시오가피", "곰취", "삼백초", "녹차", "눈개승마", "어성초", "피망", "곤드레",
    "파프리카", "시래기", "가지", "쪽파", "명이", "부지갱이", "미역취", "서덜취", "머위", "음나무", "차나무", "여주",
    "양상추", "시금치", "박나물", "치자", "알로에", "브로콜리", "고구마줄기", "고구마", "감자", "당근", "연근", "마늘",
    "양파", "무", "도라지", "인삼", "순무", "돼지감자", "더덕", "산삼", "생강", "천마", "토란", "산양삼",
    "우엉", "쌀", "찹쌀", "흑미", "현미", "율무", "콩", "들깨", "호두", "참외", "멜론", "체리", "복숭아", "포도",
    "사과", "수박", "단감", "블루베리", "토마토", "딸기", "감", "배", "복숭아", "다래", "구기자", "오미자", "대추",
    "복분자", "무화과", "한라봉", "유자", "대봉감", "자두", "망고", "애플망고", "용과", "키위", "매실",
    "단호박", "호박", "애호박", "옥수수", "밤", "쥬키니", "붕장어", "곰장어", "갈치", "멸치", "장어", "숭어", "황복",
    "참게", "코다리", "오징어", "전어", "은어", "내수면 철갑상어", "쥐치", "조개", "가리비", "전복", "새조개", "바지락",
    "굴", "개조개", "문어", "대게", "황태", "어란", "다시마", "미역", "김", "한우", "꿀", "닭", "오리",
    "밀가루", "잡곡", "양돈", "녹돈", "돼지", "유정란", "율란", "달걀", "송이버섯", "표고", "느타리", "영지",
    "노루궁뎅이", "상황", "양송이", "팽이버섯", "새송이버섯","참다래","버섯","호박","꽃게","요구르트","새우젓","치즈","계란",
    "콩","갯벌장어","메주","표고버섯","귀리","김치","참옻된장","엿","고구마 빼떼기죽","황토햇쌀","친환경 미르쌀",
    "용유고추단지","영종고추단지","배(탑프루트)단지","이앤미떡","방울토마토","창녕 송이버섯","우엉마영농조합법인",
    "생강조청","생강한과","인삼죽염","매력한우","창녕고추","유가 한정양파","다사 이천참외","옥포 참외","오색소반김치","고구마 쫀득이",
    "동백·유자오일","유자청","강화고추","강화속노랑고구마","토마토단지","강화섬쌀","강화섬포도","서산육쪽마늘","지리산산골흑돼지","감말랭이",
    "새송이 버섯","달빛 무화과 쌀빵","단풍미인방울토마토","꽈리고추","싱싱단감농장","땅두릅","참고비(섬고사리)","부추해물 잡채","옥포 수박",
    "논공 찰토마토","하빈 기곡포도","구지 예현마늘","멸치액젓","유자주","우렁이쌀","황매산 친환경 밤묵","양떡메(양파즙, 떡가래, 메주)"
)

fun convertToOfficial(target: String): String {
    var result = ""
    var length = 0
    specialties.forEach { specialty ->
        if(target.contains(specialty)) {
            if(length < specialty.length) {
                result = specialty
                length = specialty.length
            }
        }
    }
    if (length == 0) throw Exception("There is no matching ingredient error!")
    return result
}


@GlideModule
class StorageGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.append(
            StorageReference::class.java,
            InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}

fun getRecipeImageUrl(query: String): String? {
    val doc = Jsoup.connect("https://www.10000recipe.com/recipe/list.html?q=${query}").get()
    val tags = doc.select(".common_sp_thumb img").last()?.attr("src")
    return tags
}
