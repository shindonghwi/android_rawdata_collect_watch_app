package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import mago.apps.wearhealthrawdata.presentation.ui.Screens
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.noDuplicationClickable

val enableButtonColor = Color(0xFF30C46B)
val disableButtonColor = Color(0xFFB0B1B0)

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val activity = context as MainActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .noDuplicationClickable {
                    activity.mainViewModel.updateMeasurementAvailableStatue(true)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppLogo()
                AppTitle()
                StartButton()
            }
            Footer()
        }
    }
}


@Composable
private fun StartButton() {
    val context = LocalContext.current
    val activity = context as MainActivity
    Box(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, enableButtonColor, RoundedCornerShape(12.dp))
            .noDuplicationClickable {
                activity.mainViewModel.navController.navigate(Screens.Measurement.route)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 14.dp),
            text = "시작하기",
            color = Color(0xFF444444),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun AppLogo() {
    Row(
        modifier = Modifier.padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, disableButtonColor.copy(alpha = 0.3f), CircleShape),
            painter = painterResource(id = R.drawable.mago_logo),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Icon(
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, disableButtonColor.copy(alpha = 0.3f), CircleShape),
            painter = painterResource(id = R.drawable.orot_logo),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun AppTitle() {
    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "Watch 데이터 수집기",
        color = Color(0xFF444444),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = "(Raw Data Collector)",
        color = Color(0xFF444444).copy(alpha = 0.4f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
}


@Composable
private fun ColumnScope.Footer() {
    Text(
        modifier = Modifier.weight(0.15f),
        text = "Mago & OrotCode",
        color = Color(0xFF444444),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
    )
}