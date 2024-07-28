package nbc.group.recipes.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
// -> 의존성 그래프 초기화, DI 환경 설정, 해당 클래스는 Application을 상속받아야 함
class HiltApplication: Application()
