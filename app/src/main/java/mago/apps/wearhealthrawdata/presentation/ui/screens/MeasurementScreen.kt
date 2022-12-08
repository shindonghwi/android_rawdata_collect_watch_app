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
import mago.apps.wearhealthrawdata.presentation.ui.theme.disableButtonColor
import mago.apps.wearhealthrawdata.presentation.ui.theme.enableButtonColor
import mago.apps.wearhealthrawdata.presentation.ui.theme.primaryColor
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.noDuplicationClickable

@Composable
fun MeasurementScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeartRateContent(modifier = Modifier.weight(0.6f))

        Row(
            modifier = Modifier.weight(0.4f).background(Color.Yellow),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            MeasurementButton()
            DataSendButton()
        }

    }
}

@Composable
private fun HeartRateContent(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "심박수 & 혈압",
            color = primaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }

}

@Composable
private fun MeasurementButton() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(enableButtonColor)
            .noDuplicationClickable {
                Toast.makeText(context, "측정", Toast.LENGTH_SHORT).show()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = "측정",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DataSendButton() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(enableButtonColor)
            .noDuplicationClickable {
                Toast.makeText(context, "전송", Toast.LENGTH_SHORT).show()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = "전송",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
