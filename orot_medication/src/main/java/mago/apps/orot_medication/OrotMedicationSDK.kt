package mago.apps.orot_medication

import android.util.Log
import com.google.gson.Gson
import mago.apps.orot_medication.interfaces.IOrotMedicationSDK
import mago.apps.orot_medication.interfaces.MedicationStateListener
import mago.apps.orot_medication.model.*
import okhttp3.*

class OrotMedicationSDK : IOrotMedicationSDK {

    val TAG: String = "OrotMedicationSDK"
    private val url: String = "ws://demo-health-stream.mago52.com/ws/chat"
    private var request: Request? = null
    private var client: OkHttpClient? = null
    private var webSocket: WebSocket? = null
    private var medicationStateListener: MedicationStateListener? = null

    /** 서버에 연결한다. */
    override fun connectServer() {

        if (medicationStateListener == null) {
            Log.e(TAG, "listener not initialization: ${State.ERROR}")
            return
        }

        try {
            request = Request.Builder().url(url).build()
            client = OkHttpClient()
        } catch (e: Exception) {
            medicationStateListener?.onState(State.ERROR, e.message)
            return
        }

        request?.run {
            try {
                webSocket = client!!.newWebSocket(this, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        medicationStateListener?.onState(State.CONNECTED, null)
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        super.onMessage(webSocket, text)
                        Log.w(TAG, "onMessage: $text")
                        try {
                            val receivedMsg: MessageProtocol =
                                Gson().fromJson(text, MessageProtocol::class.java)
                            Log.w(TAG, "onMessage: $receivedMsg")
                            if (receivedMsg.header.protocol_id == "DEVICE_MEASUREMENT_ENTRY_REQ") {
                                medicationStateListener?.onState(State.ALLOWED_TRANSMISSION, text)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "onMessage: ${e.message}")
                        }
                    }

                    override fun onFailure(
                        webSocket: WebSocket, t: Throwable, response: Response?
                    ) {
                        super.onFailure(webSocket, t, response)
                        medicationStateListener?.onState(State.ERROR, t.message.toString())
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosed(webSocket, code, reason)
                        medicationStateListener?.onState(State.CLOSED, reason)
                    }
                })
            } catch (e: Exception) {
                medicationStateListener?.onState(State.ERROR, e.message.toString())
            }
        }
    }

    /** 서버상태를 확인하기 위해 리스너 설정을 한다. */
    override fun setListener(listener: MedicationStateListener) {
        medicationStateListener = listener
    }

    /** 서버의 연결을 닫는다. */
    override fun closeServer() {
        try {
            webSocket?.cancel()
            request = null
            client = null
            webSocket = null
        } catch (e: Exception) {
            medicationStateListener?.onState(State.ERROR, e.message.toString())
        }
    }

    /** 서버로 의료장비에서 측정된 데이터를 보낸다. */
    override fun sendMedicalInfo(bloodPressureSystolic: Int, glucose: Int) {
        try {
            val msg = MessageProtocol(
                header = HeaderInfo(protocol_id = "DEVICE_MEASUREMENT_ENTRY_ACK"),
                body = BodyInfo(measurement = MeasurementInfo(bloodPressureSystolic, glucose))
            )
            Log.w(TAG, "sendMedicalInfo: ${Gson().toJson(msg)}")
            webSocket?.send(Gson().toJson(msg))
        } catch (e: Exception) {
            medicationStateListener?.onState(State.ERROR, e.message.toString())
        }
    }
}