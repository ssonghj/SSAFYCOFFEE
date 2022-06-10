package com.ssafy.smartcafe.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelLazy
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.NotificationActivity
import com.ssafy.smartcafe.activity.SettingActivity
import com.ssafy.smartcafe.activity.UserRecentOrderListActivity
import com.ssafy.smartcafe.activity.UserReviewActivity
import com.ssafy.smartcafe.databinding.FragmentMypageBinding
import com.ssafy.smartcafe.databinding.FragmentOrderBinding
import com.ssafy.smartcafe.viewModel.JoinViewModel
import com.ssafy.smartcafe.viewModel.MypageViewModel

private const val TAG = "MypageFragment"
class MypageFragment : Fragment() {
    private lateinit var ctx: Context
    private lateinit var binding: FragmentMypageBinding
    val mainViewModel: MypageViewModel by ViewModelLazy(
        MypageViewModel::class,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )

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

        binding.btnNotification.setOnClickListener{
            val intent = Intent(ctx, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.btnSetting.setOnClickListener{
            val intent = Intent(ctx, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.frameLogout.setOnClickListener {
            Log.d(TAG, "onCreateView: 로그아웃 버튼 클릭")
        }
        return binding.root
    }

}