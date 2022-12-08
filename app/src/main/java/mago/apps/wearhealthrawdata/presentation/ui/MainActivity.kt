package mago.apps.wearhealthrawdata.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import mago.apps.wearhealthrawdata.presentation.ui.screens.MainScreen
import mago.apps.wearhealthrawdata.presentation.ui.screens.MainViewModel
import mago.apps.wearhealthrawdata.presentation.ui.theme.WearHealthRawDataTheme


class MainActivity : ComponentActivity() {

    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                "android.permission.BODY_SENSORS"
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), 0)
        } else {
            mainViewModel.run {
                initTrackingHelper(this@MainActivity)
                Log.w(TAG, "mainViewModel init")
            }

        }

        setContent {
            WearApp()
        }
    }

    companion object {
        const val TAG = "WATCH APP TAG"
    }
}

@Composable
fun WearApp() {
    WearHealthRawDataTheme {
        MainScreen()
    }
}
