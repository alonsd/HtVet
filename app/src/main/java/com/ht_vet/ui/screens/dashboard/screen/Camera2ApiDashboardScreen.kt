package com.ht_vet.ui.screens.dashboard.screen

import android.content.Context
import android.content.pm.PackageManager.FEATURE_CAMERA_EXTERNAL
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ht_vet.ui.screens.dashboard.viewmodel.DashboardViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RootNavGraph(start = true)
@ExperimentalComposeUiApi
@Destination
@Composable
fun Camera2ApiDashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        val hasSystemFeature = context.packageManager.hasSystemFeature(FEATURE_CAMERA_EXTERNAL)
        // hasSystemFeature returns false, means that we can't use Camera2 API
        val cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.registerAvailabilityCallback(object : CameraManager.AvailabilityCallback() {

            override fun onCameraAvailable(cameraId: String) {
                super.onCameraAvailable(cameraId)
                //Always returns 0 and 1, regardless if the external cameras are connected or not
            }
        }, null)
    }
}

