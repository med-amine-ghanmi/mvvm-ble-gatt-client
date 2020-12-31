package com.rosafi.test.ui.deliveries

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rosafi.test.R
import com.rosafi.test.databinding.DeliveriesFragmentBinding
import com.rosafi.test.ui.deliveries.adapter.DeliveriesRecyclerViewAdapter

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()

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

    private fun initViewModel(){

        viewModel = ViewModelProvider(this).get(DeliveriesViewModel::class.java)
        viewModel.getRemoteDeliveries()
        viewModel.deliveriesLiveData.observe(viewLifecycleOwner, Observer {

            viewBinding.deliveriesRecyclerView.adapter = DeliveriesRecyclerViewAdapter(it)

        })
    }


}