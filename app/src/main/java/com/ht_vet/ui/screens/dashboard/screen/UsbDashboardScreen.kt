package com.ht_vet.ui.screens.dashboard.screen

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ht_vet.core.constants.PermissionConstants.USB_PERMISSION
import com.ht_vet.data.UsbBroadcastReceiver
import com.ht_vet.ui.screens.dashboard.viewmodel.DashboardViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


//@RootNavGraph(start = true)
@ExperimentalComposeUiApi
@Destination
@Composable
fun UsbDashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {})

    /**
     *  No endpoints devices:
     *  --------------------
     *  USB PnP Audio Device
     *  USB Billboard Device
     *
     *  Devices with endpoints:
     *  ----------------------
     *  AX88179A
     *  HD camera
     *  PureThermal // one of the cameras
     *  USB 2.0 Camera // one of the cameras
     *
     */
    LaunchedEffect(key1 = true) {
        launcher.launch(android.Manifest.permission.CAMERA)
        launcher.launch(android.Manifest.permission.RECORD_AUDIO)
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val filter = IntentFilter(USB_PERMISSION)
        registerReceiver(context, UsbBroadcastReceiver(), filter, RECEIVER_NOT_EXPORTED)

        //For each device I wrote all of the interfaces and endpoints with the results of the
        // 'bulkTransfer()' method

        /* total interfaces : 1
       interface 0:
       endpoint 0 - bulkTransfer of -1
       endpoint 1 - bulkTransfer of 512
       */
        val hdCamera = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("HD camera")
        } ?: return@LaunchedEffect
        /* total interfaces : 1
        interface 0:
        bulkTransfer of 512 with endpoint 3
        bulkTransfer of 512 with endpoint 2
        bulkTransfer of -1 with endpoint 1
        bulkTransfer of -1 with endpoint 0
        */
        val aX88179A = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("AX88179A")
        } ?: return@LaunchedEffect
        /* total interfaces : 4
       interface 0:
       endpoint 0 - bulkTransfer of -1
       interface 1: no endpoint
       interface 1: no endpoint
       interface 1: no endpoint
       */
        val pureThermal = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("PureThermal")
        } ?: return@LaunchedEffect
        /* total interfaces : 5
        interface 0:
        endpoint 0 - bulkTransfer of -1
        interface 1:
        endpoint 0 - bulkTransfer of -1
        interface 2 & 3: no endpoints
        interface 4:
        endpoint 0 - bulkTransfer of -1
        */
        val usbCamera = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("USB 2.0 Camera")
        } ?: return@LaunchedEffect
        val permissionIntent = PendingIntent.getBroadcast(
            context,
            0, Intent(USB_PERMISSION),
            0
        )
        usbManager.requestPermission(pureThermal, permissionIntent)
    }
}

