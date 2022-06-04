package com.ssafy.smartcafe.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private lateinit var binding:FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.order_nav_host) as NavHostFragment

        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        return binding.root
    }
}