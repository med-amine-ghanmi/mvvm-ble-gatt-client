package com.rosafi.test.ui.deliveries

import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rosafi.test.R
import com.rosafi.test.databinding.DeliveriesFragmentBinding
import com.rosafi.test.ui.deliveries.adapter.DeliveriesRecyclerViewAdapter
import com.rosafi.test.ui.deliveries.ble.BleViewModel
import com.rosafi.test.utils.Util

class DeliveriesFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveriesFragment()
    }

    private lateinit var viewModel: DeliveriesViewModel
    private lateinit var bleViewModel: BleViewModel
    private lateinit var viewBinding: DeliveriesFragmentBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 2
    private val deliveriesRecyclerViewAdapter by lazy {DeliveriesRecyclerViewAdapter(ArrayList(), viewModel)  }
    private var selectedPosition = -1
    private var targetCharacteristic = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.deliveries_fragment, container, false)
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModels()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

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
        bleViewModel = ViewModelProvider(this).get(BleViewModel::class.java)
        bleViewModel.activity = requireActivity()


        viewModel.getRemoteDeliveries()

        viewModel.deliveriesLiveData.observe(viewLifecycleOwner, Observer {
            deliveriesRecyclerViewAdapter.updateList(it)
            viewBinding.deliveriesRecyclerView.adapter = deliveriesRecyclerViewAdapter

        })

        viewModel.markAsDoneLiveData.observe(viewLifecycleOwner, Observer {
            selectedPosition = it.second
            deliveriesRecyclerViewAdapter.updateDeliveryStatus(it.second)
            Util.toastSuccess(requireContext(), getString(R.string.status_updated_txt))

            bleViewModel.targetServiceUUID = deliveriesRecyclerViewAdapter.getDeliveryByPosition(selectedPosition)?.uuid.toString()
            bleViewModel.targetCharacteristic = deliveriesRecyclerViewAdapter.getDeliveryByPosition(selectedPosition)?.receiverUuid.toString()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission()
            }
            else {
                initBLeScanner()
            }


        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initBLeScanner(){
        bleViewModel.advertisedServiceUUID = deliveriesRecyclerViewAdapter.getDeliveryByPosition(selectedPosition).toString()
        bleViewModel.initBleAdapter()
        bleViewModel.startLeScan()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
            )
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            else {

                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }

        } else {
            initBLeScanner()
        }

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    for (permissionResult: Int in grantResults) {
                        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                            initBLeScanner()
                            break

                        } else {
                            Util.toastSuccess(requireContext(), getString(R.string.location_not_permitted))
                            break


                        }

                    }

                }
            }

        }
    }

}