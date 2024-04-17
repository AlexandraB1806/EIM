package ro.pub.cs.systems.eim.ftpserverwelcomemessage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Socket

class FTPViewModel : ViewModel() {
    private val _welcomeMessage = mutableStateOf("")
    val welcomeMessage = _welcomeMessage

    fun fetchFTPWelcomeMessage(address: String, port: Int) {
        viewModelScope.launch {
            val message = getFTPData(address, port)
            _welcomeMessage.value = message
        }
    }

    private suspend fun getFTPData(address: String, port: Int): String = withContext(Dispatchers.IO) {
        val result = StringBuilder()
        var socket: Socket? = null

        try {
            socket = Socket(address, port).apply {
                soTimeout = 10000  // Set timeout to handle long waits
            }

            // Automatically manage closing with the 'use' block
            socket.getInputStream().reader().buffered().use { reader ->
                val line = reader.readLine() ?: "No response received"
                result.append(line)
            }
        } catch (e: Exception) {  // Catch all exceptions in one block
            result.append("Error: ${e.localizedMessage}")
        } finally {
            try {
                socket?.close()  // Ensure the socket is closed
            } catch (e: Exception) {
                result.append("Error closing socket: ${e.localizedMessage}")
            }
        }

        result.toString()
    }


    companion object {
        const val FTP_PORT = 21 // Standard FTP port, adjust if necessary
    }
}
