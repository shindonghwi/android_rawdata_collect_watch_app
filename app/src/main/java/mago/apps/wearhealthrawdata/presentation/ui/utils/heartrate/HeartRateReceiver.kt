package mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import com.samsung.android.service.health.tracking.HealthTracker.TrackerError
import com.samsung.android.service.health.tracking.HealthTracker.TrackerEventListener
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.ValueKey
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity.Companion.TAG

enum class HeartRateType {
    // 작동중, 일시적 수신불가, 에러, 끝, 값이 없음
    RUNNING, TEMPORARILY_UNAVAILABLE, ERROR, COMPLETED, EMPTY
}

data class HeartRateData(
    val hr: Int,
    val ibi: Int,
    val status: HeartRateType,
)

class HeartRateReceiver {

    val callback = mutableStateOf(HeartRateData(0, 0, HeartRateType.EMPTY), neverEqualPolicy())

    val trackerListener: TrackerEventListener = object : TrackerEventListener {
        override fun onDataReceived(list: List<DataPoint>) {
            if (list.isNotEmpty()) {
                for (dataPoint in list) {
                    val hr = dataPoint.getValue(ValueKey.HeartRateSet.HEART_RATE)
                    val hrIbi = dataPoint.getValue(ValueKey.HeartRateSet.HEART_RATE_IBI)
                    val status = dataPoint.getValue(ValueKey.HeartRateSet.STATUS)

                    updateHeartRateData(
                        hr, hrIbi, if (status == 1) {
                            HeartRateType.RUNNING
                        } else {
                            HeartRateType.TEMPORARILY_UNAVAILABLE
                        }
                    )
                }
            } else {
                updateHeartRateData(0, 0, HeartRateType.EMPTY)
            }
        }

        override fun onFlushCompleted() {
            updateHeartRateData(0, 0, HeartRateType.COMPLETED)
        }

        override fun onError(trackerError: TrackerError) {
            updateHeartRateData(0, 0, HeartRateType.ERROR)
        }
    }

    fun updateHeartRateData(hr: Int, ibi: Int, status: HeartRateType) {
        Log.i(TAG, "HeartRateReceiver: updateHeartRateData || hr:$hr , ibi:$ibi , status:$status")
        callback.value = HeartRateData(hr = hr, ibi = ibi, status = status)
    }

}