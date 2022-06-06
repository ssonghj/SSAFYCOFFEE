package com.ssafy.smartcafe.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.UserRecentOrderListActivity
import com.ssafy.smartcafe.activity.UserReviewActivity
import com.ssafy.smartcafe.databinding.FragmentMypageBinding
import com.ssafy.smartcafe.databinding.FragmentOrderBinding

class MypageFragment : Fragment() {
    private lateinit var ctx: Context
    private lateinit var binding: FragmentMypageBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //context받아옴
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater)

        binding.frameReview.setOnClickListener{
            val intent = Intent(ctx, UserReviewActivity::class.java)
            startActivity(intent)
        }

        binding.frameOrderList.setOnClickListener{
            val intent = Intent(ctx, UserRecentOrderListActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}