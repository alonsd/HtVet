package com.ht_vet.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.ht_vet.core.constants.PermissionConstants.USB_PERMISSION
import com.ht_vet.core.extensions.getParcelable
import com.ht_vet.core.extensions.goAsync

class UsbBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (intent?.action != USB_PERMISSION) return@goAsync
        synchronized(this) {
            val usbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
            val device: UsbDevice? = intent.getParcelable(UsbManager.EXTRA_DEVICE)

            val usbInterface = device?.getInterface(3)
            val endpoint = usbInterface?.getEndpoint(0) ?: return@synchronized
            usbManager.openDevice(device)?.apply {
                val array = ByteArray(endpoint.maxPacketSize / 2)
                claimInterface(usbInterface, true)
                val bulkTransfer = bulkTransfer(endpoint, array, array.size, 0)
                Log.d("defaultAppDebuger", "bulkTransfer: $bulkTransfer")
                val stringBuilder = StringBuilder()
                array.forEach {
                    stringBuilder.append("$it ")
                }
                Log.d("defaultAppDebuger", "stringBuilder: $stringBuilder")

            }
        }
    }
}