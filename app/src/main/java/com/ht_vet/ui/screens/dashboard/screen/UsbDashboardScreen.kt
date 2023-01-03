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
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.hilt.navigation.compose.hiltViewModel
import com.ht_vet.data.UsbBroadcastReceiver
import com.ht_vet.ui.screens.dashboard.viewmodel.DashboardViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@ExperimentalComposeUiApi
@Destination
@Composable
fun UsbDashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {})

    /**
     *  AX88179A
    USB PnP Audio Device���������������� //no endpoint
    PureThermal (fw:v1.3.0)
    USB Billboard Device //no endpoint
    HD camera ��������
    USB 2.0 Camera
     */
    LaunchedEffect(key1 = true) {
        launcher.launch(android.Manifest.permission.CAMERA)
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(context, UsbBroadcastReceiver(), filter, RECEIVER_NOT_EXPORTED)
        /* has 1 and 0 */
        // bulkTransfer of 512 with endpoint 1
        // bulkTransfer of -1 with endpoint 0
        val hdCamera = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("HD camera")
        } ?: return@LaunchedEffect
        // bulkTransfer of -1 with endpoint 0
        val pureThermal = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("PureThermal")
        } ?: return@LaunchedEffect
        // bulkTransfer of -1 with endpoint 0
        val usbCamera = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("USB 2.0 Camera")
        } ?: return@LaunchedEffect
        /* has 1 and 0 */
        // bulkTransfer of 512 with endpoint 3
        // bulkTransfer of 512 with endpoint 2
        // bulkTransfer of -1 with endpoint 1
        // bulkTransfer of -1 with endpoint 0
        val aX88179A = usbManager.deviceList.values.find { device ->
            val name = device.productName ?: return@LaunchedEffect
            name.contains("AX88179A")
        } ?: return@LaunchedEffect
        val device = pureThermal
        val permissionIntent = PendingIntent.getBroadcast(
            context,
            0, Intent(ACTION_USB_PERMISSION),
           0
        )
        usbManager.requestPermission(device, permissionIntent)
//        val usbInterface = device.getInterface(0)
//        val endpoint = usbInterface.getEndpoint(0) ?: return@LaunchedEffect
//        usbManager.openDevice(device)?.apply {
//            val array = ByteArray(endpoint.maxPacketSize)
//            claimInterface(usbInterface, true)
//            val bulkTransfer = bulkTransfer(endpoint, array, array.size, 3000)
//            Log.d("defaultAppDebuger", "bulk array: $bulkTransfer")
//            val usbRequest = requestWait()
//            usbRequest.initialize(this, endpoint)
//            array.forEach {
//                Log.d("defaultAppDebuger", "bulk array: $it")
//            }
//        }
    }
}

