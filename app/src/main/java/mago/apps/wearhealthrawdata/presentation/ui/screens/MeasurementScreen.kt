package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import mago.apps.wearhealthrawdata.R
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity.Companion.TAG
import mago.apps.wearhealthrawdata.presentation.ui.theme.*
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.coroutineScopeOnDefault
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
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            MeasurementTitle()
            CountDownTimer()
            MeasurementIconValue()
        }

        Column(
            modifier = Modifier
                .weight(0.4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DataSendButton()
        }

    }
}

@Composable
private fun CountDownTimer() {
    val mainViewModel = (LocalContext.current as MainActivity).mainViewModel
    val percent = mainViewModel.progressValue.collectAsState().value

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$percent%",
            color = enableButtonColor.copy(alpha = 0.8f),
            style = MaterialTheme.typography.display3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MeasurementTitle() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "심박수 & 혈압",
            color = primaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun MeasurementIconValue() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        HeartRateContent()
        BloodPressureContent()
    }
}

@Composable
private fun HeartRateContent() {

    val mainViewModel = (LocalContext.current as MainActivity).mainViewModel

    val heartValue = mainViewModel.getHeartBeatState()?.value

    val status = when (heartValue?.status) {
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
        if (!timerIsStarted && heartValue?.hr != 0) {
            Log.w(TAG, "measurementTimerStart: ui Start")
            coroutineScopeOnDefault {
                measurementStart()
            }
            updateTimerFlag(true)
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val iconSize by infiniteTransition.animateFloat(
        initialValue = 35.0f,
        targetValue = 39.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 300, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(
                    if (mainViewModel.timerIsStarted) {
                        when (status) {
                            "측정중", "측정대기" -> iconSize.dp
                            else -> 39.dp
                        }
                    } else {
                        39.dp
                    }
                ),
                painter = painterResource(id = R.drawable.heart),
                contentDescription = null
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = status,
                color = when (status) {
                    "측정중" -> allowColor.copy(alpha = 0.8f)
                    "측정불가" -> denyColor.copy(alpha = 0.8f)
                    else -> primaryColor.copy(alpha = 0.8f)
                },
                style = MaterialTheme.typography.caption2
            )
            Text(
                text = (heartValue?.hr ?: 0).toString(),
                color = when (status) {
                    "측정중" -> allowColor.copy(alpha = 0.8f)
                    "측정불가" -> denyColor.copy(alpha = 0.8f)
                    else -> primaryColor.copy(alpha = 0.8f)
                },
                style = MaterialTheme.typography.body1
            )
        }

    }
}

@Composable
private fun BloodPressureContent() {

    val infiniteTransition = rememberInfiniteTransition()
    val iconSize by infiniteTransition.animateFloat(
        initialValue = 35.0f,
        targetValue = 39.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 300, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(if (false) iconSize.dp else 35.dp),
                painter = painterResource(id = R.drawable.arm),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier.padding(start = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "측정불가",
                color = primaryColor,
                style = MaterialTheme.typography.caption2

            )
            Text(
                text = "0",
                color = primaryColor,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun DataSendButton() {
    val context = LocalContext.current
    val mainViewModel = (LocalContext.current as MainActivity).mainViewModel
    val isMeasurementEnd = mainViewModel.isSendingButtonEnable.collectAsState().value

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, disableButtonColor, RoundedCornerShape(12.dp))
                .noDuplicationClickable(enabled = isMeasurementEnd && mainViewModel.timerIsStarted) {
                    Toast.makeText(context, "재측정 시작", Toast.LENGTH_SHORT).show()
                    coroutineScopeOnDefault {
                        mainViewModel.run {
                            measurementCancel()
                            measurementStart()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp),
                text = "재측정",
                color = if (isMeasurementEnd && mainViewModel.timerIsStarted) {
                    enableButtonColor
                } else {
                    disableButtonColor.copy(alpha = 0.5f)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, disableButtonColor, RoundedCornerShape(12.dp))
                .noDuplicationClickable(enabled = isMeasurementEnd) {
                    Toast
                        .makeText(context, "전송", Toast.LENGTH_SHORT)
                        .show()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp),
                text = "전송",
                color = if (isMeasurementEnd) enableButtonColor else disableButtonColor.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
