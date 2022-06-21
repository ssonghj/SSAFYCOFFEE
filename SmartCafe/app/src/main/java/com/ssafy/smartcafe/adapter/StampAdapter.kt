package com.ssafy.smartcafe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.dto.ProductDTO

class StampAdapter(var context: Context, val resource: Int, list:List<String>)
    : RecyclerView.Adapter<StampHolder>() {
    //사용하고자 하는 데이터
    var listData = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return StampHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: StampHolder, position: Int) {

        var img = listData[position]

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.couponeImg.setImageResource(resId)
    }
}

class StampHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var couponeImg: ImageView = itemView!!.findViewById(R.id.img_item)
}
