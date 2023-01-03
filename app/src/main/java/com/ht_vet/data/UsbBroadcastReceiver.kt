package com.ht_vet.data

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.ht_vet.core.extensions.getParcelable

class UsbBroadcastReceiver : BroadcastReceiver() {

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ACTION_USB_PERMISSION) return
        synchronized(this) {
            val usbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
            val device: UsbDevice? = intent.getParcelable(UsbManager.EXTRA_DEVICE)

            val usbInterface = device?.getInterface(0)
            val endpoint = usbInterface?.getEndpoint(1) ?: return@synchronized
            usbManager.openDevice(device)?.apply {
                val array = ByteArray(endpoint.maxPacketSize)
                claimInterface(usbInterface, true)
                val bulkTransfer = bulkTransfer(endpoint, array, array.size, 3000)
                Log.d("defaultAppDebuger", "bulk array: $bulkTransfer")
                val usbRequest = requestWait()
                usbRequest.initialize(this, endpoint)
                array.forEach {
                    Log.d("defaultAppDebuger", "bulk array: $it")
                }
            }

//            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                device?.apply {
//                    //call method to set up device communication
//                    Log.d("defaultAppDebuger", "achievemed permissions:")
//                }
//            } else {
//                Log.d(TAG, "permission denied for device $device")
//            }
        }
    }
}