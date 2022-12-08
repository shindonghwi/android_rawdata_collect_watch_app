package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mago.apps.orot_medication.model.State
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.noDuplicationClickable

@Composable
fun MeasurementScreen() {
    Row(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        HeartRateContent()

        ConnectionButton()
        DataSendButton()
    }
}

@Composable
private fun HeartRateContent() {

}

@Composable
private fun ConnectionButton() {
    val context = LocalContext.current
    val activity = context as MainActivity
    val serverState = activity.mainViewModel.serverState.collectAsState().value

    LaunchedEffect(key1 = serverState) {
        if (serverState.first == State.ERROR) {
            Toast.makeText(context, serverState.second.toString(), Toast.LENGTH_SHORT).show()
        } else if (serverState.first == State.CONNECTED) {
            Toast.makeText(context, "서버 연결 성공", Toast.LENGTH_SHORT).show()
        }
    }


    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(enableButtonColor)
            .noDuplicationClickable {
                activity.mainViewModel.connectionOrotServer()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "연결",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DataSendButton() {
    val context = LocalContext.current
    val activity = context as MainActivity
    val serverState = activity.mainViewModel.serverState.collectAsState().value

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                when (serverState.first) {
                    State.IDLE,
                    State.ERROR,
                    State.CLOSED -> {
                        disableButtonColor
                    }
                    else -> {
                        enableButtonColor
                    }
                }

            )
            .noDuplicationClickable {
                when (serverState.first) {
                    State.IDLE,
                    State.ERROR,
                    State.CLOSED -> {
                        Toast
                            .makeText(context, "서버 연결 안됨", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        activity.mainViewModel.connectionOrotServer()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "전송",
            color = Color.White.copy(
                alpha = when (serverState.first) {
                    State.IDLE,
                    State.ERROR,
                    State.CLOSED -> {
                        0.7f
                    }
                    else -> {
                        1f
                    }
                }
            ),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
