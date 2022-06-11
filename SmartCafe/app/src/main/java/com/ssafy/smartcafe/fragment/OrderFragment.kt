package com.ssafy.smartcafe.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.activity.ShoppingListActivity
import com.ssafy.smartcafe.databinding.FragmentOrderBinding
import com.ssafy.smartcafe.viewModel.AllFragmentViewModel
import com.ssafy.smartcafe.viewModel.JoinViewModel

class OrderFragment : Fragment() {
    private lateinit var ctx: Context
    private lateinit var binding:FragmentOrderBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //context받아옴
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = DataBindingUtil.setContentView(this@OrderFragment, R.layout.fragment_order)
        binding = FragmentOrderBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.order_nav_host) as NavHostFragment

        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)


        //임시로 xml확인용
        binding.btnShoppingList.setOnClickListener{
            var intent = Intent(ctx,ShoppingListActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}