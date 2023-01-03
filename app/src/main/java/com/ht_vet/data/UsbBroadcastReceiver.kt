package com.ht_vet.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbRequest
import android.util.Log
import com.ht_vet.core.extensions.getParcelable
import com.ht_vet.core.extensions.goAsync
import java.nio.ByteBuffer

class UsbBroadcastReceiver : BroadcastReceiver() {

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (intent?.action != ACTION_USB_PERMISSION) return@goAsync
        synchronized(this) {
            val usbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
            val device: UsbDevice? = intent.getParcelable(UsbManager.EXTRA_DEVICE)

            val usbInterface = device?.getInterface(0)
            val endpoint = usbInterface?.getEndpoint(1) ?: return@synchronized
            usbManager.openDevice(device)?.apply {
                val array = ByteArray(endpoint.maxPacketSize)
                val buffer = ByteBuffer.allocate(endpoint.maxPacketSize)
//                claimInterface(usbInterface, true)
//                val bulkTransfer = bulkTransfer(endpoint, array, array.size, 0)
//                Log.d("defaultAppDebuger", "bulkTransfer array: $bulkTransfer")
                val usbRequest = UsbRequest()
                val initialize = usbRequest.initialize(this, endpoint)
                Log.d("defaultAppDebuger", "initialize: $initialize")
                val queue = usbRequest.queue(buffer)
                Log.d("defaultAppDebuger", "queue: $queue")
                Log.d("defaultAppDebuger", "buffer: $buffer")
//                array.forEach {
//                    Log.d("defaultAppDebuger", "bulk array: $it")
//                }

            }
        }
    }
}