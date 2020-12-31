package com.rosafi.test.ui.deliveries.ble

import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rosafi.test.R
import com.rosafi.test.utils.Constants.Companion.SCAN_PERIOD
import java.util.*


class BleViewModel() : ViewModel() {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothGattServer: BluetoothGattServer
    private lateinit var bluetoothLeAdvertiser: BluetoothLeAdvertiser
    private lateinit var advertisingCallback: AdvertiseCallback

    private  val _confirmationLiveData = MutableLiveData<String>()
    val confirmationLiveData: LiveData<String> = _confirmationLiveData

    private val ENABLE_BLE_REQ_CODE = 1

    lateinit var activity: Activity
    lateinit var dataToSend: String

    var targetServiceUUID = ""
    var targetCharacteristic = ""

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

    // ********************************************* GattServer Logic ************************************** //

    fun startServer() {
        bluetoothGattServer = bluetoothManager.openGattServer(activity, gattServerCallback)
    }

    fun addGattService(deliveryUUID: String, senderUUID: String){
        bluetoothGattServer.addService(createDeliveryService(deliveryUUID, senderUUID))
    }



    private val gattServerCallback = object : BluetoothGattServerCallback() {

        override fun onCharacteristicWriteRequest(device: BluetoothDevice?, requestId: Int, characteristic: BluetoothGattCharacteristic?, preparedWrite: Boolean, responseNeeded: Boolean, offset: Int, value: ByteArray?) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value)
            _confirmationLiveData.postValue(value.toString())

        }

        override fun onExecuteWrite(device: BluetoothDevice?, requestId: Int, execute: Boolean) {
            super.onExecuteWrite(device, requestId, execute)

        }

    }

        private fun stopServer() {
        bluetoothGattServer.close()


    }



    // ********************************************* Advertiser Logic ************************************** //
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startAdvertising(){
        initBleAdvertiseCallback()
        bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser
        bluetoothLeAdvertiser.startAdvertising(initAdvertiseSettings(),initAdvertiseData(), initAdvertiseScanData(), advertisingCallback)
    }


    private fun initBleAdvertiseCallback(){
        advertisingCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.d(TAG, "BLE advertisement added successfully")
            }

            override fun onStartFailure(errorCode: Int) {
                Log.e(TAG, "Failed to add BLE advertisement, reason: $errorCode")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initAdvertiseSettings() : AdvertiseSettings {
        return AdvertiseSettings.Builder()
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initAdvertiseData() : AdvertiseData {
        return AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initAdvertiseScanData() : AdvertiseData {

        return  AdvertiseData.Builder()
                .setIncludeTxPowerLevel(true)
                .build()
    }




    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)


    private fun createDeliveryService(deliveryUUID: String, senderUUID: String): BluetoothGattService {
        val service = BluetoothGattService(UUID.fromString(deliveryUUID),
                BluetoothGattService.SERVICE_TYPE_PRIMARY)



        val serialData = BluetoothGattCharacteristic(UUID.fromString(senderUUID),
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE)

        service.addCharacteristic(serialData)



        return service
    }


    // ********************************************* Scanner Logic ************************************** //


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startLeScan(){
        bluetoothAdapter.bluetoothLeScanner.startScan(mLeScanCallback);

        val address = "C4:8E:8F:22:DE:22"
        val device = bluetoothAdapter.getRemoteDevice(address)
        if (device == null) {
            Toast.makeText(activity, "Device not found, unable to connect", Toast.LENGTH_LONG).show()
        }

        val mHandler = Handler()

        mHandler.postDelayed(Runnable { bluetoothAdapter.bluetoothLeScanner.stopScan(mLeScanCallback) }, SCAN_PERIOD)


    }



    private val mLeScanCallback : ScanCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ScanCallback(){
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }


    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val intentAction: String
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // This is where you will call the method to get the service

                val serviceList: List<BluetoothGattService> = gatt.services
                Log.w(TAG, "onServicesDiscovered received: " + serviceList.size + serviceList.toString())
                for (i in serviceList.indices) {
                    Log.w(TAG, "onServicesDiscovered received: " + serviceList[i].uuid.toString())
                }
                for (i in serviceList.indices) {
                    for (j in serviceList[i].characteristics.indices) {
                        Log.w(TAG, "onServicesDiscovered received: " + serviceList[i].characteristics[j].uuid)
                        if (serviceList[i].characteristics[j].uuid.toString() == targetCharacteristic) {
                            val myCharact = serviceList[i].characteristics[j]
                            //byte[] value = new byte[1];
                            //value[0] = (byte) (0xFF);
                            val decodedDataToSend = Base64.decode(dataToSend, Base64.DEFAULT);
                            myCharact.setValue(decodedDataToSend)
                            gatt.writeCharacteristic(myCharact)
                            Log.w(TAG, "onServicesDiscovered received: " + "data sent" + Arrays.toString("hello".toByteArray()))
                        }
                    }
                }
            } else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }
    }


}