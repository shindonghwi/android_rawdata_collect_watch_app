package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import mago.apps.orot_medication.OrotMedicationSDK
import mago.apps.orot_medication.interfaces.MedicationStateListener
import mago.apps.orot_medication.model.State
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.utils.HealthTrackingHelper
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.coroutineScopeOnDefault
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.coroutineScopeOnMain
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

    private val initTimerValue = 40L
    var timerIsStarted = false
    val timerValueState = MutableStateFlow<Long>(initTimerValue)

    fun updateTimerFlag(flag: Boolean){
        timerIsStarted = flag
    }

    fun measurementTimerStart() {
        tickerFlow(start = initTimerValue).onEach { it ->
            /** 여기해야함 */
            timerValueState.value = it
        }.launchIn(viewModelScope)
    }

    private fun tickerFlow(start: Long, end: Long = 0L) = flow {
        for (i in start downTo end) {
            emit(i)
            delay((Random.nextInt(1500 - 300 + 1) + 300).toLong())
        }
    }
    val heartBeatState = healthTrackingHelper.heartBeatState

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
    fun sendMedicationInfo() = sdk.sendMedicalInfo(0, 0)

    override fun onCleared() {
        super.onCleared()
        closeOrotServer()
    }

}