package mago.apps.wearhealthrawdata.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mago.apps.wearhealthrawdata.presentation.ui.screens.MainScreen
import mago.apps.wearhealthrawdata.presentation.ui.screens.MainViewModel
import mago.apps.wearhealthrawdata.presentation.ui.screens.MeasurementScreen
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
        }

        setContent {
            WearHealthRawDataTheme {
                NavGraph()
            }
        }
    }

    companion object {
        const val TAG = "WATCH APP TAG"
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(TAG, "onDestroy: ", )
    }
}


@Composable
fun NavGraph() {
    val context = LocalContext.current
    val activity = context as MainActivity
    activity.mainViewModel.navController = rememberNavController()
    NavHost(
        navController = activity.mainViewModel.navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            MainScreen()
        }
        composable(route = Screens.Measurement.route) {
            MeasurementScreen()
        }
    }
}


sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Measurement : Screens("Measurement")
}