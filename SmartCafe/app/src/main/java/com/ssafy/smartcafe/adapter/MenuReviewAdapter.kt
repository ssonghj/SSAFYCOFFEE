package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.activity.MenuReviewActivity
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.CommentService
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG="MenuReviewAdapter"
class MenuReviewAdapter(var context: Context, private val resource: Int,var productList: List<ProductDTO>, var product_id: Int)
    : RecyclerView.Adapter<MenuReviewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return MenuReviewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MenuReviewHolder, position: Int) {

        holder.userName.text = productList[position].userName
        holder.rating.rating = productList[position].rating.toFloat() / 2
        holder.content.text = productList[position].comment

        var inputUserId = productList[position].user_id
        if(inputUserId != userId){
            holder.btn_modify.visibility = View.GONE
            holder.btn_delete.visibility = View.GONE
        }

        //수정 버튼 클릭시 내용 수정
        holder.btn_modify.setOnClickListener{

        }

        //삭제 버튼 클릭시 삭제
        holder.btn_delete.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                deleteComment(productList[position].commentId)
                getComment(product_id)
                notifyItemRemoved(position)
            }
        }
    }

    private suspend fun getComment(product_id:Int) {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectProduct(product_id).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!

                println("MenuReviewAdapter : ${productList}")
            } else {
                print("MenuReviewAdapter: error code")
            }
        }
    }

    private suspend fun deleteComment(id:Int){
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(CommentService::class.java)
            val response = service.deleteComment(id)

            if (response.code() == 200) {
                var res = response.body()!!

                println("deleteComment : ${res}")
            } else {
                print("deleteComment: error code")
            }
        }
    }
}


class MenuReviewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var userName: TextView = itemView!!.findViewById(R.id.tv_user_name)
    var rating: RatingBar = itemView!!.findViewById(R.id.tv_rating)
    var content: TextView = itemView!!.findViewById(R.id.tv_review_detail)
    var btn_modify: TextView = itemView!!.findViewById(R.id.tv_modify)
    var btn_delete: TextView = itemView!!.findViewById(R.id.tv_delete)
}