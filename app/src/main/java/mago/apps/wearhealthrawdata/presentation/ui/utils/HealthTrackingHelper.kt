package mago.apps.wearhealthrawdata.presentation.ui.utils

import android.content.Context
import android.util.Log
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.HealthTrackerException
import com.samsung.android.service.health.tracking.HealthTrackingService
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.TrackerUserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity.Companion.TAG
import mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate.HeartRateReceiver
import mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate.HeartRateType

class HealthTrackingHelper {

    private var healthTrackingService: HealthTrackingService? = null
    private var heartRateTracker: HealthTracker? = null
    var heartRateReceiver: HeartRateReceiver? = HeartRateReceiver()

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    val errorState: MutableStateFlow<String?> = MutableStateFlow<String?>(null)

    val heartBeatState = heartRateReceiver?.callback
    var listener: HealthTracker.TrackerEventListener? = null
    fun setUp(context: Context) {
        healthTrackingService = HealthTrackingService(connectionListener, context)
        healthTrackingService?.connectService()
    }

    fun startHeartBeat(){
        if (listener == null){
            listener = heartRateReceiver?.trackerListener
            heartRateTracker?.setEventListener(listener)
        }
    }

    fun completeHeartBeat(averageHr: Int, averageBloodPressure:Int){
        heartBeatState?.value.let {
            heartRateReceiver?.updateHeartRateData(averageHr, averageBloodPressure, HeartRateType.COMPLETED)
        }
    }

    fun stopHeartBeat(){
        listener = null
        heartRateTracker?.unsetEventListener()
    }

    private val connectionListener: ConnectionListener = object : ConnectionListener {
        override fun onConnectionSuccess() {
            Log.i(TAG, "Connected")
            checkCapabilities()
            heartRateTracker = healthTrackingService!!.getHealthTracker(HealthTrackerType.HEART_RATE)
            _isConnected.update { true }
        }

        override fun onConnectionEnded() {
            Log.i(TAG, "Disconnected")
        }

        override fun onConnectionFailed(e: HealthTrackerException) {
            Log.i(TAG, "onConnectionFailed")
            errorState.update { e.message }
        }
    }

    private fun checkCapabilities() {
        val availableTrackers = healthTrackingService!!.trackingCapability.supportHealthTrackerTypes
        if (!availableTrackers.contains(HealthTrackerType.HEART_RATE)) {
            errorState.update { "Device does not support HEART_RATE tracking" }
        }
    }

}