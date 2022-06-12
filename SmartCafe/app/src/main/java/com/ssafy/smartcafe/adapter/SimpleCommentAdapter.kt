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
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.dto.ProductDTO
import org.w3c.dom.Text

private const val TAG="SimpleCommnetAdapter"
//전체메뉴 어댑터
class SimpleCommentAdapter(var context: Context, val resource: Int, list:List<ProductDTO>)
    : RecyclerView.Adapter<SimpleCommentHolder>() {
    //사용하고자 하는 데이터
    var listData = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleCommentHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return SimpleCommentHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: SimpleCommentHolder, position: Int) {

        //평점내용
        holder.comment.text = listData[position].comment
        //평점별만 넣음 됌
        holder.rating.rating = listData[position].rating.toFloat() / 2
    }
}

class SimpleCommentHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var comment: TextView = itemView!!.findViewById(R.id.tv_review_title)
    var rating: RatingBar = itemView!!.findViewById(R.id.tv_rating)
}
