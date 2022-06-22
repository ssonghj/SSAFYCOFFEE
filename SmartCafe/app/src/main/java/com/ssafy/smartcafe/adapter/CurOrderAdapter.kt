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
import org.w3c.dom.Text
import java.util.LinkedHashMap

class CurOrderAdapter(var context: Context, private val resource: Int,var list: List<RecentOrderDTO>)
    : RecyclerView.Adapter<CurOrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurOrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(resource, parent, false)

        return CurOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurOrderHolder, position: Int) {

        var img = list[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productsImg.setImageResource(resId)

        holder.productsName.text = list[position].name
        holder.quantity.text = "${list[position].quantity}"
        holder.price.text = "${list[position].price}원"

        var sum = list[position].quantity * list[position].price
        holder.sumPrice.text = "${sum}원"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class CurOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var productsImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productsName: TextView = itemView!!.findViewById(R.id.tv_item_name)
    var quantity: TextView = itemView!!.findViewById(R.id.tv_quantity)
    var price: TextView = itemView!!.findViewById(R.id.tv_single_price)
    var sumPrice:TextView = itemView!!.findViewById(R.id.tv_total_price)
}