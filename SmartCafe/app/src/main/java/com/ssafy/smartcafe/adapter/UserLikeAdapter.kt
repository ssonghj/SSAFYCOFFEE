package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelLazy
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.UserLikeDTO
import com.ssafy.smartcafe.service.ProductService
import com.ssafy.smartcafe.viewModel.MenuDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserLikeAdapter(var context: Context, val resource: Int,var listData:List<ProductDTO>)
    : RecyclerView.Adapter<UserLikeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserLikeHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return UserLikeHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: UserLikeHolder, position: Int) {

        var img = listData[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productImg.setImageResource(resId)
        //메뉴 상세페이지로 넘어가기
        holder.productImg.setOnClickListener{
            var intent = Intent(context, MenuDetailActivity::class.java)
            intent.putExtra("productId", listData[position].id.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        holder.productName.text = listData[position].name

        //하트 아이콘 누르면 현재 찜 메뉴에서 사라짐
        holder.heartIcon.setOnClickListener{

            MenuDetailViewModel().setNotLike()
            //서버에 좋아하는 메뉴에서 삭제
            CoroutineScope(Dispatchers.Main).launch {
                DeleteUserLikeMenu(listData[position].id);
                getAllUserLikeMenu()
                notifyDataSetChanged()
            }
        }
    }

    private suspend fun DeleteUserLikeMenu(id:Int) {
        var userLikeDTO = UserLikeDTO(0, LoginActivity.userId,id)

        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.removeLikeMenu(userLikeDTO)

            if (response.code() == 200) {
                var res = response.body()!!
                println("DeleteUserLikeMenu : ${res}")
            } else {
                println( "DeleteUserLikeMenu: error code")
            }
        }
    }

    private suspend fun getAllUserLikeMenu() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectUserLikeMenu(LoginActivity.userId).execute()

            if (response.code() == 200) {
                listData = (response.body() as ArrayList<ProductDTO>?)!!
                println("getAllUserLikeMenu : ${listData}")
            } else {
                println("getAllUserLikeMenu: error code")
            }
        }
    }
}

class UserLikeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var productImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)
    var heartIcon: ImageView = itemView!!.findViewById(R.id.img_heart)
}
