package com.diwan.myprofileapp.shared.platform

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import android.content.Context

actual class BatteryInfo : KoinComponent {
    private val context: Context by inject()
    private val bm get() = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    actual fun getBatteryLevel(): Int =
        bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    actual fun isCharging(): Boolean {
        val intent = context.registerReceiver(
            null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }
}