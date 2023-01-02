package mago.apps.orot_medication.interfaces

import mago.apps.orot_medication.model.State

/** 서버의 상태를 수신하는 리스너 ( 클라이언트에서 콜백을 전달받는다. )*/
interface MedicationStateListener {
    fun onState(state: State, msg: String?)
}