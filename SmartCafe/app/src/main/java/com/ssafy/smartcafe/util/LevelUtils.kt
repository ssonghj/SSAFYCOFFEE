package com.ssafy.smartcafe.util

fun getLevel(stamp : Int) : Array<String> {
    val res = Array<String>(3) {""}

    when(stamp) {
        // 브론즈
        in 0..50 -> {
            res[0] = "BRONZE ${(stamp/10) + 1}"
            res[1] = "${stamp%10}"
            res[2] = "다음 등급까지 ${10 - (stamp%10)}개 남았어요!"
        }
        // 실버
        in 51..100 -> {
            res[0] = "SILVER ${((stamp-50)/10) + 1}"
            res[1] = "${(stamp-50)%10}"
            res[2] = "다음 등급까지 ${10 - ((stamp-50)%10)}개 남았어요!"
        }
        // 골드
        in 101..150 -> {
            res[0] = "GOLD ${((stamp-100)/10) + 1}"
            res[1] = "${(stamp-100)%10}"
            res[2] = "다음 등급까지 ${10 - ((stamp-100)%10)}개 남았어요!"
        }
        // 플래티넘
        in 151.. 200 -> {
            res[0] = "PLATINUM ${((stamp-150)/10) + 1}"
            res[1] = "${(stamp-150)%10}"
            res[2] = "다음 등급까지 ${10 - ((stamp-150)%10)}개 남았어요!"
        }
        else -> {
            //다이아몬드
            res[0] = "DIAMOND"
            res[1] = "10"
            res[2] = "최고 등급입니다!"
        }
    }
    return res
}