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
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.dto.ProductDTO

private const val TAG="MenuReviewAdapter"
class MenuReviewAdapter(var context: Context, private val resource: Int, list: List<ProductDTO>)
    : RecyclerView.Adapter<MenuReviewHolder>() {
    //사용하고자 하는 데이터
    private var productList = list

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

            //클릭시 다른 효과
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