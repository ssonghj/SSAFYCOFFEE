package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.activity.WriteReviewActivity
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.CommentService
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyReviewAdapter(var context: Context, private val resource: Int, var productList: List<ProductDTO>)
    : RecyclerView.Adapter<MyReviewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return MyReviewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyReviewHolder, position: Int) {

        holder.productName.text = productList[position].name
        holder.rating.rating = productList[position].rating.toFloat() / 2
        holder.content.text = productList[position].comment

        var inputUserId = productList[position].user_id
        if(inputUserId != userId){
            holder.btn_modify.visibility = View.GONE
            holder.btn_delete.visibility = View.GONE
        }

        //수정 버튼 클릭시 내용 수정
        holder.btn_modify.setOnClickListener{
            val intent = Intent(context, WriteReviewActivity::class.java)
            intent.putExtra("modify",true)
            intent.putExtra("commentId", productList[position].commentId)
            intent.putExtra("menuName",productList[position].name)
            intent.putExtra("rating", productList[position].rating.toFloat()/2)
            intent.putExtra("content",productList[position].comment)
            intent.putExtra("productId", productList[position].id)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        //삭제 버튼 클릭시 삭제
        holder.btn_delete.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                deleteComment(productList[position].commentId)
                getAllMyComment()
                notifyItemRemoved(position)
            }
        }
    }

    private suspend fun getAllMyComment() {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(ProductService::class.java)
            val response = service.selectAllProductOfUserComment(userId).execute()

            if (response.code() == 200) {
                productList = (response.body() as ArrayList<ProductDTO>?)!!

                println("getAllMyComment : ${productList}")
            } else {
                print("getAllMyComment: error code")
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


class MyReviewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var productName: TextView = itemView!!.findViewById(R.id.tv_user_name)
    var rating: RatingBar = itemView!!.findViewById(R.id.tv_rating)
    var content: TextView = itemView!!.findViewById(R.id.tv_review_detail)
    var btn_modify: TextView = itemView!!.findViewById(R.id.tv_modify)
    var btn_delete: TextView = itemView!!.findViewById(R.id.tv_delete)
}