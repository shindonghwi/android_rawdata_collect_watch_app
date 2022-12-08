package mago.apps.orot_medication.model

/** 서버의 상태 */
enum class State {
    IDLE, // 아무작업도 하지 않았음. ( 초기 상태 )
    CONNECTED, // 서버와 연결이 되었음.
    ALLOWED_TRANSMISSION, // 의료장비(의자)에서 서버로 데이터를 전송을 허용함.
    CLOSED, // 서버의 연결이 닫힘. (의도적으로 개발자가 닫았음.)
    ERROR // 서버와의 연결이 닫힘. (예외상황에서 발생한 에러케이스, msg 값으로 error message 확인가능 )
}