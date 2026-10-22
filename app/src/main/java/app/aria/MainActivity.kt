package app.aria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.aria.notif.Notifications
import app.aria.ui.theme.AriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AriaTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
private fun HomeScreen() {
    val context = LocalContext.current
    var granted by remember { mutableStateOf(Notifications.hasPostPermission(context)) }
    var deniedAfterAsk by remember { mutableStateOf(false) }
    var tapCount by remember { mutableIntStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { allowed ->
        granted = allowed
        if (!allowed) deniedAfterAsk = true
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Aria", style = MaterialTheme.typography.headlineLarge)

            if (!granted) {
                Text(
                    "Aria posts through the system notification shade. " +
                        "One permission and it is yours.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(onClick = {
                    deniedAfterAsk = false
                    permissionLauncher.launch(Notifications.POST_PERMISSION)
                }) {
                    Text("Allow notifications")
                }
                if (deniedAfterAsk) {
                    Text(
                        "Permission denied — if the dialog no longer appears, it was " +
                            "permanently denied. Re-enable under system settings → Apps → Aria.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            } else {
                Text(
                    "Notification base is live.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(onClick = {
                    tapCount++
                    Notifications.post(
                        context,
                        title = "Aria",
                        body = "Test notification #$tapCount from Aria ⚡",
                    )
                }) {
                    Text("Send test notification")
                }
            }
        }
    }
}
