package org.guivicj.support.websocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.MessageDTO

class ChatSyncHandler(
    private val webSocketManager: WebSocketManager
) {
    private var job: Job? = null

    fun connect(ticketId: Long, userId: Long, onNewMessage: (MessageDTO) -> Unit) {
        webSocketManager.connect(ticketId, userId)
        job = CoroutineScope(Dispatchers.Default).launch {
            webSocketManager.messages.collectLatest {
                onNewMessage(it)
            }
        }
    }

    fun send(message: MessageDTO) {
        webSocketManager.send(message)
    }

    fun disconnect() {
        job?.cancel()
        webSocketManager.disconnect()
    }
}
