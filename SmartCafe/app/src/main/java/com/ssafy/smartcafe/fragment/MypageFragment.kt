package com.ssafy.smartcafe.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.smartcafe.activity.*
import com.ssafy.smartcafe.databinding.FragmentMypageBinding


private const val TAG = "MypageFragment"
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

        binding.tvUserName.text = LoginActivity.userName+"님"

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