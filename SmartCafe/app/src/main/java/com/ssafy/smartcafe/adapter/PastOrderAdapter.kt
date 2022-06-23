package com.ssafy.smartcafe.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.dto.RecentOrderDTO
import java.util.LinkedHashMap

class PastOrderAdapter(var context: Context, private val resource: Int,
                       var orderList: LinkedHashMap<Int, MutableList<RecentOrderDTO>>,
                       var mapKey:MutableList<Int>)
    : RecyclerView.Adapter<PastOrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastOrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(resource, parent, false)

        return PastOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: PastOrderHolder, position: Int) {

        //한번에 orderId별로 다 가져와서 두개 이상이면 ~외 ~잔, 이름, 가격 , 날짜 출력
        var curInfo = orderList.getValue(mapKey[position])

        var img = curInfo[0].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productsImg.setImageResource(resId)


        var sum = 0
        var totalQuantity = 0

        if(curInfo.size >= 2){
            for(i in curInfo.indices){
                sum += curInfo[i].price * curInfo[i].quantity
                totalQuantity += curInfo[i].quantity
            }
            holder.productsName.text = "${curInfo[0].name} 외 ${totalQuantity}"
        }else{
            sum += curInfo[0].price * curInfo[0].quantity
            holder.productsName.text = "${curInfo[0].name}"
        }


        holder.price.text = "${sum}원"

        var arr = curInfo[0].order_time.split("T")
        holder.date.text = arr[0]
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

class PastOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var productsImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productsName: TextView = itemView!!.findViewById(R.id.tv_item_name)
    var price: TextView = itemView!!.findViewById(R.id.tv_total_price)
    var date: TextView = itemView!!.findViewById(R.id.tv_order_date)
}