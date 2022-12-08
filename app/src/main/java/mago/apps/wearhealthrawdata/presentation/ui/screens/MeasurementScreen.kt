package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mago.apps.wearhealthrawdata.R
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.theme.disableButtonColor
import mago.apps.wearhealthrawdata.presentation.ui.theme.enableButtonColor
import mago.apps.wearhealthrawdata.presentation.ui.theme.primaryColor
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.noDuplicationClickable
import mago.apps.wearhealthrawdata.presentation.ui.utils.heartrate.HeartRateType

@Composable
fun MeasurementScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MeasurementTitle(modifier = Modifier.weight(0.6f))

        Column(
            modifier = Modifier
                .weight(0.4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CountDownTimer()
            DataSendButton()
        }

    }
}

@Composable
private fun CountDownTimer() {
    val mainViewModel = (LocalContext.current as MainActivity).mainViewModel
    val timerCount = mainViewModel.timerValueState.collectAsState().value

    Log.w("ASdasdads", "CountDownTimer: $timerCount", )

    Text(
        text = timerCount.toString(),
        color = primaryColor,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
    )

}

@Composable
private fun MeasurementTitle(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "심박수 & 혈압",
            color = primaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HeartRateContent()
            BloodPressureContent()
        }

    }

}

@Composable
private fun HeartRateContent() {

    val mainViewModel = (LocalContext.current as MainActivity).mainViewModel

    val heartValue = mainViewModel.heartBeatState?.value?.hr ?: 0

    val status = when (mainViewModel.heartBeatState?.value?.status) {
        HeartRateType.ERROR -> {
            "측정불가"
        }
        HeartRateType.COMPLETED -> {
            "측정완료"
        }
        HeartRateType.EMPTY,
        HeartRateType.TEMPORARILY_UNAVAILABLE -> {
            "측정대기"
        }
        else -> {
            "측정중"
        }
    }

    mainViewModel.run {
        if (!timerIsStarted && heartValue != 0){
            measurementTimerStart()
            updateTimerFlag(true)
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = R.drawable.heart),
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = status,
                color = primaryColor,
                fontSize = 12.sp,
            )
            Text(
                text = heartValue.toString(),
                color = primaryColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

    }
}

@Composable
private fun BloodPressureContent() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = R.drawable.arm),
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "측정불가",
                color = primaryColor,
                fontSize = 12.sp,
            )
            Text(
                text = "123",
                color = primaryColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun DataSendButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, disableButtonColor, RoundedCornerShape(12.dp))
                .noDuplicationClickable {

                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp),
                text = "전송",
                color = disableButtonColor.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
