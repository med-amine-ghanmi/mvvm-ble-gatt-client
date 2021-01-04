package com.rosafi.test.ui.deliveries

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rosafi.test.R
import com.rosafi.test.data.model.VerificationRequestBody
import com.rosafi.test.databinding.DeliveriesFragmentBinding
import com.rosafi.test.ui.deliveries.adapter.DeliveriesRecyclerViewAdapter
import com.rosafi.test.ui.deliveries.ble.BleViewModel
import com.rosafi.test.utils.Util

class DeliveriesFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveriesFragment()
    }

    private lateinit var viewModel: DeliveriesViewModel
    private lateinit var viewBinding: DeliveriesFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.deliveries_fragment, container, false)
        return viewBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModels()

    }

    private fun initViews(){

        viewBinding.deliveriesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this.context, 1)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.deliveries_frag_title)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initViewModels(){

        viewModel = ViewModelProvider(this).get(DeliveriesViewModel::class.java)

        val bleViewModel = ViewModelProvider(this).get(BleViewModel::class.java)
        bleViewModel.activity = requireActivity()
        bleViewModel.checkBleAvailability()
        bleViewModel.checkIfBluetoothIsEnabled()

        val deliveriesRecyclerViewAdapter by lazy {DeliveriesRecyclerViewAdapter(ArrayList(), viewModel)  }

        viewModel.getRemoteDeliveries()

        viewModel.deliveriesLiveData.observe(viewLifecycleOwner, Observer {
            deliveriesRecyclerViewAdapter.updateList(it)
            viewBinding.deliveriesRecyclerView.adapter = deliveriesRecyclerViewAdapter

        })
        viewModel.markAsDoneLiveData.observe(viewLifecycleOwner, Observer {

            deliveriesRecyclerViewAdapter.updateDeliveryStatus(it.second)
            Util.toastSuccess(requireContext(), getString(R.string.status_updated_txt))
            bleViewModel.targetServiceUUID = it.first.uuid!!
            bleViewModel.targetServiceUUID = it.first.senderUuid!!
            bleViewModel.startAdvertising(it.first.senderUuid!!)
            bleViewModel.startServer()
            bleViewModel.addGattService(it.first.uuid!!, it.first.senderUuid!!)

        })

        viewModel.codeVerificationLiveData.observe(viewLifecycleOwner, Observer {

            deliveriesRecyclerViewAdapter.updateByDeliveryUUID(it.first.elementUuid.toString())
            Util.toastSuccess(requireContext(), getString(R.string.status_updated_txt))
            bleViewModel.bluetoothGattServer.notifyCharacteristicChanged(it.second.bluetoothDevice, it.second.characteristic, it.second.confirm)
            bleViewModel.stopAdvertising()
            bleViewModel.stopServer()

        })


        bleViewModel.confirmationLiveData.observe(viewLifecycleOwner, Observer {
            Util.toastSuccess(requireContext(), it.first.confirmationCode.toString())
            Util.toastSuccess(requireContext(), "Sending verification code to server ...")
            viewModel.sendVerificationCode(it.first, it.second)

        })






    }


}