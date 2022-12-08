package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import mago.apps.orot_medication.OrotMedicationSDK
import mago.apps.orot_medication.interfaces.MedicationStateListener
import mago.apps.orot_medication.model.State
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.utils.HealthTrackingHelper


class MainViewModel : ViewModel() {

    lateinit var navController: NavHostController

    private var healthTrackingHelper: HealthTrackingHelper = HealthTrackingHelper()

    val measurementAvailableStatue = MutableStateFlow(true)
    fun updateMeasurementAvailableStatue(flag: Boolean) {
        measurementAvailableStatue.update { flag }
    }

    fun initTrackingHelper(context: Context) {
        healthTrackingHelper.setUp(context)
    }

    init {
        healthTrackingHelper.isConnected.onEach {
            if (it) {
                healthTrackingHelper.startHeartBeat()
            }
        }.launchIn(viewModelScope)
    }

    val heartBeatState = healthTrackingHelper.heartBeatState

    private var sdk: OrotMedicationSDK = OrotMedicationSDK()
    val serverState = MutableStateFlow<Pair<State, String?>>(Pair(State.IDLE, null))
    fun connectionOrotServer() {
        sdk.run {
            setListener(object : MedicationStateListener {
                override fun onState(state: State, msg: String?) {
                    Log.w(MainActivity.TAG, "onState: $state")
                    serverState.update { Pair(state, msg) }
                }
            })
            connectServer()
        }
    }

    fun closeOrotServer() = sdk.closeServer()
    fun sendMedicationInfo() = sdk.sendMedicalInfo(0, 0)

    override fun onCleared() {
        super.onCleared()
        closeOrotServer()
    }

}