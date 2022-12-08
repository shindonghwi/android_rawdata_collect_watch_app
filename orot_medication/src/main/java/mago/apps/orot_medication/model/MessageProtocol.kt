package mago.apps.orot_medication.model

/** 오롯코드 서버에 맞는 데이터 형식 */
data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo? = null,
)

data class HeaderInfo constructor(
    val protocol_id: String? = null,
    val protocol_version: String = "1.0",
    val timestamp: Long = System.currentTimeMillis() / 1000,
    val device: String? = "Chair",
)

data class BodyInfo constructor(
    val measurement: MeasurementInfo? = null,
)

data class MeasurementInfo(
    val bloodPressureSystolic: Int,
    val glucose: Int,
)
