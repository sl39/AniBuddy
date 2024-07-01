package com.example.front

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WSListener : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        // WebSocket 연결이 성공적으로 수립되었을 때 초기화 작업 수행
        // response 객체를 통해 WebSocket 연결 시 전송된 응답 정보를 확인
        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        // 수신된 텍스트 메시지 처리
        super.onMessage(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        // 수신된 바이너리 메시지 처리
        super.onMessage(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        // 연결 종료 전 필요한 작업을 수행
        super.onClosing(webSocket, code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        // 연결 종료 후 필요한 작업을 수행
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // WebSocket 연결이 실패했을 때 처리 로직
        super.onFailure(webSocket, t, response)
    }
}