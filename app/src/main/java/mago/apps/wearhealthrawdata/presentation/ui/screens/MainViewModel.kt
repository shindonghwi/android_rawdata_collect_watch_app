package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import mago.apps.orot_medication.OrotMedicationSDK
import mago.apps.orot_medication.interfaces.MedicationStateListener
import mago.apps.orot_medication.model.State
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity.Companion.TAG
import mago.apps.wearhealthrawdata.presentation.ui.utils.HealthTrackingHelper
import mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate.HeartRateData
import mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate.HeartRateType
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
        measurementScope = CoroutineScope(Dispatchers.Default).apply {
            launch {
                measurementTimerStart()
            }
        }
    }

    fun measurementCancel() {
        measurementScope.launch {
            timerIsStarted = false
            isSendingButtonEnable.update { false }
            progressValue.update { 0 }
            hrList.clear()
            bloodPressureList.clear()
            healthTrackingHelper.run {
                stopHeartBeat()
                startHeartBeat()
            }
            measurementScope.cancel()
        }
    }

    private suspend fun measurementTimerStart() = measurementScope.launch {
        flow {
            for (i in 0..100) {
                emit(i)
                if (i >= 60) {
                    delay((Random.nextInt(100) + 30).toLong())
                } else {
                    delay((Random.nextInt(350) + 150).toLong())
                }
            }
        }
            .cancellable()
            .map { percent ->
                progressValue.update { percent }

                if (percent == 100) {
                    healthTrackingHelper.run {
                        completeHeartBeat(hrList.average().toInt(), 0)
                        stopHeartBeat()
                    }
                    isSendingButtonEnable.update { true }
                }

            }.collect()
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
    val loadingBarIsShowing = MutableStateFlow(false)
    fun connectionOrotServer() {
        loadingBarIsShowing.update { true }
        sdk.run {
            setListener(object : MedicationStateListener {
                override fun onState(state: State, msg: String?) {
                    Log.w(MainActivity.TAG, "onState: $state")
                    serverState.update { Pair(state, msg) }
                    loadingBarIsShowing.update { false }
                }
            })
            connectServer()
        }
    }

    fun closeOrotServer() = sdk.closeServer()
    fun sendMedicationInfo() = sdk.sendMedicalInfo(hrList.average().toInt(), 0)

    override fun onCleared() {
        super.onCleared()
        closeOrotServer()
    }

}