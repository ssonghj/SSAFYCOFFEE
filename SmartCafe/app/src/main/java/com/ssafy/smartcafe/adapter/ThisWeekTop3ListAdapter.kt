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
import com.ssafy.smartcafe.dto.OrderDTOwithTotal
import com.ssafy.smartcafe.dto.ProductDTO

private const val TAG="ThisWeekTop3ListAdapter"
class ThisWeekTop3ListAdapter(var context: Context, private val resource: Int, list: List<OrderDTOwithTotal>) : RecyclerView.Adapter<ThisWeekTop3Holder>() {
    //사용하고자 하는 데이터
    private var top3List = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThisWeekTop3Holder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return ThisWeekTop3Holder(view)
    }

    override fun getItemCount(): Int {
        return top3List.size
    }

    override fun onBindViewHolder(holder: ThisWeekTop3Holder, position: Int) {

        var img = top3List[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productImg.setImageResource(resId)

        holder.productName.text = "${top3List[position].name}"
        holder.productImg.setOnClickListener{
            
            var intent = Intent(context, MenuDetailActivity::class.java)
            intent.putExtra("productId", top3List[position].p_id.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }


}

class ThisWeekTop3Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var productImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)
}