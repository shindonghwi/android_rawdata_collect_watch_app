package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import mago.apps.orot_medication.OrotMedicationSDK
import mago.apps.orot_medication.interfaces.MedicationStateListener
import mago.apps.orot_medication.model.State
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity.Companion.TAG
import mago.apps.wearhealthrawdata.presentation.ui.utils.HealthTrackingHelper
import mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate.HeartRateData
import kotlin.random.Random


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
                Log.w(TAG, "measurementStart isConnected: ")
                healthTrackingHelper.startHeartBeat()
            }
        }.launchIn(viewModelScope)
    }

    var timerIsStarted = false
    var isSendingButtonEnable = MutableStateFlow<Boolean>(false)

    val progressValue = MutableStateFlow<Int>(0)
    fun updateTimerFlag(flag: Boolean) {
        timerIsStarted = flag
    }

    private lateinit var measurementScope: CoroutineScope

    fun measurementStart() {
        Log.w(TAG, "measurementStart START: ")
        measurementScope = CoroutineScope(SupervisorJob() + Dispatchers.Default).apply {
            launch {
                measurementTimerStart()
                Log.w(TAG, "measurementStart END: ")
            }
        }
    }

    fun measurementCancel() {
        Log.w(TAG, "measurementCancel START: ")
        timerIsStarted = false
        isSendingButtonEnable.update { false }
        measurementScope.cancel()
        progressValue.update { 0 }
        hrList.clear()
        bloodPressureList.clear()
        Log.w(TAG, "measurementCancel END: ")
    }

    fun reconnectHeartRateReceiver() {
        measurementCancel()
        healthTrackingHelper.run {
            stopHeartBeat()
            startHeartBeat()
        }
    }

    private suspend fun measurementTimerStart() {
        Log.w(TAG, "measurementTimerStart: START")
        for (percent in 0..100) {
            Log.w(TAG, "flow: $percent")
            if (percent >= 10) {
                delay((Random.nextInt(100) + 30).toLong())
            } else {
                delay((Random.nextInt(350) + 150).toLong())
            }

            progressValue.update { percent }
            if (percent == 100) {
                healthTrackingHelper.run {
                    stopHeartBeat()
                    completeHeartBeat(hrList.toMutableList().average().toInt(), 0)
                }
                isSendingButtonEnable.update { true }
            }

        }
        Log.w(TAG, "measurementTimerStart: END")
    }

    fun getHeartBeatState(): MutableState<HeartRateData>? {
        val callback = healthTrackingHelper.heartBeatState
        callback?.value?.hr.takeIf { it != 0 }?.apply { hrList.add(this) }
        return callback
    }

    val hrList = arrayListOf<Int>()
    val bloodPressureList = arrayListOf<Int>()

    private var sdk: OrotMedicationSDK = OrotMedicationSDK()
    val serverState = MutableStateFlow<Pair<State, String?>>(Pair(State.IDLE, null))
    val serverAllowedTransmission = MutableStateFlow<Boolean>(false)
    val loadingBarIsShowing = MutableStateFlow(false)
    fun connectionOrotServer() {
        loadingBarIsShowing.update { true }
        sdk.run {
            setListener(object : MedicationStateListener {
                override fun onState(state: State, msg: String?) {
                    Log.w(TAG, "onState: $state : $msg")
                    serverState.update { Pair(state, msg) }
                    when (state) {
                        State.CONNECTED -> {
                            loadingBarIsShowing.update { false }
                        }
                        State.ALLOWED_TRANSMISSION -> {
                            serverAllowedTransmission.update { true }
                        }
                        else -> {}
                    }
                }
            })
            connectServer()
        }
    }

    fun closeOrotServer() = sdk.closeServer()
    fun sendMedicationInfo() = sdk.sendMedicalInfo(hrList.average().toInt(), 0)

    fun clear() {
        measurementCancel()
        healthTrackingHelper.run {
            stopHeartBeat()
            disconnect()
        }
        closeOrotServer()
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }

}