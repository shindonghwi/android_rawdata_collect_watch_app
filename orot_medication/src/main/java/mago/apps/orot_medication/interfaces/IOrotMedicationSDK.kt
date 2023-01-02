package mago.apps.orot_medication.interfaces

interface IOrotMedicationSDK {
    /** 오롯코드 서버에 연결 요청을 한다. */
    fun connectServer()

    /** 서버에서 응답되는 상태를 전달 받는 리스너 */
    fun setListener(listener: MedicationStateListener)

    /** 오롯코드 서버에 연결해제 요청을 한다. */
    fun closeServer()

    /** 의료장비에서 측정된 정보를 서버에 보낸다. */
    fun sendMedicalInfo(bloodPressureSystolic: Int, glucose: Int)
}


