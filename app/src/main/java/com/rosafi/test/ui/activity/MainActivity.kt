package com.rosafi.test.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.rosafi.test.R
import com.rosafi.test.databinding.ActivityMainBinding
import com.rosafi.test.ui.deliveries.DeliveriesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(viewBinding.root)

        initViews()
        initDeliveriesFragment()

    }


    private fun initDeliveriesFragment(){

        supportFragmentManager.beginTransaction().add(R.id.deliveryFragmentContainer, DeliveriesFragment()).commit()
    }

    private fun initViews(){

        setSupportActionBar(viewBinding.mainActivityToolbar)

    }
}