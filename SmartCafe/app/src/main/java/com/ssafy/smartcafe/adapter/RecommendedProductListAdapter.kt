package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.dto.ProductDTO

private const val TAG="RecommendedProductListAdapter"
class RecommendedProductListAdapter(var context: Context, private val resource: Int, list: List<ProductDTO>) : RecyclerView.Adapter<RecommendedProductHolder>() {
    //사용하고자 하는 데이터
    private var productList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return RecommendedProductHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: RecommendedProductHolder, position: Int) {

        var img = productList[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productImg.setImageResource(resId)

        holder.productName.text = "${productList[position].name}"
        holder.productImg.setOnClickListener{
            var intent = Intent(context, MenuDetailActivity::class.java)
            intent.putExtra("productId", productList[position].id.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


}

class RecommendedProductHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var productImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)

}