package mago.apps.wearhealthrawdata.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import mago.apps.orot_medication.model.State
import mago.apps.wearhealthrawdata.R
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity
import mago.apps.wearhealthrawdata.presentation.ui.Screens
import mago.apps.wearhealthrawdata.presentation.ui.theme.Red400
import mago.apps.wearhealthrawdata.presentation.ui.theme.disableButtonColor
import mago.apps.wearhealthrawdata.presentation.ui.theme.enableButtonColor
import mago.apps.wearhealthrawdata.presentation.ui.theme.primaryColor
import mago.apps.wearhealthrawdata.presentation.ui.utils.compose.noDuplicationClickable

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val activity = context as MainActivity

    ServerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
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
            LoadingBar()
        }
    }

}

@Composable
private fun LoadingBar() {
    val context = LocalContext.current
    val activity = context as MainActivity
    val loadingBarIsShowing = activity.mainViewModel.loadingBarIsShowing.collectAsState().value

    if (loadingBarIsShowing) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Red400)
        }
    }
}

@Composable
private fun ServerState() {
    val context = LocalContext.current
    val activity = context as MainActivity
    val serverState = activity.mainViewModel.serverState.collectAsState().value

    LaunchedEffect(key1 = serverState) {
        if (serverState.first == State.ERROR) {
            Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
        } else if (serverState.first == State.CONNECTED) {
            Toast.makeText(context, "서버 연결 성공", Toast.LENGTH_SHORT).show()
            activity.mainViewModel.navController.navigate(Screens.Measurement.route)
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
                activity.mainViewModel.connectionOrotServer()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 14.dp),
            text = "시작하기",
            color = primaryColor,
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
        color = primaryColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = "(Raw Data Collector)",
        color = primaryColor.copy(alpha = 0.4f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
}


@Composable
private fun ColumnScope.Footer() {
    Text(
        modifier = Modifier.weight(0.15f),
        text = "Mago & OrotCode",
        color = primaryColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
    )
}