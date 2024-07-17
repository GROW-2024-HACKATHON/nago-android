package com.grow.nago.ui.utiles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <SIDE_EFFECT : Any> Flow<SIDE_EFFECT>.CollectAsSideEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit),
) {
    val sideEffectFlow = this
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect {
                sideEffect(it)
            }
        }
    }
}

val safety = listOf("여름철 집중신고”, “도로”, “시설물 파손 및 고장", "건설", "(해체)공사장 위험", "대기오염", "수질오염", "소방안전", "기타 안전 · 환경 위험요소")
val life = listOf("불법광고물", "자전거 · 이륜차 방치 및 불편", "쓰레기 · 폐기물", "해양 쓰레기", "불법 숙박", "기타 생활불편")
val drive = listOf("교통위반(고속도로 포함)", "이륜차 위반", "버스전용차로 위반(일반도로)", "번호판 규정 위반", "불법등화 · 반사판(지) 가림 · 손상", "불법 튜닝 · 해체 · 조작", "기타 자동차 안전기준 위반")
val parking = listOf("소화전", "교차로 모퉁이", "버스 정류소", "횡단보도", "어린이 보호구역", "인도", "기타", "장애인 전용구역", "소방차 전용구역", "친환경차 충전구역")