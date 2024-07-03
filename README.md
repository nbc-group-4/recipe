![image 483](https://github.com/nbc-group-4/recipe/assets/50291395/5b91b181-7afa-43a2-b68c-73ffb2ab64cd)

# 프로젝트 소개
- **맛고**는 대한민국의 지역별 특산물을 앱 내에 구현된 지도를 통해 확인할 수 있습니다. 

- 해당 특산물로 만든 레시피와 레시피의 상세한 설명을 볼 수 있습니다.

- 로그인/회원가입 기능이 존재하여 사용자가 저장하고 싶은 레시피를 북마크할 수 있습니다. 

- 또한 사용자가 직접 특산물을 재료로 이용한 레시피를 작성하여 사용자들끼리 공유할 수 있습니다.


# 팀 소개
|이준영|배기정|전주원|변예진|
|:---:|:---:|:---:|:---:|
|[Github](https://github.com/treeralph)|[Github](https://github.com/koreabgj)|[Github](https://github.com/wndnjs00)|[Github](https://github.com/Yejin-Byun)|

# 스택
|분류|이름|
|:---:|:---:|
|언어|<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">
|개발 환경|<img src="https://img.shields.io/badge/android studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white">
|Architecture|<img src="https://img.shields.io/badge/mvvm-221E68?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/repository pattern-221E68?style=for-the-badge&logoColor=white">
|DI|<img src="https://img.shields.io/badge/hilt-221E68?style=for-the-badge&logoColor=white">
|비동기 처리|<img src="https://img.shields.io/badge/flow-221E68?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/coroutine-221E68?style=for-the-badge&logoColor=white">
|Networking Tool|<img src="https://img.shields.io/badge/jsoup-221E68?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/retrofit-221E68?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/okhttp-221E68?style=for-the-badge&logoColor=white">
|이미지 로더|<img src="https://img.shields.io/badge/glide-221E68?style=for-the-badge&logoColor=white">
|지도|<img src="https://img.shields.io/badge/kakao map-FFCD00?style=for-the-badge&logoColor=white">
|Database|<img src="https://img.shields.io/badge/room-221E68?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/firebase-DD2C00?style=for-the-badge&logo=firebase&logoColor=white">
|UI|<img src="https://img.shields.io/badge/xml-221E68?style=for-the-badge&logoColor=white"><img src="https://img.shields.io/badge/navigation-221E68?style=for-the-badge&logoColor=white">
|협업|<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

# 앱 디자인
<img width="870" alt="image" src="https://github.com/nbc-group-4/recipe/assets/50291395/61a0c8f1-7a7d-4435-bf23-48ede9069b14">


# 기능
|홈 및 특산물 검색|지도|레시피|
|:---:|:---:|:---:|
|![recipe_home_small](https://github.com/nbc-group-4/recipe/assets/50291395/ade64517-dae9-4889-9c13-fd8367784bcd)|![recipe_map_small](https://github.com/nbc-group-4/recipe/assets/50291395/03e03cda-c349-4495-8613-7c7a0a1f6e8b)|![recipe_recipe_small](https://github.com/nbc-group-4/recipe/assets/50291395/f2b16bc2-6374-44fe-a9f8-47cffe81eea1)|

|레시피 상세|레시피 작성|북마크|
|:---:|:---:|:---:|
|![recipe_recipe_detail_small](https://github.com/nbc-group-4/recipe/assets/50291395/e8620f3b-dd6a-428f-883b-a74c6fcbc8fa)|![recipe_make_recipe_small](https://github.com/nbc-group-4/recipe/assets/50291395/76f419e9-f9d5-4091-81cf-49fa7c89f711)|![recipe_bookmark_small](https://github.com/nbc-group-4/recipe/assets/50291395/abf1a741-b12b-4837-98ee-f36007fd816b)
|

|내 페이지|회원|
|:---:|:---:|
|![recipe_my_page](https://github.com/nbc-group-4/recipe/assets/50291395/fb760fdb-9464-4159-84cf-e687de525ab6)|![recipe_user](https://github.com/nbc-group-4/recipe/assets/50291395/36a0bd71-dea3-444f-ac1f-0f98f9d3901a)|

## 그래프 시각화
- [Graph Visualizing Algorithm](https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0098679) 참고하여 알고리즘 작성.

~~~kotlin
fun operate(
    nodes: MutableList<Node<Any>>,
    edges: MutableList<Edge>,
    inInteraction: Int,
) {
    for(i in 0 until nodes.size) {
        if(inInteraction == i) continue
        val ithNode = nodes[i]
        var fx = 0f
        var fy = 0f

        for(j in 0 until nodes.size) {
            val jthNode = nodes[j]

            val distX = ithNode.x - jthNode.x
            val distY = ithNode.y - jthNode.y

            val rsq = distX.pow(2) + distY.pow(2)

            val coulombDistX = COULOMB * distX
            val coulombDistY = COULOMB * distY

            if(rsq >= 0.01f && sqrt(rsq) < DISTANCE) {
                fx += coulombDistX / rsq
                fy += coulombDistY / rsq
            }
        }

        val distXC = -1 * (ithNode.x + ithNode.size / 2) + CENTER_X
        val distYC = -1 * (ithNode.y + ithNode.size / 2) + CENTER_Y
        fx += GRAVITY * distXC
        fy += GRAVITY * distYC

        for(j in 0 until edges.size) {
            val jthEdge = edges[j]

            var distX = 0f
            var distY = 0f

            if(i == jthEdge.to) {
                val targetNode = nodes[jthEdge.from]
                distX = targetNode.x - ithNode.x
                distY = targetNode.y - ithNode.y
            } else if(i == jthEdge.from) {
                val targetNode = nodes[jthEdge.to]
                distX = targetNode.x - ithNode.x
                distY = targetNode.y - ithNode.y
            }

            fx += BOUNCE * distX
            fy += BOUNCE * distY
        }

        val dx = (ithNode.dx + fx) * ATTENUATION
        val dy = (ithNode.dy + fy) * ATTENUATION

        val x = ithNode.x + dx
        val y = ithNode.y + dy

        nodes[i] = ithNode.copy(
            x = x,
            y = y,
            dx = dx,
            dy = dy
        )
    }
}
~~~

- View lifecycle
  - 성능을 고려한 코드 작성

<table>
<tr>
<td> Lifecycle </td> <td> dispatchDraw </td>
</tr>
<tr>
<td> 
<img width="200" alt="스크린샷 2024-07-03 오후 6 07 32" src="https://github.com/nbc-group-4/recipe/assets/50291395/c4fdf9f2-1727-452d-850b-8e3d26b075da">
</td>
<td>
  
~~~kotlin
override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)
    /** 중략 **/
    operate(nodes, edges, targetNode)
    edges.forEach { edge ->
        /** 중략 **/
    }
    nodes.forEachIndexed { index, node ->
        /** 중략 **/
     }
    invalidate()
}
~~~
</td>
</tr>
</table>

- Motion Event Handling
  - Motion Event Action Mask를 통한 이벤트 처리
  - GestureDetector를 통한 이벤트 처리

<table>
<tr>
<td> Lifecycle </td> <td> dispatchDraw </td>
</tr>
<tr>
<td> 

~~~kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
        
        when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    /** 중략 **/
                }

                MotionEvent.ACTION_MOVE -> {
                    /** 중략 **/
                }

                MotionEvent.ACTION_UP -> {
                    /** 중략 **/
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    /** 중략 **/
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    /** 중략 **/
                }
    
                MotionEvent.ACTION_CANCEL -> {
                    /** 중략 **/
                }
        }
        /** 중략 **/
    }
~~~


</td>
<td>
  
~~~kotlin
private inner class LongClickListener: GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
                /** 중략 **/
        }
        override fun onLongPress(e: MotionEvent) {
                /** 중략 **/
        }
}
~~~
</td>
</tr>
</table>
