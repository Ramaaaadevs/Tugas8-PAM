package com.diwan.myprofileapp.shared.platform

import kotlinx.coroutines.flow.Flow

expect class NetworkMonitor() {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}