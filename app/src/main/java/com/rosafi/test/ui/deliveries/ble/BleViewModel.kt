package com.rosafi.test.ui.deliveries.ble

import android.app.Activity
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.rosafi.test.R
import java.util.*

class BleViewModel(private val activity: Activity) : ViewModel() {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val ENABLE_BLE_REQ_CODE = 1


    fun checkBleAvailability() {
        activity.packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
            Toast.makeText(activity, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
        }
    }

    fun checkIfBluetoothIsEnabled(){

        bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, ENABLE_BLE_REQ_CODE)
        }

    }

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)


    fun createDeliveryService(deliveryUUID: String, senderUUID: String): BluetoothGattService {
        val service = BluetoothGattService(UUID.fromString(deliveryUUID),
                BluetoothGattService.SERVICE_TYPE_PRIMARY)


        val serialData = BluetoothGattCharacteristic(UUID.fromString(senderUUID),
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE)

        service.addCharacteristic(serialData)

        return service
    }



}