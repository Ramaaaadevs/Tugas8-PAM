package com.diwan.myprofileapp.shared.platform

expect class BatteryInfo() {
    fun getBatteryLevel(): Int
    fun isCharging(): Boolean
}