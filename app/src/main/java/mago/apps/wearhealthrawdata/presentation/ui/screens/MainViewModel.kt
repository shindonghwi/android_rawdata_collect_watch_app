package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.wearhealthrawdata.presentation.ui.utils.HealthTrackingHelper

class MainViewModel : ViewModel() {

    private var healthTrackingHelper: HealthTrackingHelper = HealthTrackingHelper()

    fun initTrackingHelper(context: Context) {
        healthTrackingHelper.setUp(context)
    }

    init {
        healthTrackingHelper.isConnected.onEach {
            if (it){
                healthTrackingHelper.startHeartBeat()
            }
        }.launchIn(viewModelScope)
    }

    val heartBeatState = healthTrackingHelper.heartBeatState

}