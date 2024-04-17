package ro.pub.cs.systems.eim.ftpserverwelcomemessage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ro.pub.cs.systems.eim.ftpserverwelcomemessage.FTPViewModel.Companion.FTP_PORT

class FTPServerWelcomeMessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<FTPViewModel>()
        setContent {
            FTPServerWelcomeMessageScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun FTPServerWelcomeMessageScreen(viewModel: FTPViewModel) {
    var address by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        TextField(value = address,
            onValueChange = { address = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            label = { Text("FTP Server Address: ") },
            placeholder = { Text("Example: ftp.gnu.org") }
        )

        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = port,
            onValueChange = { port = it },
            modifier = Modifier.padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("FTP Server Port: ") },
            placeholder = { Text("Example: $FTP_PORT") }
        )

        Button(onClick = {
            viewModel.fetchFTPWelcomeMessage(address, port.toIntOrNull() ?: FTP_PORT)
        }) {
            Text("Display Welcome Message")
        }
        Text(viewModel.welcomeMessage.value)
    }
}

