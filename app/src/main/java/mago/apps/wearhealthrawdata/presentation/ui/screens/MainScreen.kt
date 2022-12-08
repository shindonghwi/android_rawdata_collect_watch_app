package mago.apps.wearhealthrawdata.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import mago.apps.wearhealthrawdata.presentation.ui.MainActivity

@Composable
fun MainScreen() {

    val activity = LocalContext.current as MainActivity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            activity.mainViewModel.heartBeatState?.value.toString(),
            color = Color.Black,
            fontSize = 20.sp
        )
    }
}