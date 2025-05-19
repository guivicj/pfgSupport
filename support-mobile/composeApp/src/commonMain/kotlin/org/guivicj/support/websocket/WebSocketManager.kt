package org.guivicj.support.websocket

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.guivicj.support.domain.model.MessageDTO

class WebSocketManager(
    private val client: HttpClient
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var session: WebSocketSession? = null

    private val _messages = MutableSharedFlow<MessageDTO>()
    val messages = _messages.asSharedFlow()

    private val json = Json { ignoreUnknownKeys = true }

    fun connect(ticketId: Long, userId: Long) {
        scope.launch {
            try {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = "10.0.2.2",
                    port = 8080,
                    path = "/ws/chat",
                    request = {
                        url.parameters.append("ticketId", ticketId.toString())
                        url.parameters.append("userId", userId.toString())
                    }
                ) {
                    session = this
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val msg = json.decodeFromString<MessageDTO>(text)
                            _messages.emit(msg)
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket connection error: ${e.message}")
            }
        }
    }

    fun send(message: MessageDTO) {
        scope.launch {
            try {
                val text = json.encodeToString(message)
                session?.send(Frame.Text(text))
            } catch (e: Exception) {
                println("WebSocket send error: ${e.message}")
            }
        }
    }

    fun disconnect() {
        scope.launch {
            session?.close()
            session = null
        }
    }
}
